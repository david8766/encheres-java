package fr.eni.javaee.encheres.bll;

import java.util.List;

import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.dal.RetraitDAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.messages.BusinessException;

public class RetraitManager {

	private static RetraitManager instance;
	private static RetraitDAO DAORetrait;

	private RetraitManager() {
		DAORetrait=DAOFactory.getRetraitDAO();
	}

	public static RetraitManager getInstance() {
		if (instance == null) {
			return new RetraitManager();
		}
		return instance;
	}

	public List<Retrait> getListeRetraits() throws BusinessException {
		return DAORetrait.selectAll();
	}
	
	public Retrait getRetrait(int no_article) throws BusinessException {
		return DAORetrait.selectById(no_article);
	}
	
	public void createRetrait(Retrait retrait) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		this.validerArticle(retrait.getArticle(), businessException);
		this.validerRue(retrait.getRue(), businessException);
		this.validerCode_postal(retrait.getCode_postal(), businessException);
		this.validerVille(retrait.getVille(), businessException);
		
		if(!businessException.hasErreurs()) {
			DAORetrait.insert(retrait);
		}
		else
		{
			throw businessException;
		}
		
	}
	
	public void updateRetrait(Retrait retrait) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		this.validerArticle(retrait.getArticle(), businessException);
		this.validerRue(retrait.getRue(), businessException);
		this.validerCode_postal(retrait.getCode_postal(), businessException);
		this.validerVille(retrait.getVille(), businessException);

		if(!businessException.hasErreurs()) {
			DAORetrait.update(retrait);
		}
		else
		{
			throw businessException;
		}

	}
	
	public void deleteRetrait(int no_retrait) throws BusinessException {
		DAORetrait.delete(no_retrait);
	}

	// Controles metiers
	
	private void validerArticle (ArticleVendu articleVendu, BusinessException businessException) {
		try {
			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			if(articleVenduManager.getArticleVendu(articleVendu.getNo_article()) == null) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_ARTICLE_INCONNU);
			}
		} catch (BusinessException e) {
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_ARTICLE_INCONNU);
		}
	}	
	
	private void validerRue (String rue, BusinessException businessException) {
		if(rue==null || rue.trim().length()==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_RUE_MANQUANT);
		}
		else if (rue.trim().length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_RUE_LONG);
		}
	}	
	
	private void validerCode_postal (String code_postal, BusinessException businessException) {
		if(code_postal==null || code_postal.trim().length()==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_CODE_POSTAL_MANQUANT);
		}
		else if (code_postal.trim().length()>15)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_CODE_POSTAL_LONG);
		}
	}	
	
	private void validerVille (String ville, BusinessException businessException) {
		if(ville==null || ville.trim().length()==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_VILLE_MANQUANT);
		}
		else if (ville.trim().length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_RETRAIT_VILLE_LONG);
		}
	}
	
}
