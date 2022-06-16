package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

public interface UtilisateurDAO extends DAO<Utilisateur>{
	
	public Utilisateur selectByPseudo(String pseudo) throws BusinessException;
	public Utilisateur selectByEmail(String email) throws BusinessException;
	
}
