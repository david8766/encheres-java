package fr.eni.javaee.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.dal.RetraitDAO;
import fr.eni.javaee.encheres.dal.CodesResultatDAL;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.utils.ErrorLogger;
 
public class RetraitDAOJdbcImpl implements RetraitDAO{
	
	private static Logger logger = ErrorLogger.getLogger("RetraitDAOJdbcImpl");
	
	private static final String SQL_SELECT_BY="SELECT no_article, rue, code_postal, ville, retire FROM RETRAITS WHERE no_article=?;";
	private static final String SQL_SELECT_ALL="SELECT no_article, rue, code_postal, ville, retire FROM RETRAITS ORDER BY no_article;";
	private static final String SQL_INSERT="INSERT INTO RETRAITS (no_article, rue, code_postal, ville) VALUES (?,?,?,?);";
	private static final String SQL_UPDATE="UPDATE RETRAITS SET rue=?, code_postal=?, ville=?, retire=? WHERE no_article=?;";
	private static final String SQL_DELETE="DELETE FROM RETRAITS WHERE no_article=?;";
	
	@Override
	public Retrait selectById(int no_article) throws BusinessException {
		
		Retrait retrait=null;
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_BY);
			stmt.setInt(1, no_article);
			ResultSet rs=stmt.executeQuery();
			if (rs.next()) {
				retrait=retraitBuilder(rs);
			}
		;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_RETRAIT_ECHEC);
			throw businessException;
		}
		return retrait;
	}

	@Override
	public List<Retrait> selectAll() throws BusinessException {
		
		List<Retrait> liste=new ArrayList<>();
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_ALL);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				Retrait retrait=new Retrait();
				retrait=retraitBuilder(rs);
				liste.add(retrait);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_RETRAIT_ECHEC);
			throw businessException;
		}
		
		return liste;
		
	}

	@Override
	public void update(Retrait obj) throws BusinessException {
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, obj.getRue());
			stmt.setString(2, obj.getCode_postal());
			stmt.setString(3, obj.getVille());
			stmt.setBoolean(4, obj.getRetire());
			stmt.setInt(5, obj.getArticle().getNo_article());
			stmt.executeUpdate();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_RETRAIT_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_RETRAIT_ECHEC);
			throw businessException;
		}
		
	}

	@Override
	public void insert(Retrait obj) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setInt(1, obj.getArticle().getNo_article());
			stmt.setString(2, obj.getRue());
			stmt.setString(3, obj.getCode_postal());
			stmt.setString(4, obj.getVille());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
			{
				ArticleVenduManager articleManager = ArticleVenduManager.getInstance();
				obj.setArticle(articleManager.getArticleVendu(rs.getInt(1)));
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_RETRAIT_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_RETRAIT_ECHEC);
			throw businessException;
		}
		
	}

	@Override
	public void delete(int no_article) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, no_article);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_RETRAIT_ECHEC);
			throw businessException;
		}
		
	}

	//Charge l'objet à partir du ResultSet
	private Retrait retraitBuilder(ResultSet rs) throws BusinessException, SQLException {
		
		Retrait retrait = new Retrait();
		ArticleVendu articleVendu = new ArticleVendu();
		
		try {
			articleVendu.setNo_article(rs.getInt("no_article"));
			retrait.setArticle(articleVendu);
			retrait.setRue(rs.getString("rue"));
			retrait.setCode_postal(rs.getString("code_postal"));
			retrait.setVille(rs.getString("ville"));
			retrait.setRetire(rs.getBoolean("retire"));
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.BUILDER_RETRAIT_ECHEC);
			throw businessException;
		}

		return retrait;
	}
	
}
