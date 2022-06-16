package fr.eni.javaee.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.dal.EnchereDAO;
import fr.eni.javaee.encheres.dal.CodesResultatDAL;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.utils.ErrorLogger;

public class EnchereDAOJdbcImpl implements EnchereDAO{
	
	private static Logger logger = ErrorLogger.getLogger("EnchereDAOJdbcImpl");
	
	private static final String SQL_SELECT_BY_ARTICLE="SELECT no_utilisateur, no_article, date_enchere, montant_enchere FROM ENCHERES WHERE no_article=? ORDER BY date_enchere;";
	private static final String SQL_SELECT_BY_TOP_ARTICLE="SELECT TOP 1 no_utilisateur, no_article, date_enchere, montant_enchere FROM ENCHERES WHERE no_article=? ORDER BY montant_enchere DESC;";
	private static final String SQL_SELECT_ALL="SELECT no_utilisateur, no_article, date_enchere, montant_enchere FROM ENCHERES ORDER BY no_article, no_utilisateur;";
	private static final String SQL_INSERT=""
			+ "BEGIN TRANSACTION; "
			+ "INSERT INTO ENCHERES (no_utilisateur, no_article, date_enchere, montant_enchere) VALUES (?,?,?,?); "
			+ "UPDATE ARTICLES_VENDUS SET prix_vente=? WHERE no_article=?; "
			+ "COMMIT TRANSACTION; ";
	
	@Override
	public List<Enchere> selectByArticle(int no_article) throws BusinessException {
		// renvoie la liste des enchères de l'article
		
		List<Enchere> liste=new ArrayList<>();
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_BY_ARTICLE);
			stmt.setInt(1,no_article);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				Enchere enchere=new Enchere();
				enchere=enchereBuilder(rs);
				liste.add(enchere);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ENCHERE_ECHEC);
			throw businessException;
		}
		
		return liste;
	}
	
	@Override
	public Enchere selectByTopArticle(int no_article) throws BusinessException {
		// renvoie la meilleure enchère de l'article
		
		Enchere enchere = null;
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_BY_TOP_ARTICLE);
			stmt.setInt(1,no_article);
			ResultSet rs=stmt.executeQuery();
			if (rs.next()) {
				enchere=new Enchere();
				enchere=enchereBuilder(rs);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ENCHERE_ECHEC);
			throw businessException;
		}
		
		return enchere;
	}
	@Override
	public List<Enchere> selectAll() throws BusinessException {
		
		List<Enchere> liste=new ArrayList<>();
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_ALL);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				Enchere enchere=new Enchere();
				enchere=enchereBuilder(rs);
				liste.add(enchere);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_ENCHERE_ECHEC);
			throw businessException;
		}
		
		return liste;
		
	}
	
	@Override
	public void insert(Enchere obj) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_INSERT);
			stmt.setInt(1, obj.getEncherisseur().getNo_utilisateur());
			stmt.setInt(2, obj.getArticle().getNo_article());
			stmt.setDate(3, java.sql.Date.valueOf(obj.getDate_enchere()));
			stmt.setInt(4, obj.getMontant_enchere());
			stmt.setInt(5, obj.getMontant_enchere());
			stmt.setInt(6, obj.getArticle().getNo_article());
			stmt.executeUpdate();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_ENCHERE_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_ENCHERE_ECHEC);
			throw businessException;
		}
		
	}
	
	@Override
	public void update(Enchere obj) throws BusinessException {
		// Pas d'update autorisé
		BusinessException businessException = new BusinessException();
		businessException.ajouterErreur(CodesResultatDAL.UPDATE_ENCHERE_ECHEC);
		throw businessException;
	}

	@Override
	public void delete(int no_enchere) throws BusinessException {
		// Pas de delete autorisé
		BusinessException businessException = new BusinessException();
		businessException.ajouterErreur(CodesResultatDAL.DELETE_ENCHERE_ECHEC);
		throw businessException;
	}

	//Charge l'objet à partir du ResultSet
	private Enchere enchereBuilder(ResultSet rs) throws BusinessException, SQLException {
		
		Enchere enchere = new Enchere();
		
		try {

			ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
			ArticleVendu articleVendu = articleVenduManager.getArticleVendu(rs.getInt("no_article"));
			
			UtilisateurManager utilisateurManager = UtilisateurManager.getInstance();
			Utilisateur encherisseur = utilisateurManager.getUtilisateur(rs.getInt("no_utilisateur"));
			
			enchere.setArticle(articleVendu);
			enchere.setEncherisseur(encherisseur);
			enchere.setDate_enchere(rs.getDate("date_enchere").toLocalDate());
			enchere.setMontant_enchere(rs.getInt("montant_enchere"));

		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.BUILDER_ENCHERE_ECHEC);
			throw businessException;
		}

		return enchere;
	}

	@Override
	public Enchere selectById(int id) throws BusinessException {
		return null;
	}

}
