package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.UtilisateurManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;

import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;



/**
 * Servlet implementation class ServletListeEncheres
 */
@WebServlet("/ListeEncheres")
public class ServletListeEncheres extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		Cookie[] cookies = request.getCookies();
		
		// Récupération de la session.
		HttpSession session = request.getSession();
		
		try {				
			if (cookies != null && session.getAttribute("liste") != null && request.getParameter("reset") == null) {	
				
				// Récupération de la liste résultant d'une recherche
				List<ArticleVendu> listeAvecParametres = new ArrayList<ArticleVendu>();	
				listeAvecParametres = (List<ArticleVendu>) session.getAttribute("liste");
				request.setAttribute("liste", listeAvecParametres);
				
				// Récupération de cookies à afficher dans la jsp
				for (Cookie cookie:cookies) {
					if(cookie.getName().equals("choix")) {
						request.setAttribute("choixUtilisateur", cookie.getValue());
					}	
					if(cookie.getName().equals("choixAchat")) {
						request.setAttribute("choixAchat", cookie.getValue());
					}	
					if(cookie.getName().equals("choixVente")) {
						request.setAttribute("choixVente", cookie.getValue());
					}	
					if(cookie.getName().equals("nomArticleSaisi")) {
						request.setAttribute("nomArticleSaisi", cookie.getValue());
					}		
					if(cookie.getName().equals("categorie")) {
						int noCategorie = Integer.parseInt(cookie.getValue());
						request.setAttribute("choixUtilisateur", cookie.getValue());
						//Récupération catégorie choisie
						if (noCategorie > -1) {
							Categorie categorieSelectionnee = selectionnerUneCategorie(noCategorie);
							request.setAttribute("categorieSelectionnee", categorieSelectionnee);
						}
					}	
				}
							
				// Récupération de la liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories();
				request.setAttribute("listeCategories", listeCategories);	
				
			} else {
				// Récupération de la liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories();
				request.setAttribute("listeCategories", listeCategories);
				
				// Récupération de la liste de toutes les enchères en cours
				List<ArticleVendu> listeDeToutesLesEncheresEnCours = new ArrayList<ArticleVendu>();		
				listeDeToutesLesEncheresEnCours = selectionnerToutesLesEncheresEnCours(-1, "");
				request.setAttribute("liste", listeDeToutesLesEncheresEnCours);
			}
		} catch (BusinessException e) {
			// Récupération de la liste des codes d'erreurs
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
			request.setAttribute("listeCodesErreur", listeCodesErreur);
		}
			
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// Initialistion de la liste de codes d'erreurs
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		// Encodage des caractères.
		request.setCharacterEncoding("UTF-8");
		
		// Récupération de la session.
		HttpSession session = request.getSession();
		
		// Initialisation des paramètres.
		String nomArticle = "";
		int noCategorie = -1;
		String choixUtilisateur = null;
		String choixAchat = null;
		String choixVente = null;
		List<ArticleVendu> listeAvecParamètres = new ArrayList<ArticleVendu>();
		
		// Recheche avec paramètres en mode connecté avec la présence d'un utilisateur en session.
		if(session.getAttribute("utilisateur")!=null) {
			try {
				// Récupération de l'identifiant de l'utilisateur
				Utilisateur utilisateur = (Utilisateur) session.getAttribute("utilisateur");
				int noUtilisateur = utilisateur.getNo_utilisateur();
				
				// Récuparation du choix de l'utilisateur
				choixUtilisateur = request.getParameter("choix");
				request.setAttribute("choixUtilisateur", choixUtilisateur);
				Cookie choixUtilisateurCookie = new Cookie("choix",choixUtilisateur);
				choixUtilisateurCookie.setMaxAge(Integer.MAX_VALUE);
				response.addCookie(choixUtilisateurCookie);
				
				choixAchat = request.getParameter("choixAchat");
				request.setAttribute("choixAchat", choixAchat);
				Cookie choixAchatCookie = new Cookie("choixAchat",choixAchat);
				choixAchatCookie.setMaxAge(Integer.MAX_VALUE);
				response.addCookie(choixAchatCookie);
				
				choixVente = request.getParameter("choixVente");
				request.setAttribute("choixVente", choixVente);
				Cookie choixVenteCookie = new Cookie("choixVente",choixVente);
				choixVenteCookie.setMaxAge(Integer.MAX_VALUE);
				response.addCookie(choixVenteCookie);
				
				//Récupération du nom de l'article à rechercher
				nomArticle = request.getParameter("article");
				request.setAttribute("nomArticleSaisi", nomArticle);
				Cookie nomArticleCookie = new Cookie("nomArticleSaisi",nomArticle);
				nomArticleCookie.setMaxAge(Integer.MAX_VALUE);
				response.addCookie(nomArticleCookie);
				
				if (request.getParameter("categorie") != null) {
					Cookie categorieCookie = new Cookie("categorie",request.getParameter("categorie"));
					categorieCookie.setMaxAge(Integer.MAX_VALUE);
					response.addCookie(categorieCookie);
					noCategorie = Integer.parseInt(request.getParameter("categorie"));		
					// Récupération catégorie choisie
					if (noCategorie > -1) {
						Categorie categorieSelectionnee = selectionnerUneCategorie(noCategorie);
						request.setAttribute("categorieSelectionnee", categorieSelectionnee);
					}				
				}
				
				// Récupération liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories();
				request.setAttribute("listeCategories", listeCategories);
				
				// Si le choix concerne les achats
				if (choixUtilisateur != null && choixUtilisateur.equalsIgnoreCase("achats")) {
					switch (choixAchat) {
					case "encheresOuvertes":
						listeAvecParamètres = selectionnerLesEncheresOuvertesAvecUnUtilisateurConnecte(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					case "encheresEnCours":
						listeAvecParamètres = selectionnerLesEncheresEnCoursDeLUtilisateurConnecte(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					case "encheresRemportees":
						listeAvecParamètres = selectionnerLesEncheresRemporteesParLUtilisateur(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					}
				}
				// Si le choix concerne les ventes
				if (choixUtilisateur != null && choixUtilisateur.equalsIgnoreCase("ventes")) {
					switch (choixVente) {
					case "ventesEncours":
						listeAvecParamètres = selectionnerLesVentesEnCoursDeLUtilisateur(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					case "ventesNonDebutees":
						listeAvecParamètres = selectionnerLesVentesNonDebuteesDeLUtilisateur(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					case "ventesTerminees":
						listeAvecParamètres = selectionnerLesVentesTermineesDeLUtilisateur(noUtilisateur, noCategorie, nomArticle);
						request.setAttribute("liste", listeAvecParamètres);
						session.setAttribute("liste", listeAvecParamètres);
						break;
					}
				}
				
				if (choixUtilisateur == null) {
					listeAvecParamètres = selectionnerToutesLesEncheresEnCours(noCategorie, nomArticle);
					request.setAttribute("liste", listeAvecParamètres);
					session.setAttribute("liste", listeAvecParamètres);
				}
			} catch (BusinessException e) {
				// Récupération de la liste des codes d'erreurs
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
			}
			
		} else {
		// Recherche avec paramètres sans utilisateur connecté.
			try {
				//Récupération du nom de l'article à rechercher
				nomArticle = request.getParameter("article");
				request.setAttribute("nomArticleSaisi", nomArticle);
				
				
				Cookie categorieCookie = new Cookie("categorie",request.getParameter("categorie"));
				categorieCookie.setMaxAge(Integer.MAX_VALUE);
				response.addCookie(categorieCookie);
				noCategorie = Integer.parseInt(request.getParameter("categorie"));
				//Récupération catégorie choisie
				if (noCategorie > -1) {
					Categorie categorieSelectionnee = selectionnerUneCategorie(noCategorie);
					request.setAttribute("categorieSelectionnee", categorieSelectionnee);
				}
				
				//Récupération liste des catégories
				List<Categorie> listeCategories = new ArrayList<Categorie>();
				listeCategories = selectionnerToutesLesCategories();
				request.setAttribute("listeCategories", listeCategories);
				
				//Récupération des listes
				listeAvecParamètres = selectionnerToutesLesEncheresEnCours(noCategorie, nomArticle);
				request.setAttribute("liste", listeAvecParamètres);
				session.setAttribute("liste", listeAvecParamètres);
			} catch(NumberFormatException e) {
				listeCodesErreur.add(CodesResultatServlets.FORMAT_NUMERO_CATEGORIE_ERREUR);
				request.setAttribute("listeCodesErreur", listeCodesErreur);
			} catch (BusinessException e) {
				// Récupération de la liste des codes d'erreurs
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
				e.printStackTrace();
			}	
		}
		
		RequestDispatcher rd = this.getServletContext().getRequestDispatcher("/WEB-INF/jsp/listeEncheres.jsp");
		rd.forward(request, response);
	}
	
	
	//--------------------------------------------------------------------------------------------------------------------------------------------------//
	// Méthodes utilisant les managers
	//--------------------------------------------------------------------------------------------------------------------------------------------------//

	
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
	 * @param int noCategorie
	 * @param  String nomArticle
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerToutesLesEncheresEnCours(int noCategorie,String nomArticle) throws BusinessException{
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeEncheresEncoursToutes(noCategorie,nomArticle);
		return listeArticlesVendus;
	}
	
	/**
	 * @param int no_utilisateur
	 * @return Utilisateur utilisateur
	 * @throws BusinessException
	 */
	public Utilisateur selectionnerUnUtilisateur(int no_utilisateur) throws BusinessException {
		Utilisateur utilisateur = null;
		UtilisateurManager um = UtilisateurManager.getInstance();
		utilisateur = um.getUtilisateur(no_utilisateur);
		return utilisateur;
	}

	/**
	 * @param int no_utilisateur
	 * @param int no_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesEncheresOuvertesAvecUnUtilisateurConnecte(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeEncheresEncoursAutresVendeurs(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}
	
	/**
	 * @param int no_utilisateur
	 * @param int no_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesEncheresEnCoursDeLUtilisateurConnecte(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeEncheresEncoursUtilisateur(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}
	
	/**
	 * @param int no_utilisateur
	 * @param int o_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesEncheresRemporteesParLUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeEncheresRemporteesUtilisateur(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}
	
	/**
	 * @param int no_utilisateur
	 * @param int no_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesVentesEnCoursDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeVentesEncoursUtilisateur(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}
	
	/**
	 * @param int no_utilisateur
	 * @param int no_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesVentesNonDebuteesDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeVentesAVenirsUtilisateur(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}
	
	
	/**
	 * @param int no_utilisateur
	 * @param int no_categorie
	 * @param String nom_article
	 * @return List<ArticleVendu> listeArticlesVendus
	 * @throws BusinessException
	 */
	public List<ArticleVendu> selectionnerLesVentesTermineesDeLUtilisateur(int no_utilisateur, int no_categorie, String nom_article) throws BusinessException {
		List<ArticleVendu> listeArticlesVendus = new ArrayList<ArticleVendu>();
		ArticleVenduManager avm = ArticleVenduManager.getInstance();
		listeArticlesVendus = avm.getListeVentesTermineesUtilisateur(no_utilisateur, no_categorie, nom_article);
		return listeArticlesVendus;
	}

}
