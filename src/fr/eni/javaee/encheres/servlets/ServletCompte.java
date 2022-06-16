package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletCompte
 */
@WebServlet(urlPatterns= {
							"/creer",
							"/modifier",
							"/supprimer"
						 })
public class ServletCompte extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		List<Integer> listeCodesErreur = new ArrayList<>();
		if(request.getServletPath().equals("/creer"))
		{
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/compte.jsp");
			rd.forward(request, response);
		}else if (request.getServletPath().equals("/modifier")){
			HttpSession session = request.getSession();
			Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
			UtilisateurManager manager = new UtilisateurManager();
			int id = utilisateur.getNo_utilisateur();
			try {
				utilisateur = manager.getUtilisateur(id);
			} catch (BusinessException e) {
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
				request.setAttribute("listeCodesErreur", listeCodesErreur);
			}
			session.setAttribute("utilisateur", utilisateur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/compte.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		//Initialisation de la liste d'erreur et récupération des information de session
		List<Integer> listeCodesErreur = new ArrayList<>();
		HttpSession session = request.getSession();
		
		//Cas de la suppression
		if(request.getServletPath().equals("/supprimer")){
			UtilisateurManager manager = new UtilisateurManager();
			Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
			try {
				manager.supprimerUtilisateur(utilisateur.getNo_utilisateur());
				response.sendRedirect(request.getContextPath()+"/ListeEncheres");
			} catch (BusinessException e) {
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/compte.jsp");
				rd.forward(request, response);
			}
			response.sendRedirect(request.getContextPath()+"/ListeEncheres");
			
		//Cas de la modification ou la création d'un nouvel utilisateur	
		}else {
			Utilisateur utilisateur = new Utilisateur();
			utilisateur.setPseudo(request.getParameter("pseudo"));
			utilisateur.setNom(request.getParameter("nom"));
			utilisateur.setPrenom(request.getParameter("prenom"));
			utilisateur.setEmail(request.getParameter("email"));
			utilisateur.setTelephone(request.getParameter("telephone"));
			utilisateur.setRue(request.getParameter("rue"));
			utilisateur.setCode_postal(request.getParameter("code_postal"));
			utilisateur.setVille(request.getParameter("ville"));
			
			//Validation du mot de passe 
			String mdp1 = request.getParameter("mot_de_passe");
			String mdp2 = request.getParameter("confirmation");
			//Si mots de passe différents, erreur remontée
			if(!mdp1.equals(mdp2)) {
				listeCodesErreur.add(CodesResultatServlets.MOTS_DE_PASSE_DIFFERENTS);
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				if(request.getServletPath().equals("/creer")) {
					RequestDispatcher rd = request.getRequestDispatcher("/creer");
					rd.forward(request, response);
				}else {
					RequestDispatcher rd = request.getRequestDispatcher("/modifier");
					rd.forward(request, response);
				}
				
			//Si mot de passe validé 	
			}else {
				utilisateur.setMot_de_passe(request.getParameter("mot_de_passe"));
				UtilisateurManager manager = new UtilisateurManager();
				if(request.getServletPath().equals("/creer"))
				{
					try {
						manager.ajouterUtilisateur(utilisateur);
					} catch (BusinessException e) {
						for (int err : e.getListeCodesErreur()) {
							listeCodesErreur.add(err);
						}
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						RequestDispatcher rd = request.getRequestDispatcher("/creer");
						rd.forward(request, response);
					}
				}else if (request.getServletPath().equals("/modifier")){
					try {
						manager.modifierUtilisateur(utilisateur);
					} catch (BusinessException e) {
						for (int err : e.getListeCodesErreur()) {
							listeCodesErreur.add(err);
						}
						request.setAttribute("listeCodesErreur", listeCodesErreur);
						RequestDispatcher rd = request.getRequestDispatcher("/modifier");
						rd.forward(request, response);
					}
				}
				session.setAttribute("utilisateur", utilisateur);
				response.sendRedirect(request.getContextPath()+"/ListeEncheres");
			}
		}
	}

}
