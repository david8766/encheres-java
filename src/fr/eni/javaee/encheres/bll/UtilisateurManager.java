package fr.eni.javaee.encheres.bll;

import java.util.ArrayList;
import java.util.List;

import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.DAOFactory;
import fr.eni.javaee.encheres.dal.UtilisateurDAO;

public class UtilisateurManager {
	
	private UtilisateurDAO utilisateurDAO;
	private static UtilisateurManager instance;
	
	public UtilisateurManager() {
		this.utilisateurDAO = DAOFactory.getUtilisateurDAO();
	}
	
	public static UtilisateurManager getInstance() {
		if(instance == null){
			instance = new UtilisateurManager();
		}
		return instance;
	}
	
	public void ajouterUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerUtilisateurInsertion(utilisateur, businessException);
		if(!businessException.hasErreurs()) 
		{
			this.utilisateurDAO.insert(utilisateur);
		}
		else
		{
			throw businessException;
		}
	}
	
	public void modifierUtilisateur(Utilisateur utilisateur) throws BusinessException {
		BusinessException businessException = new BusinessException();
		this.validerUtilisateurModification(utilisateur, businessException);
		if(!businessException.hasErreurs()) 
		{
			this.utilisateurDAO.update(utilisateur);
		}
		else
		{
			throw businessException;
		}
	}
	
	public void supprimerUtilisateur(int id) throws BusinessException {
		this.utilisateurDAO.delete(id);
	}
	
	public List<Utilisateur> getListeUtilisateurs() throws BusinessException {
		return this.utilisateurDAO.selectAll();
	}
	
	public Utilisateur getUtilisateur(int no_utilisateur) throws BusinessException {
		return utilisateurDAO.selectById(no_utilisateur);
		}
	
	public Utilisateur getUtilisateurByPseudo (String pseudo) throws BusinessException{
		return utilisateurDAO.selectByPseudo(pseudo);
	}
	
	public Utilisateur getUtilisateurByEmail (String email) throws BusinessException{
		return utilisateurDAO.selectByEmail(email);
	}
	
	
	
	private void validerUtilisateurInsertion (Utilisateur utilisateur, BusinessException businessException) throws BusinessException {
		this.validerNom(utilisateur.getNom(), businessException);
		this.validerPrenom(utilisateur.getPrenom(), businessException);
		this.validerEmailInsertion(utilisateur.getEmail(), businessException);
		this.validerPseudoInsertion(utilisateur.getPseudo(), businessException);
		this.validerTelephone(utilisateur.getTelephone(), businessException);
		this.validerRue(utilisateur.getRue(), businessException);
		this.validerCodePostal(utilisateur.getCode_postal(), businessException);
		this.validerVille(utilisateur.getVille(), businessException);
		this.validerMotDePasse(utilisateur.getMot_de_passe(), businessException);
	}
	
	private void validerUtilisateurModification (Utilisateur utilisateur, BusinessException businessException) throws BusinessException {
		this.validerNom(utilisateur.getNom(), businessException);
		this.validerPrenom(utilisateur.getPrenom(), businessException);
		this.validerEmailModification(utilisateur.getEmail(), businessException);
		this.validerPseudoModification(utilisateur.getPseudo(), businessException);
		this.validerTelephone(utilisateur.getTelephone(), businessException);
		this.validerRue(utilisateur.getRue(), businessException);
		this.validerCodePostal(utilisateur.getCode_postal(), businessException);
		this.validerVille(utilisateur.getVille(), businessException);
		this.validerMotDePasse(utilisateur.getMot_de_passe(), businessException);
	}
	
	private void validerNom (String nom, BusinessException businessException) {
		if(nom==null)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_NOM_MANQUANT);
		}
		else if(nom.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_NOM_LONG);
		}
	}
	
	private void validerPrenom (String prenom, BusinessException businessException) {
		if(prenom.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PRENOM_MANQUANT);
		}
		else if(prenom.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PRENOM_LONG);
		}
	}
	
	private void validerEmailInsertion (String email, BusinessException businessException) throws BusinessException {
		if(email==null)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_MANQUANT);
		}
		else if(email.length()>200)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_LONG);
		}
		List<Utilisateur>utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs = utilisateurDAO.selectAll();
		for (Utilisateur utilisateur : utilisateurs) {
			if(email.trim().equals(utilisateur.getEmail())) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_DOUBLON);
				break;
			}
		}
	}
	
	private void validerPseudoInsertion (String pseudo, BusinessException businessException) throws BusinessException {
		if(pseudo==null || pseudo.trim().length() <1)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_MANQUANT);
		}
		else if(pseudo.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_LONG);
		}
		else if(!pseudo.matches("[A-Za-z0-9]*"))
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_CARACTERE_NON_AUTORISE);
		}
		List<Utilisateur>utilisateurs = new ArrayList<Utilisateur>();
		utilisateurs = utilisateurDAO.selectAll();
		for (Utilisateur utilisateur : utilisateurs) {
			if(pseudo.trim().equals(utilisateur.getPseudo())) {
				businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_DOUBLON);
				break;
			}
		}
	}
	
	private void validerEmailModification (String email, BusinessException businessException) throws BusinessException {
		if(email==null)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_MANQUANT);
		}
		else if(email.length()>200)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_EMAIL_LONG);
		}
	}
	
	private void validerPseudoModification (String pseudo, BusinessException businessException) throws BusinessException {
		if(pseudo==null || pseudo.trim().length() <1)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_MANQUANT);
		}
		else if(pseudo.length()>30)
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_LONG);
		}
		else if(!pseudo.matches("[A-Za-z0-9]*"))
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_PSEUDO_CARACTERE_NON_AUTORISE);
		}
	}
	
	private void validerTelephone (String telephone, BusinessException businessException) throws BusinessException {
		if(telephone.length() >15) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_TELEPHONE_LONG);
		}
		else if(!telephone.matches("[0-9]*")) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_TELEPHONE_CARACTERE_NON_AUTORISE);
		}
	}
	
	private void validerRue (String rue, BusinessException businessException) throws BusinessException {
		if(rue == null || rue.trim().length() < 1) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_RUE_MANQUANT);
		}
		else if(rue.length() >30) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_RUE_LONG);
		}
	}
	
	private void validerCodePostal (String codePostal, BusinessException businessException) throws BusinessException {
		if(codePostal.trim().length() >10 || codePostal == null || !codePostal.matches("[0-9]*")) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_CODE_POSTAL_ERREUR);
		}
	}
	
	private void validerVille (String ville, BusinessException businessException) throws BusinessException {
		if(ville == null) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_VILLE_MANQUANT);
		}
		else if(ville.length() >30) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_VILLE_LONG);
		}
	}
	
	private void validerMotDePasse (String mdp, BusinessException businessException) throws BusinessException {
		if(mdp == null) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_MOT_DE_PASSE_MANQUANT);
		}
		else if(mdp.length() >30) 
		{
			businessException.ajouterErreur(CodesResultatBLL.REGLE_UTILISATEUR_MOT_DE_PASSE_LONG);
		}
	}
	
}
