package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import fr.eni.javaee.encheres.bll.ArticleVenduManager;
import fr.eni.javaee.encheres.bll.CategorieManager;
import fr.eni.javaee.encheres.bll.EnchereManager;
import fr.eni.javaee.encheres.bll.RetraitManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Categorie;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.messages.LecteurMessage;


/**
 * Servlet implementation class ServletArticle
 */

@WebServlet("/vente")

public class ServletVente extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		Retrait retrait = null;
		
		try {
			
			int no_article=0;
			no_article = lireParametreInt(request, "no_article", listeCodesErreur);
				
			if(no_article>0)
			{
				// charge l'article
				ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
				ArticleVendu articleVendu = articleVenduManager.getArticleVendu(no_article);
				if(articleVendu==null) {
					listeCodesErreur.add(CodesResultatServlets.VENTE_INCONNUE);
				}else {
					request.setAttribute("article", articleVendu);
					
					// charge l'adresse de retrait
					RetraitManager retraitManager = RetraitManager.getInstance();
					retrait = retraitManager.getRetrait(articleVendu.getNo_article());
					
					//charge la liste des encheres
					EnchereManager enchereManager = EnchereManager.getInstance();
					List<Enchere> listeEnchere = enchereManager.getListeEncheres(no_article);
					request.setAttribute("listeEnchere", listeEnchere);
				}
				
				request.setAttribute("isModifiable", isModifiable(articleVendu));
				request.setAttribute("isAnnulable", isAnnulable(articleVendu));
				
			}else
			{	
				// récupere l'adresse de retrait dans la fiche utilisateur du vendeur
				Utilisateur utilisateur = null;
				HttpSession session=request.getSession();
				if(session.getAttribute("utilisateur")!=null) {
					utilisateur = (Utilisateur)session.getAttribute("utilisateur");
					retrait = new Retrait();
					retrait.setRue(utilisateur.getRue());
					retrait.setCode_postal(utilisateur.getCode_postal());
					retrait.setVille(utilisateur.getVille());
				}
				
				// passe les attributs à la page
				request.setAttribute("isModifiable", Boolean.TRUE);
				request.setAttribute("isAnnulable", Boolean.FALSE);
			}
			
			request.setAttribute("retrait", retrait);
			request.setAttribute("dateDuJour", dateDuJour());
			request.setAttribute("categories", listeCategories(listeCodesErreur));
			
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		// transmet la liste des erreurs
		request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));

		// affiche la page
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp");			
		rd.forward(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		List<Integer> listeCodesErreur = new ArrayList<>();
		
		ArticleVendu articleVendu = null;
		Retrait retrait = null;
		ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
		RetraitManager retraitManager = RetraitManager.getInstance();
		
		Boolean modifiable = Boolean.FALSE;
		Boolean annulable = Boolean.FALSE;
		
		try {
			
			if(lireParametreInt(request, "supprimer", listeCodesErreur)==0) {

				// Récupère les valeurs du formulaire
				articleVendu = recupererSaisieArticle(request, response, listeCodesErreur);
				
				retrait = recupererSaisieRetrait(request, listeCodesErreur);
				retrait.setArticle(articleVendu);
				
				modifiable = lireParametreBoolean(request, "isModifiable", listeCodesErreur);
				annulable = lireParametreBoolean(request, "isAnnulable", listeCodesErreur);
				
				// controle et enregistre l'article vendu et l'adresse de retrait
				if (articleVendu.getNo_article()>0) {
					articleVenduManager.updateArticleVendu(articleVendu);
					retraitManager.updateRetrait(retrait);
				}else
				{
					articleVenduManager.createArticleVendu(articleVendu);
					retrait.getArticle().setNo_article(articleVendu.getNo_article());
					retraitManager.createRetrait(retrait);
				}
			}else
			{
				// supprime la vente
				int no_article = lireParametreInt(request, "no_article", listeCodesErreur);
				if(no_article>0) {
					articleVenduManager.deleteArticleVendu(no_article);
				}
			}
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		if(listeCodesErreur.size()>0)
		{
			// recharge la page en affichant les erreurs
			request.setAttribute("dateMin", dateDuJour());
			request.setAttribute("categories", listeCategories(listeCodesErreur));
			request.setAttribute("article", articleVendu);
			request.setAttribute("retrait", retrait);
			request.setAttribute("isModifiable", modifiable);
			request.setAttribute("isAnnulable", annulable);
			request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));
			
			RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/vente.jsp");			
			rd.forward(request, response);
			
		}else {
			// redirige vers la liste
			response.sendRedirect(request.getContextPath() + "/ListeEncheres");
		}
		
	}
	
	private int lireParametreInt(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type INT
		int valeur=0;
		try
		{
			if(request.getParameter(parametre)!=null && !request.getParameter(parametre).isEmpty())
			{
				valeur = Integer.parseInt(request.getParameter(parametre));
			}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private Boolean lireParametreBoolean(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type Boolean
		Boolean valeur=Boolean.FALSE;
		try
		{
			if(request.getParameter(parametre)!=null && !request.getParameter(parametre).isEmpty())
			{
				valeur = Boolean.parseBoolean(request.getParameter(parametre));
			}
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private String lireParametreString(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type STRING
		String valeur = request.getParameter(parametre);
		if(valeur==null)
		{
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private LocalDate lireParametreLocalDate(HttpServletRequest request, String parametre, List<Integer> listeCodesErreur) {
		// Renvoie la valeur du parametre de type LocalDate
		LocalDate valeur=null;
		try
		{
			String string = request.getParameter(parametre);
			if(string!=null && !string.isEmpty())
			{
				DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				valeur = LocalDate.parse(string, dtf);
			}
		}
		catch(DateTimeParseException e)
		{
			e.printStackTrace();
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_VENTE);
		}
		return valeur;
	}
	
	private ArticleVendu recupererSaisieArticle(HttpServletRequest request, HttpServletResponse response, List<Integer> listeCodesErreur) throws ServletException, IOException {
		
		//recupere les parametres du formulaire
		int no_article = lireParametreInt(request, "no_article", listeCodesErreur);

		Utilisateur vendeur=null;
		
		String no_utilisateur = lireParametreString(request, "no_utilisateur", listeCodesErreur);


		if(no_utilisateur==null || no_utilisateur.isEmpty()) {
			// Le vendeur est l'utilisateur connecté
			HttpSession session=request.getSession();
			if(session.getAttribute("utilisateur")!=null) {
				vendeur = (Utilisateur)session.getAttribute("utilisateur");
			}else
			{
				// demande à l'utilisateur de se connecter
				RequestDispatcher rd = request.getRequestDispatcher("/connection");			
				rd.forward(request, response);
			}
			
		}else
		{
			vendeur = new Utilisateur();
			vendeur.setNo_utilisateur(lireParametreInt(request, "no_utilisateur", listeCodesErreur));
		}
		
		String nom = lireParametreString(request, "nom", listeCodesErreur);
		String description = lireParametreString(request, "description", listeCodesErreur);
		int prixInitial = lireParametreInt(request, "prixInitial", listeCodesErreur);
		LocalDate dateDebut = lireParametreLocalDate(request, "dateDebut", listeCodesErreur);
		LocalDate dateFin = lireParametreLocalDate(request, "dateFin", listeCodesErreur);

		int no_categorie = lireParametreInt(request, "categorie", listeCodesErreur);
		Categorie categorie = new Categorie();
		categorie.setNo_categorie(no_categorie);
		
		//construit l'article
		ArticleVendu articleVendu = new ArticleVendu();	
		
		articleVendu.setNo_article(no_article);
		articleVendu.setVendeur(vendeur);	
		articleVendu.setNom_article(nom);
		articleVendu.setDescription(description);
		articleVendu.setCategorie(categorie);
		articleVendu.setPrix_initial(prixInitial);
		articleVendu.setDate_debut_encheres(dateDebut);
		articleVendu.setDate_fin_encheres(dateFin);
		
		return articleVendu;
	}
	
	private Retrait recupererSaisieRetrait(HttpServletRequest request, List<Integer> listeCodesErreur) {
		
		//recupere les parametres du formulaire
		String rue = lireParametreString(request, "rue", listeCodesErreur);
		String code_postal = lireParametreString(request, "code_postal", listeCodesErreur);
		String ville = lireParametreString(request, "ville", listeCodesErreur);
		
		//construit le retrait
		Retrait retrait = new Retrait();	
		retrait.setRue(rue);
		retrait.setCode_postal(code_postal);
		retrait.setVille(ville);
		
		return retrait;
	}
	
	private List<String> ListeLibellesErreurs (List<Integer> listeCodesErreur){
		List<String> liste=new ArrayList<>();
		if(listeCodesErreur.size()>0)
			for(int code:listeCodesErreur) {
				liste.add(LecteurMessage.getMessageErreur(code));
			}
		return liste;
	}
	
	private String dateDuJour() {
		Date date = new Date();  
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");  
		return dateFormat.format(date);
	}	
	
	private List<Categorie> listeCategories(List<Integer> listeCodesErreur) {
		List<Categorie> categories=null;
		try {
			CategorieManager categorieManager = CategorieManager.getInstance();
			categories = categorieManager.getListeCategories();
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		return categories;
	}
	
	private Boolean isModifiable(ArticleVendu articleVendu) {
		Boolean modifiable = Boolean.FALSE;
		if(articleVendu!=null) {
			LocalDate today = LocalDate.now(); 
			if( today.isBefore(articleVendu.getDate_debut_encheres()) ) 
			{
				modifiable = Boolean.TRUE;
			}			
		}
		return modifiable;
	}
	
	private Boolean isAnnulable(ArticleVendu articleVendu) {
		Boolean annulable = Boolean.FALSE;
		if(articleVendu!=null) {
			if(articleVendu.getNo_article()>0 && isModifiable(articleVendu) ) 
			{
				annulable = Boolean.TRUE;
			}			
		}
		return annulable;
	}
	
}
