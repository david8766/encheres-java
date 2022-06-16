package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletAdministrationCategorieSupprimer
 */
@WebServlet("/SupprimerCategorie")
public class ServletAdministrationCategorieSupprimer extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
	
		try {
			if (request.getParameter("categorie") != null) {
				int noCategorie;
				noCategorie = Integer.parseInt(request.getParameter("categorie"));
				if (noCategorie > 0) {
					supprimerUneCategorie(noCategorie);
				}		
			}
			
			// Récupération de la liste des catégories
			List<Categorie> listeCategories = new ArrayList<Categorie>();
			listeCategories = selectionnerToutesLesCategories();
			request.setAttribute("listeCategories", listeCategories);
			
			// Récupération de la liste des utilisateurs
			List<Utilisateur> listeUtilisateurs = new ArrayList<Utilisateur>();
			listeUtilisateurs = selectionnerTousLesUtilisateurs();
			request.setAttribute("listeUtilisateurs", listeUtilisateurs);
			
		} catch (BusinessException e) {
			// Récupération de la liste des codes d'erreurs
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
			request.setAttribute("listeCodesErreur", listeCodesErreur);
		}
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administration.jsp");
		rd.forward(request, response);
	}

	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//

	/**
	 * @param int categorie
	 * @throws BusinessException
	 */
	public void supprimerUneCategorie(int categorie) throws BusinessException{
		CategorieManager cm = CategorieManager.getInstance();
		cm.deleteCategorie(categorie);
	}
	
	/**
	 * @return List<Categorie> listeCategories
	 * @throws BusinessException
	 */
	public List<Categorie> selectionnerToutesLesCategories() throws BusinessException{
		List<Categorie> listeCategories = new ArrayList<Categorie>();	
		CategorieManager cm = CategorieManager.getInstance();
		listeCategories = cm.getListeCategories();


		return listeCategories;
	}
	
	/**
	 * @return List<Utilisateur>
	 * @throws BusinessException
	 */
	public List<Utilisateur> selectionnerTousLesUtilisateurs() throws BusinessException {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		UtilisateurManager um = UtilisateurManager.getInstance();
		utilisateurs = um.getListeUtilisateurs();
		return utilisateurs;
	}

}
