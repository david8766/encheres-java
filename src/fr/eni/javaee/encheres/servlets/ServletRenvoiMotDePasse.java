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
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;

/**
 * Servlet implementation class ServletRenvoiMotDePasse
 */
@WebServlet("/renvoi")
public class ServletRenvoiMotDePasse extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/renvoi.jsp");
		rd.forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		String email = request.getParameter("email");
		String mdp = null;
		List<Utilisateur> utilisateurs = new ArrayList<Utilisateur>();
		UtilisateurManager manager = new UtilisateurManager();
		try {
			utilisateurs = manager.getListeUtilisateurs();
			for (Utilisateur utilisateur : utilisateurs) {
				String contact = utilisateur.getEmail();
				if(contact.equals(email)) {
					mdp = utilisateur.getMot_de_passe();
					
					//Procédé pour envoyer un mail -- voir javamail
					/* Create mailer.
			        String hostname = "smtp.example.com";
			        int port = 2525;
			        String username = "nobody";
			        String password = "idonttellyou";
			        Mailer mailer = new Mailer(hostname, port, username, password);

			        // Send mail.
			        String from = "john.doe@example.com";
			        String to = email;
			        String subject = "Demande de mot de passe";
			        String message = "Votre mot de passe pour ENI-Enchères est : " + mdp;
			        mailer.send(from, to, subject, message);
			        */
					request.setAttribute("email", email);
					RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/renvoi.jsp");
					rd.forward(request, response);
			    }
			}
			//Si l'adresse email n'est pas en base de données
			if (mdp == null) {
				listeCodesErreur.add(CodesResultatServlets.ADRESSE_EMAIL_INCONNUE);
				request.setAttribute("listeCodesErreur", listeCodesErreur);
				RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/renvoi.jsp");
				rd.forward(request, response);
			}
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
			e.printStackTrace();
			request.setAttribute("listeCodesErreur", listeCodesErreur);
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/renvoi.jsp");
			rd.forward(request, response);
		}
		
	}

}
