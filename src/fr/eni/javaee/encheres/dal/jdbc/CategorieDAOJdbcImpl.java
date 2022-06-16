package fr.eni.javaee.encheres.dal.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.dal.CategorieDAO;
import fr.eni.javaee.encheres.dal.CodesResultatDAL;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.utils.ErrorLogger;
 
public class CategorieDAOJdbcImpl implements CategorieDAO{
	
	private static Logger logger = ErrorLogger.getLogger("CategorieDAOJdbcImpl");
	
	private static final String SQL_SELECT_BY="SELECT no_categorie, libelle FROM CATEGORIES WHERE no_categorie=?;";
	private static final String SQL_SELECT_ALL="SELECT no_categorie, libelle FROM CATEGORIES ORDER BY libelle;";
	private static final String SQL_INSERT="INSERT INTO CATEGORIES (libelle) VALUES (?);";
	private static final String SQL_UPDATE="UPDATE CATEGORIES SET libelle=? WHERE no_categorie=?;";
	private static final String SQL_DELETE="DELETE FROM CATEGORIES WHERE no_categorie=?;";
	
	@Override
	public Categorie selectById(int no_categorie) throws BusinessException {
		
		Categorie categorie=null;
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_BY);
			stmt.setInt(1, no_categorie);
			ResultSet rs=stmt.executeQuery();
			if (rs.next()) {
				categorie=categorieBuilder(rs);
			}
		;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_CATEGORIE_ECHEC);
			throw businessException;
		}
		
		return categorie;
		
	}

	@Override
	public List<Categorie> selectAll() throws BusinessException {
		
		List<Categorie> liste=new ArrayList<>();
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_SELECT_ALL);
			ResultSet rs=stmt.executeQuery();
			while (rs.next()) {
				Categorie categorie=new Categorie();
				categorie=categorieBuilder(rs);
				liste.add(categorie);
			}
		
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.SELECT_CATEGORIE_ECHEC);
			throw businessException;
		}
		
		return liste;
		
	}

	@Override
	public void update(Categorie obj) throws BusinessException {
		
		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_UPDATE);
			stmt.setString(1, obj.getLibelle());
			stmt.setInt(2, obj.getNo_categorie());
			stmt.executeUpdate();
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_CATEGORIE_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.UPDATE_CATEGORIE_ECHEC);
			throw businessException;
		}
		
	}


	@Override
	public void insert(Categorie obj) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_INSERT, PreparedStatement.RETURN_GENERATED_KEYS);
			stmt.setString(1, obj.getLibelle());
			stmt.executeUpdate();
			
			ResultSet rs = stmt.getGeneratedKeys();
			if(rs.next())
			{
				obj.setNo_categorie(rs.getInt(1));
			}
			
		} catch (NullPointerException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_CATEGORIE_NULL);
			throw businessException;
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.INSERT_CATEGORIE_ECHEC);
			throw businessException;
		}
		
	}

	@Override
	public void delete(int no_categorie) throws BusinessException {

		try(Connection cn=ConnectionProvider.getConnection())
		{
			PreparedStatement stmt = cn.prepareStatement(SQL_DELETE);
			stmt.setInt(1, no_categorie);
			stmt.executeUpdate();
			
		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.DELETE_CATEGORIE_ECHEC);
			throw businessException;
		}
		
	}

	//Charge l'objet à partir du ResultSet
	private Categorie categorieBuilder(ResultSet rs) throws BusinessException, SQLException {
		
		Categorie categorie = new Categorie();
		
		try {

			categorie.setNo_categorie(rs.getInt("no_categorie"));
			categorie.setLibelle(rs.getString("libelle"));

		} catch (SQLException e) {
			e.printStackTrace();
			logger.severe(e.getMessage());
			BusinessException businessException = new BusinessException();
			businessException.ajouterErreur(CodesResultatDAL.BUILDER_CATEGORIE_ECHEC);
			throw businessException;
		}

		return categorie;
	}
	
}
