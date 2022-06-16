package fr.eni.javaee.encheres.filtres;

import java.io.IOException;
import javax.servlet.DispatcherType;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet Filter implementation class FiltreConnexion
 */
@WebFilter(dispatcherTypes = {
		DispatcherType.REQUEST, 
}
, urlPatterns = { 
		"/modifier",
		"/supprimer",
		"/mon_profil",
		"/profil",
		"/vente",
		"/enchere",
		"/Administration",
		"/AdministrationDesCategories",
		"/SupprimerCategorie",
		"/AdministrationCreerUtilisateur",
		"/AdministrationUtilisateurSupprimer"
})

public class FiltreConnexion implements Filter {

    /**
     * Default constructor. 
     */
    public FiltreConnexion() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if(httpRequest.getSession().getAttribute("utilisateur") != null){
			chain.doFilter(request, response);
		}else {
			RequestDispatcher rd = request.getRequestDispatcher("/Connexion");
			rd.forward(httpRequest, httpResponse);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
