package fr.eni.javaee.encheres.bll;

import java.util.List;

import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.dal.CategorieDAO;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.messages.BusinessException;

public class CategorieManager {

	private static CategorieManager instance;
	private static CategorieDAO DAOCategorie;

	private CategorieManager() {
		DAOCategorie=DAOFactory.getCategorieDAO();
	}

	public static CategorieManager getInstance() {
		if (instance == null) {
			return new CategorieManager();
		}
		return instance;
	}

	public List<Categorie> getListeCategories() throws BusinessException {
		return DAOCategorie.selectAll();
	}
	
	public Categorie getCategorie(int no_categorie) throws BusinessException {
		return DAOCategorie.selectById(no_categorie);
	}
	
	public void createCategorie(Categorie categorie) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		this.validerLibelle(categorie.getLibelle(), businessException);

		if(!businessException.hasErreurs()) {
			DAOCategorie.insert(categorie);
		}
		else
		{
			throw businessException;
		}
		
	}
	
	public void updateCategorie(Categorie categorie) throws BusinessException {
		
		BusinessException businessException = new BusinessException();
		this.validerLibelle(categorie.getLibelle(), businessException);

		if(!businessException.hasErreurs()) {
			DAOCategorie.update(categorie);
		}
		else
		{
			throw businessException;
		}

	}
	
	public void deleteCategorie(int no_categorie) throws BusinessException {
		DAOCategorie.delete(no_categorie);
	}

	// Controles metiers
	
	private void validerLibelle (String libelle, BusinessException businessException) {
		if(libelle==null || libelle.trim().length()==0)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_CATEGORIE_LIBELLE_MANQUANT);
		}
		else if (libelle.trim().length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_CATEGORIE_LIBELLE_LONG);
		}
	}
	
}
