package fr.eni.javaee.encheres.servlets;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
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
import fr.eni.javaee.encheres.bll.EnchereManager;
import fr.eni.javaee.encheres.bll.RetraitManager;
import fr.eni.javaee.encheres.bo.ArticleVendu;
import fr.eni.javaee.encheres.bo.Enchere;
import fr.eni.javaee.encheres.bo.Retrait;
import fr.eni.javaee.encheres.bo.Utilisateur;
import fr.eni.javaee.encheres.messages.BusinessException;
import fr.eni.javaee.encheres.messages.LecteurMessage;


/**
 * Servlet implementation class ServletArticle
 */
@WebServlet("/enchere")

public class ServletEnchere extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		List<Integer> listeCodesErreur = new ArrayList<>();

		AfficherPage(request, response, listeCodesErreur);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		request.setCharacterEncoding("UTF-8");
		
		List<Integer> listeCodesErreur = new ArrayList<>();

		// Récupère les valeurs du formulaire
		int no_article = 0;
		int montant_enchere = 0;
		int retraitEffectue = 0;
		
		no_article = lireParametreInt(request, "no_article", listeCodesErreur);
		montant_enchere = lireParametreInt(request, "prix_vente", listeCodesErreur);
		retraitEffectue = lireParametreInt(request, "retraitEffectue", listeCodesErreur);

		// soumet l'enchere
		if(no_article>0) {
			
			try {
				HttpSession session = request.getSession();
				Utilisateur encherisseur = (Utilisateur)session.getAttribute("utilisateur");
				
				ArticleVenduManager articleVenduManager = ArticleVenduManager.getInstance();
				ArticleVendu articleVendu = new ArticleVendu();
				articleVendu = articleVenduManager.getArticleVendu(no_article);
				
				if(articleVendu!=null) {
					
					if(retraitEffectue==0) {
						
						// enrtegistre l'enchere
						LocalDate date_enchere = LocalDate.now(); 
						
						Enchere enchere = new Enchere();
						enchere.setArticle(articleVendu);
						enchere.setEncherisseur(encherisseur);
						enchere.setDate_enchere(date_enchere);
						enchere.setMontant_enchere(montant_enchere);
						
						EnchereManager enchereManager = EnchereManager.getInstance();
						enchereManager.createEnchere(enchere);
						
						request.setAttribute("confirmation", "Votre enchère a été prise en compte.");
						
					}else {
						
						// enrtegistre le retrait effectué
						RetraitManager retraitManager = RetraitManager.getInstance();
						Retrait retrait = retraitManager.getRetrait(no_article);
						if (retrait==null) {
							retrait = new Retrait();
						}
						retrait.setRetire(Boolean.TRUE);
						
						retraitManager.updateRetrait(retrait);
						
						request.setAttribute("confirmation", "Le retrait a été pris en compte.");
						
					}
					
				}else {
					
				}
				
			} catch (BusinessException e) {
				for (int err : e.getListeCodesErreur()) {
					listeCodesErreur.add(err);
				}
			}
		}
		
		// gere les erreurs métiers
		if(listeCodesErreur.size()>0)
		{
			// recharge la page en affichant les erreurs
			request.setAttribute("no_article", no_article);
			AfficherPage(request, response, listeCodesErreur);
		}else {
			// recharge la page en confirmant l'enchere
			request.setAttribute("no_article", no_article);
			AfficherPage(request, response, listeCodesErreur);
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
			listeCodesErreur.add(CodesResultatServlets.LECTURE_PARAMETRE_ENCHERE);
		}
		return valeur;
	}
	
	
	private List<String> ListeLibellesErreurs (List<Integer> listeCodesErreur){
		List<String> liste=new ArrayList<>();
		if(listeCodesErreur.size()>0)
			for(int code:listeCodesErreur) {
				liste.add(LecteurMessage.getMessageErreur(code));
			}
		return liste;
	}
	
	private Boolean isModifiable(HttpServletRequest request, ArticleVendu articleVendu, List<Integer> listeCodesErreur) {
		
		Boolean modifiable = Boolean.TRUE;
		
		// controle dates
		if(articleVendu!=null) {
			LocalDate today = LocalDate.now(); 
			if( today.isBefore(articleVendu.getDate_debut_encheres())
				|| today.isAfter(articleVendu.getDate_fin_encheres())) 
			{
				modifiable = Boolean.FALSE;
			}			
		}else {
			modifiable = Boolean.FALSE;
		}
		
		// l'encherisseur ne peut pas encherir sur lui-meme : masque bouton
		if(modifiable) {
			Utilisateur encherisseur = null;
			Utilisateur utilisateur = null;
			Enchere enchere = null;
			EnchereManager enchereManager = EnchereManager.getInstance();

			try {
				enchere = enchereManager.getMeilleureEnchere(articleVendu.getNo_article());
				if (enchere != null) {
					encherisseur = enchere.getEncherisseur();
					HttpSession session = request.getSession();
					utilisateur = (Utilisateur)session.getAttribute("utilisateur");
					if(utilisateur!=null && encherisseur!=null){
						if(utilisateur.getNo_utilisateur()==encherisseur.getNo_utilisateur()) {
							modifiable = Boolean.FALSE;
						}
					}
				}
				
			} catch (BusinessException e) {
				e.printStackTrace();
				listeCodesErreur.add(CodesResultatServlets.ENCHERE_MODIFIABLE);
			}
		}
		
		return modifiable;

	}

	private void AfficherPage(HttpServletRequest request, HttpServletResponse response, List<Integer> listeCodesErreur) throws ServletException, IOException {
		
		List<String> listeInfos = new ArrayList<String>();
		
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
				}
				
				// vérifie si enchere possible
				Boolean modifiable = isModifiable(request, articleVendu, listeCodesErreur);
				
				// charge l'adresse de retrait
				Retrait retrait = null;
				RetraitManager retraitManager = RetraitManager.getInstance();
				retrait = retraitManager.getRetrait(articleVendu.getNo_article());
				
				// verifie si retrait effectué
				Date date = new Date();
				LocalDate today = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
				
				Boolean isRetraitEffectue = Boolean.TRUE;
				if(retrait !=null) {
					if(!retrait.getRetire() && articleVendu.getDate_fin_encheres().isBefore(today)) {
						isRetraitEffectue = Boolean.FALSE;
					}
				}
				
				// charge le meilleur enchéreur
				String encherisseur = null;
				Boolean soyerLePremier = Boolean.FALSE;
				Boolean enchereMenee = Boolean.FALSE;
				Boolean enchereRemportee = Boolean.FALSE;
				Utilisateur utilisateur = null;
				
				EnchereManager enchereManager = EnchereManager.getInstance();
				Enchere meilleureEnchere = enchereManager.getMeilleureEnchere(no_article);
				
				if(meilleureEnchere!=null) {
					
					Utilisateur meilleurEncherisseur = meilleureEnchere.getEncherisseur();
					
					if(meilleurEncherisseur!=null) {
						encherisseur = "par " + meilleurEncherisseur.getPseudo();
						
						HttpSession session = request.getSession();
						utilisateur = (Utilisateur)session.getAttribute("utilisateur");
						
						if(utilisateur.getNo_utilisateur() == meilleurEncherisseur.getNo_utilisateur()) {
							
							if(articleVendu.getDate_fin_encheres().isBefore(today)) {
								enchereRemportee = Boolean.TRUE;
							}else {
								enchereMenee = Boolean.TRUE;
							}
						}
					}else {
						soyerLePremier = Boolean.TRUE;
					}
					
				}
				
				// genere les messages d'information selon le contexte
				if(soyerLePremier) {
					listeInfos.add("Soyez le premier à enchérir !");
				}

				if(enchereMenee) {
					listeInfos.add("Vous menez l'enchère pour le moment...");
				}

				if(enchereRemportee) {
					listeInfos.add("FELICITATIONS !<br>Vous avez remporté l'enchère.");
				}

				if(enchereRemportee && !isRetraitEffectue) {
					listeInfos.add("Vous pouvez maintenant retiré votre article.");
				}
				
				// passe les attributs à la page
				request.setAttribute("article", articleVendu);
				request.setAttribute("retrait", retrait);
				request.setAttribute("encherisseur", encherisseur);
				request.setAttribute("listeInfos", listeInfos);
				request.setAttribute("isRetraitEffectue", isRetraitEffectue);
				request.setAttribute("isModifiable", modifiable);
				
			}else
			{	
				listeCodesErreur.add(CodesResultatServlets.VENTE_INCONNUE);
			}
			
		} catch (BusinessException e) {
			for (int err : e.getListeCodesErreur()) {
				listeCodesErreur.add(err);
			}
		}
		
		// transmet la liste des erreurs
		request.setAttribute("ListeLibellesErreurs",ListeLibellesErreurs(listeCodesErreur));

		// affiche la page 
		RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/jsp/enchere.jsp");			
		rd.forward(request, response);
	}
	
}
