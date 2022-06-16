package fr.eni.javaee.encheres.dal;

import fr.eni.javaee.encheres.dal.UtilisateurDAO;
import fr.eni.javaee.encheres.dal.jdbc.ArticleVenduDAOJdbcImpl;
import fr.eni.javaee.encheres.dal.jdbc.CategorieDAOJdbcImpl;
import fr.eni.javaee.encheres.dal.jdbc.EnchereDAOJdbcImpl;
import fr.eni.javaee.encheres.dal.jdbc.RetraitDAOJdbcImpl;
import fr.eni.javaee.encheres.dal.jdbc.UtilisateurDAOJdbcImpl;

public class DAOFactory {
	
	public static UtilisateurDAO getUtilisateurDAO() 
	{
		return new UtilisateurDAOJdbcImpl();
	}
	
	public static RetraitDAO getRetraitDAO()
	{
		return new RetraitDAOJdbcImpl();
	}
	
	public static CategorieDAO getCategorieDAO()
	{
		return new CategorieDAOJdbcImpl();
	}
	
	public static ArticleVenduDAO getArticleVenduDAO()
	{
		return new ArticleVenduDAOJdbcImpl();
	}
	
	public static EnchereDAO getEnchereDAO()
	{
		return new EnchereDAOJdbcImpl();
	}
	
}
