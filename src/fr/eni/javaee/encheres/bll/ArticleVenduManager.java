package fr.eni.javaee.encheres.bll;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.dal.ArticleVenduDAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.messages.BusinessException;

public class ArticleVenduManager {
	
	private static ArticleVenduManager instance;
	private static ArticleVenduDAO DAOArticleVendu;

	private ArticleVenduManager() {
		DAOArticleVendu=DAOFactory.getArticleVenduDAO();
	}

	public static ArticleVenduManager getInstance() {
		if (instance == null) {
			return new ArticleVenduManager();
		}
		return instance;
	}
	
	public List<ArticleVendu> getListeArticlesVendu() throws BusinessException {
		// Tous les articles de la base de données
		return DAOArticleVendu.selectAll();
	}

	public List<ArticleVendu> getListeEncheresEncoursToutes(int no_categorie, String nom_article) throws BusinessException {
		// Toutes les encheres en cours selon la catégorie de tous les vendeurs
		return DAOArticleVendu.selectAllEnCours(no_categorie,nom_article);
	}
	
	public List<ArticleVendu> getListeEncheresEncoursAutresVendeurs(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Encheres en-cours sauf celles de l'utilisateur selon la catégorie indiquée
		return DAOArticleVendu.selectEncheresEnCours(no_utilisateur,no_categorie,nom_article);
	}

	
	public List<ArticleVendu> getListeEncheresEncoursUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Encheres en-cours de l'utilisateur indiqué pour la categorie indiquée
		return DAOArticleVendu.selectEncheresEnCoursUtilisateur(no_utilisateur,no_categorie,nom_article);
	}
	
	public List<ArticleVendu> getListeEncheresRemporteesUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Encheres remportées de l'utilisateur indiqué pour la categorie
		return DAOArticleVendu.selectEncheresRemporteesUtilisateur(no_utilisateur,no_categorie,nom_article);
	}
	
	public List<ArticleVendu> getListeVentesEncoursUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Ventes en cours de l'utilisateur pour la categorie indiquée
		return DAOArticleVendu.selectVentesEnCoursUtilisateur(no_utilisateur,no_categorie,nom_article);
	}
	
	public List<ArticleVendu> getListeVentesAVenirsUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Ventes à venir de l'utilisateur pour la categorie indiquée
		return DAOArticleVendu.selectVentesAVenirUtilisateur(no_utilisateur,no_categorie,nom_article);
	}
	
	public List<ArticleVendu> getListeVentesTermineesUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		//Ventes fermées de l'utilisateur pour la categorie indiquée
		return DAOArticleVendu.selectVentesTermineesUtilisateur(no_utilisateur,no_categorie,nom_article);
	}

	
	public ArticleVendu getArticleVendu(int no_article) throws BusinessException {
		return DAOArticleVendu.selectById(no_article);
	}
	
	public void createArticleVendu(ArticleVendu articleVendu) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		
		this.validerNom(articleVendu.getNom_article(), businessException);
		this.validerDescription(articleVendu.getNom_article(), businessException);
		this.validerDates(articleVendu.getDate_debut_encheres(), articleVendu.getDate_fin_encheres(), businessException);
		this.validerPrixInitial(articleVendu.getPrix_initial(), businessException);
		this.validerPrixVente(articleVendu.getPrix_vente(), articleVendu.getPrix_initial(), businessException);
		this.validerUtilisateur(articleVendu.getVendeur().getNo_utilisateur(), businessException);
		this.validerCategorie(articleVendu.getCategorie().getNo_categorie(), businessException);
		
		if(!businessException.hasErreurs()) {
			DAOArticleVendu.insert(articleVendu);
		}
		else
		{
			throw businessException;
		}
		
	}
	
	public void updateArticleVendu(ArticleVendu articleVendu) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		
		this.validerNom(articleVendu.getNom_article(), businessException);
		this.validerDescription(articleVendu.getNom_article(), businessException);
		this.validerDates(articleVendu.getDate_debut_encheres(), articleVendu.getDate_fin_encheres(), businessException);
		this.validerPrixInitial(articleVendu.getPrix_initial(), businessException);
		this.validerPrixVente(articleVendu.getPrix_vente(), articleVendu.getPrix_initial(), businessException);
		this.validerUtilisateur(articleVendu.getVendeur().getNo_utilisateur(), businessException);
		this.validerCategorie(articleVendu.getCategorie().getNo_categorie(), businessException);
		
		if(!businessException.hasErreurs()) {
			DAOArticleVendu.update(articleVendu);
		}
		else
		{
			throw businessException;
		}

	}
	
	public void deleteArticleVendu(int no_article) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		
		this.validerSuppression(no_article, businessException);
		
		if(!businessException.hasErreurs()) {

			try {
				
				// supprime l'article
				DAOArticleVendu.delete(no_article);
				
				// supprime l'adresse de retrait
				RetraitManager retraitManager = RetraitManager.getInstance();
				retraitManager.deleteRetrait(no_article);
				
			} catch (Exception e) {
				throw businessException;
			}
			
		}
		else
		{
			throw businessException;
		}
		
	}

	// Controles metiers
	
	private void validerSuppression (int no_article, BusinessException businessException) {
		
		try {
			ArticleVendu articleVendu = DAOArticleVendu.selectById(no_article);
			if (articleVendu!=null) {
				Date date = new Date();
				LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				if (articleVendu.getDate_debut_encheres().isBefore(today)) {
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_ENCHERE_EN_COURS);
				}
			}
		} catch (Exception e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_SUPPRIMER);
		}
		
	}
	
	private void validerNom (String nom, BusinessException businessException) {
		if(nom==null || nom.trim().length()==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_LIBELLE_MANQUANT);
		}
		else if (nom.trim().length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_LIBELLE_LONG);
		}
	}	
	
	private void validerDescription (String description, BusinessException businessException) {
		if (description.trim().length()>300)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_DESCRIPTION_LONG);
		}
	}
	
	private void validerDates (LocalDate dateDebut, LocalDate dateFin, BusinessException businessException) {
		
		// recupere la date du jour et la caste en LocalDate
		Date date = new Date();
		LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
		
		if (dateDebut==null || dateFin==null)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_DATES_NULL);
		}
		else if (dateDebut.isBefore(today))
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_DATE_PASSEE);
		}
		else if (dateFin.isBefore(dateDebut))
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_DATE_INCOHERENTE);
		}
	}
	
	private void validerPrixInitial (int prixInitial, BusinessException businessException) {
		if (prixInitial<=0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_PRIX_INITIAL);
		}
	}
	
	private void validerPrixVente (int prixVente, int prixInitial, BusinessException businessException) {
		if (prixVente<0 || (prixVente>0 && prixVente<prixInitial))
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_PRIX_VENTE);
		}
	}	
	
	private void validerCategorie (int no_categorie, BusinessException businessException) {
		if (no_categorie==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_CATEGORIE_MANQUANTE);
		}
		else
		{
			// Verifie si la categorie existe
			CategorieManager categorieManager = CategorieManager.getInstance(); 
			try {
				if(categorieManager.getCategorie(no_categorie) == null) {
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_CATEGORIE_INCONNUE);
				}
			} catch (BusinessException e) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_CATEGORIE_INCONNUE);
			}
		}
	}
	
	private void validerUtilisateur (int no_utilisateur, BusinessException businessException) {
		if (no_utilisateur==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_VENDEUR_MANQUANT);
		}
		else
		{
			// Verifie si la categorie existe
			UtilisateurManager utilisateurManager = UtilisateurManager.getInstance(); 
			try {
				if(utilisateurManager.getUtilisateur(no_utilisateur) == null) {
					businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_VENDEUR_INCONNU);
				}
			} catch (BusinessException e) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_ARTICLE_VENDEUR_INCONNU);
			}
		}
	}
	
}
