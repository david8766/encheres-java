package fr.eni.javaee.encheres.dal;

import java.util.List;

import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.messages.BusinessException;

public interface EnchereDAO extends DAO<Enchere>{

	List<Enchere> selectByArticle(int no_article) throws BusinessException;

	Enchere selectByTopArticle(int no_article) throws BusinessException;

}
