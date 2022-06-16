package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletConnexion
 */
@WebServlet("/Connexion")
public class ServletConnexion extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String identifiant = null;
		String mot_de_passe = null;
		//Récupération des cookies éventuels
		Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("identifiant")){
					identifiant = cookie.getValue();
					session.setAttribute("identifiant", identifiant);
				}
				if (cookie.getName().equals("password")) {
					mot_de_passe = cookie.getValue();
					session.setAttribute("password", mot_de_passe);
				}
			}
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
			rd.forward(request, response);
			
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
			rd.forward(request, response);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//gestion de la connexion 
		request.setCharacterEncoding("UTF-8");
		List<Integer> listeCodesErreur = new ArrayList<>();
		//Récupération des informations de connexion
		HttpSession session = request.getSession();
		Utilisateur utilisateur = new Utilisateur();
		String identifiant = request.getParameter("identifiant");
		String mot_de_passe = request.getParameter("mot_de_passe");
		//Récupération de l'utilisateur, soit par pseudo, soit par email.
		UtilisateurManager manager = new UtilisateurManager();
		if (identifiant.contains("@")){
			utilisateur.setEmail(identifiant);
			try {
				utilisateur = manager.getUtilisateurByEmail(identifiant);
			} catch (BusinessException e) {
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
				rd.forward(request, response);
			}
		}
		else{
			utilisateur.setPseudo(identifiant);
			try {
				
				utilisateur = manager.getUtilisateurByPseudo(identifiant);
			} catch (BusinessException e) {
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
				rd.forward(request, response);
			}
		}
		//Vérification de la validité du mot de passe
		if(!mot_de_passe.equals(utilisateur.getMot_de_passe())){
			listeCodesErreur.add(CodesResultatServlets.MOT_DE_PASSE_INCORRECT);
			request.setAttribute("listeCodesErreur", listeCodesErreur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/connexion.jsp");
			rd.forward(request, response);
		}else {
			//Création de cookies si "se souvenir" est coché
			if(request.getParameter("SeSouvenir") == "OK") {
				if(identifiant.contains("@")) {
					Cookie identifiantEmail = new Cookie("identifiant", utilisateur.getEmail());
					identifiantEmail.setMaxAge(100);
					response.addCookie(identifiantEmail);
				}else {
					Cookie identifiantPseudo = new Cookie("identifiant", utilisateur.getPseudo());
					identifiantPseudo.setMaxAge(100);
					response.addCookie(identifiantPseudo);
				}
				Cookie password = new Cookie("password", mot_de_passe);
				password.setMaxAge(100);
				response.addCookie(password);
			}
			session.setAttribute("utilisateur", utilisateur);
			//Deconnexion au bout de 5 minutes d'inactivité
			session.setMaxInactiveInterval(300);
			
			RequestDispatcher rd = request.getRequestDispatcher("/ListeEncheres");
			rd.forward(request, response);
		}
	}

}
