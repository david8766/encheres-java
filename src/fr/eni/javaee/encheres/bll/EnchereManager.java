package fr.eni.javaee.encheres.bll;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.EnchereDAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.messages.BusinessException;

public class EnchereManager {

	private static EnchereManager instance;
	private static EnchereDAO DAOEnchere;

	private EnchereManager() {
		DAOEnchere=DAOFactory.getEnchereDAO();
	}

	public static EnchereManager getInstance() {
		if (instance == null) {
			return new EnchereManager();
		}
		return instance;
	}

	public List<Enchere> getListeEncheres() throws BusinessException {
		return DAOEnchere.selectAll();
	}

	public List<Enchere> getListeEncheres(int no_article) throws BusinessException {
		return DAOEnchere.selectByArticle(no_article);
	}

	public Enchere getMeilleureEnchere(int no_article) throws BusinessException {
		return DAOEnchere.selectByTopArticle(no_article);
	}
	
	public Enchere getEnchere(int no_article) throws BusinessException {
		return DAOEnchere.selectById(no_article);
	}
	
	public void createEnchere(Enchere enchere) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		
		this.validerArticle(enchere.getArticle(), businessException);
		this.validerUtilisateur(enchere.getEncherisseur(), businessException);
		this.validerDate(enchere.getDate_enchere(), enchere.getArticle(), businessException);
		this.validerMontant(enchere.getMontant_enchere(), enchere.getArticle(), enchere.getEncherisseur(),businessException);
		
		if(!businessException.hasErreurs()) {
			DAOEnchere.insert(enchere);
		}
		else
		{
			throw businessException;
		}
		
	}
	
	public void updateEnchere(Enchere enchere) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		this.validerArticle(enchere.getArticle(), businessException);
		this.validerUtilisateur(enchere.getEncherisseur(), businessException);
		this.validerDate(enchere.getDate_enchere(), enchere.getArticle(), businessException);
		this.validerMontant(enchere.getMontant_enchere(), enchere.getArticle(), enchere.getEncherisseur(),businessException);

		if(!businessException.hasErreurs()) {
			DAOEnchere.update(enchere);
		}
		else
		{
			throw businessException;
		}

	}
	
	public void deleteEnchere(int no_enchere) throws BusinessException {
		DAOEnchere.delete(no_enchere);
	}

	// Controles metiers
	private void validerArticle (ArticleVendu articleVendu, BusinessException businessException) {
		try {
			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			if(articleVenduManager.getArticleVendu(articleVendu.getNo_article()) == null) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INCONNU);
			}
		} catch (BusinessException e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INCONNU);
		}
	}	

	private void validerUtilisateur (Utilisateur utilisateur, BusinessException businessException) {
		try {
			UtilisateurManager utilisateurManager = UtilisateurManager.getInstance();
			if(utilisateurManager.getUtilisateur(utilisateur.getNo_utilisateur()) == null) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_UTILISATEUR_INCONNU);
			}
		} catch (BusinessException e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_UTILISATEUR_INCONNU);
		}
	}	
	
	private void validerDate (LocalDate date_enchere, ArticleVendu articleVendu, BusinessException businessException) {
		
		if(date_enchere==null)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_DATE_MANQUANTE);
		}else
		{
			
			Date date = new Date();
			LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			ArticleVendu article;
			
			try {
				article = articleVenduManager.getArticleVendu(articleVendu.getNo_article());
				if(article != null) {
					if(date_enchere.isBefore(today) || date_enchere.isBefore(article.getDate_debut_encheres()) || date_enchere.isAfter(article.getDate_fin_encheres()))
					{
						businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_DATE_INVALIDE);
					}
				}else {
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INTROUVABLE);
				}
			} catch (BusinessException e) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INTROUVABLE);
			}
		}
	}	
	
	private void validerMontant (int montant_enchere, ArticleVendu articleVendu, Utilisateur encherisseur, BusinessException businessException) {
		
		// Controle credit encherisseur
		/* à remettre qd le credit sera géré
		try {
			UtilisateurManager utilisateurManager = UtilisateurManager.getInstance();
			if(utilisateurManager.getUtilisateur(encherisseur.getNo_utilisateur()) != null) {
				if(montant_enchere <= encherisseur.getCredit()) {
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_CREDIT_INSUFFISANT);
				}
			}else {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_UTILISATEUR_INCONNU);
			}
		} catch (BusinessException e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_UTILISATEUR_INCONNU);
		}
		*/
		
		// Controle le montant de l'enchere
		ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
		ArticleVendu article;
		
		try {
			article = articleVenduManager.getArticleVendu(articleVendu.getNo_article());
			if(article != null) {			
				if(montant_enchere <= article.getPrix_initial() 
					|| (article.getPrix_vente()>0 && montant_enchere <= article.getPrix_vente()) 
				)
				{
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_INSUFFISANTE);
				}
			}else {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INTROUVABLE);
			}
		} catch (BusinessException e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ENCHERE_ARTICLE_INTROUVABLE);
		}
	}	
	
}
