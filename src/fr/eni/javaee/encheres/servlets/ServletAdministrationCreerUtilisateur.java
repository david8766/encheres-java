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
 * Servlet implementation class ServletAdministrationCreerUtilisateur
 */
@WebServlet("/AdministrationCreerUtilisateur")
public class ServletAdministrationCreerUtilisateur extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administrationCreerUtilisateur.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		try {
			// Ajout de l'utilisateur
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setPseudo(request.getParameter("pseudo"));
			utilisateur.setNom(request.getParameter("nom"));
			utilisateur.setPrenom(request.getParameter("prenom"));
			utilisateur.setEmail(request.getParameter("email"));
			utilisateur.setRue(request.getParameter("rue"));
			utilisateur.setTelephone("0699887766");
			utilisateur.setCode_postal(request.getParameter("code_postal"));
			utilisateur.setVille(request.getParameter("ville"));
			utilisateur.setMot_de_passe("password");
			utilisateur.setAdministrateur(false);
			utilisateur.setCredit(0);
			
			UtilisateurManager um = UtilisateurManager.getInstance();
			um.ajouterUtilisateur(utilisateur);
			
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
			System.out.println(listeCodesErreur);
			request.setAttribute("listeCodesErreur", listeCodesErreur);
			request.setAttribute("pseudo", request.getParameter("pseudo"));
			request.setAttribute("nom", request.getParameter("nom"));
			request.setAttribute("prenom", request.getParameter("prenom"));
			request.setAttribute("email", request.getParameter("email"));
			request.setAttribute("rue", request.getParameter("rue"));
			request.setAttribute("code_postal", request.getParameter("code_postal"));
			request.setAttribute("ville", request.getParameter("ville"));
			RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administrationCreerUtilisateur.jsp");
			rd.forward(request, response);
		}
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/administration.jsp");
		rd.forward(request, response);
		
	}
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//

	/**
	 * @param Utilisateur utilisateur
	 * @throws BusinessException
	 */
	public void creerUtilisateur(Utilisateur utilisateur) throws BusinessException{
		UtilisateurManager um = UtilisateurManager.getInstance();
		um.ajouterUtilisateur(utilisateur);
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
