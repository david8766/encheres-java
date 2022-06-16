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
 * Servlet implementation class ServletAdministrationCategorie
 */
@WebServlet("/AdministrationDesCategories")
public class ServletAdministrationCategorie extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Récupération de l'action à éffectuer
		String action = request.getParameter("action");
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		try {
			if (action.equals("creer")) {
				request.setAttribute("creer", action);
			}
			if (action.equals("modifier") && request.getParameter("categorie") != null) {
				request.setAttribute("modifier", action);
				int noCategorie = Integer.parseInt(request.getParameter("categorie"));
				//Récupération catégorie choisie
				if (noCategorie > -1) {
					Categorie categorie = selectionnerUneCategorie(noCategorie);
					System.out.println(categorie);
					request.setAttribute("categorie", categorie);
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
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administrationCategorie.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Récupération de l'action à éffectuer
		String action = request.getParameter("action");
				;
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		try {
			if (action.equals("creer")) {
				Categorie categorie = new Categorie();
				categorie.setLibelle(request.getParameter("libelle"));
				creerCategorie(categorie);
			}
			if (action.equals("modifier") && request.getParameter("categorie") != null) {
				int noCategorie;
				Categorie categorie = null;
				noCategorie = Integer.parseInt(request.getParameter("categorie"));
				//Récupération catégorie
				if (noCategorie > 0) {
					categorie = selectionnerUneCategorie(noCategorie);
				}
				
				categorie.setLibelle(request.getParameter("libelle"));
				updateCategorie(categorie);
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
	public void creerCategorie(Categorie categorie) throws BusinessException{
		CategorieManager cm = CategorieManager.getInstance();
		cm.createCategorie(categorie);
	}
	
	/**
	 * @param int noCategorie
	 * @return Categorie categorie
	 * @throws BusinessException
	 */
	public Categorie selectionnerUneCategorie(int noCategorie) throws BusinessException{
		Categorie categorie = null;	
		CategorieManager cm = CategorieManager.getInstance();
		categorie = cm.getCategorie(noCategorie);
		return categorie;
	}
	
	/**
	 * @param int categorie
	 * @throws BusinessException
	 */
	public void updateCategorie(Categorie categorie) throws BusinessException{
		CategorieManager cm = CategorieManager.getInstance();
		cm.updateCategorie(categorie);
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
	 * @return List<Utilisateur> utilisateurs
	 * @throws BusinessException
	 */
	public List<Utilisateur> selectionnerTousLesUtilisateurs() throws BusinessException {
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		UtilisateurManager um = UtilisateurManager.getInstance();
		utilisateurs = um.getListeUtilisateurs();
		return utilisateurs;
	}

}
