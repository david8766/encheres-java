<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
 <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="UTF-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	    <meta name="description" content="">
	    <meta name="author" content="">

		<link rel="stylesheet" href="vendor/css/vente.css">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
		
		<title>Vente</title>
		
	</head>

	<body>
	
		<!-- Entete de page -->
		<header>
			<!-- Navigation -->
			<%@include file="navigation.jsp" %>
		</header>
		
		<!--  Corps de la page  -->
		
		<main class="container-fluid">
			
			<div class="row text-center">
				<div class="col">
				
					<!-- titre de la page -->
					<c:if test="${empty article}">	
						<h1 class="my-5">Nouvelle vente</h1>
					</c:if>
					<c:if test="${!empty article}">	
						<h1 class="my-5">Ma vente</h1>
					</c:if>
		
					<!-- Messages d'erreur -->
					<c:if test="${!empty ListeLibellesErreurs}">
						<div class="alert alert-danger" role="alert">
							<c:forEach var="e" items="${ListeLibellesErreurs}">
								${e}<br>
							</c:forEach>
						</div>
					</c:if>
					
					<!-- Formulaire -->
					<div class="text-left">
						<form name="formSaisie" method="post" action="${pageContext.request.contextPath}/vente">
				
							<% 
							String disabled="";
							if(!(Boolean)request.getAttribute("isModifiable")) {
								disabled="disabled";
							}
							%>
							
							<div class="row">
								
								<!-- image et enchérisseurs-->
								<div class="col-md-4">
								
									<h3>Photo</h3>
									<div class="card">
										<!-- TODO -->
										<div>
											<img class="card-img-left img-fluid" src="https://via.placeholder.com/250x150" alt="image">
										</div>
										
										<c:if test="${isModifiable}">	
											<button type="button" class="btn btn-secondary btn-xs">Upload</button>
										</c:if>
									</div>
																	
									<h3>Enchères effectuées</h3>
									<div class="card">
										<c:if test="${empty listeEnchere}">
											Aucune enchère pour le moment.
										</c:if>
										
										<c:if test="${!empty listeEnchere}">
											<table class="table">
											  <thead>
											    <tr>
											      <th scope="col">Date</th>
											      <th scope="col">Enchérisseur</th>
											      <th scope="col">Offre</th>
											    </tr>
											  </thead>
											  <tbody>
												<c:forEach var="e" items="${listeEnchere}">
											    <tr>
											      <th scope="col">${e.date_enchere}</th>
											      <th scope="col">${e.encherisseur.pseudo}</th>
											      <th scope="col">${e.montant_enchere}</th>
											    </tr>						
												</c:forEach>
											  </tbody>
											</table>
										</c:if>
									</div>
									
								</div>
			
								<!-- inputs -->
								<div class="col-md-8">
														
									<input type="hidden" id="no_article" name="no_article" value="${article.no_article}">
									<input type="hidden" id="no_utilisateur" name="no_utilisateur" value="${article.vendeur.no_utilisateur}">
									<input type="hidden" id="supprimer" name="supprimer" value="0">
									<input type="hidden" id="isModifiable" name="isModifiable" value="${isModifiable}">
									
									<div>
										<h3>Article</h3>
										<div class="card">
											<div>
												<label for="nom">Nom</label>
												<input type="text" id="nom" name="nom" value="${article.nom_article}" placeholder="Nom de l'article" required <%=disabled %>>
											</div>
										
											<div>
												<label for="description">Description</label>
												<textarea id="description" name="description" required  <%=disabled %>>${article.description}</textarea>
											</div>		
										
											<div>
												<label for="categorie">Catégorie</label>
												<select id="categorie" name="categorie"  <%=disabled %>>
													<c:if test="${!empty categories}">
														<c:forEach var="c" items="${categories}">
															<c:if test="${c.no_categorie == article.categorie.no_categorie}">
													  			<option value="${c.no_categorie}" selected>${c.libelle}</option>
													  		</c:if>
													  		<c:if test="${c.no_categorie != article.categorie.no_categorie}">
													  			<option value="${c.no_categorie}">${c.libelle}</option>
													  		</c:if>
														</c:forEach>
													</c:if>
												</select>
											</div>
								
											<div>
												<label for="prixInitial">Mise à prix</label>
												<input type="number" id="prixInitial" name="prixInitial" value="${article.prix_initial}" min="0" max="10000" step="5" required <%=disabled %>>
												<label>Points</label>
											</div>
											
											<c:if test="${!isModifiable}">
												<div>
													<label for="prixVente">Prix de vente</label>
													<input type="number" id="prixVente" name="prixVente" value="${article.prix_vente}" min="0" max="10000" step="5" disabled>
													<label>Points</label>
												</div>
											</c:if>
														
											<div>
												<label for="dateDebut">Début de l'enchère</label>
												<input type="date" id="dateDebut" name="dateDebut" value="${article.date_debut_encheres}" min="${dateDuJour}" <%=disabled %>> 
											</div>
											
											<div>
												<label for="dateFin">Fin de l'enchère</label>
												<input type="date" id="dateFin" name="dateFin" value="${article.date_fin_encheres}" min="${dateDuJour}" <%=disabled %>> 
											</div>		
										</div>		
									</div>		
									
									<div>
										<h3>Retrait</h3>
										<div class="card">
											<div>
												<label for="rue">Rue</label>
												<input type="text" id="rue" name="rue" value="${retrait.rue}" placeholder="Rue" required <%=disabled %>>
											</div>
											<div>
												<label for="code_postal">Code postal</label>
												<input type="text" id="code_postal" name="code_postal" value="${retrait.code_postal}" placeholder="Code postal" required <%=disabled %> >
											</div>
											<div>
												<label for="ville">Ville</label>
												<input type="text" id="ville" name="ville" value="${retrait.ville}" placeholder="Ville" required <%=disabled %> >
											</div>
										</div>
									</div>
																																	
								</div>
									
							</div>
							
							<!-- bouton -->
							<div class="row text-center">
								<div class="col">
									<c:if test="${isModifiable}">	
										<button type="submit" class="btn btn-primary btn-md">Enregistrer</button>
									</c:if>
									
									<a href="${pageContext.request.contextPath}/ListeEncheres"><button type="button" class="btn btn-primary btn-md">Retour à la liste</button></a>
									
									<c:if test ="${isAnnulable}">
										<button type="button" class="btn btn-danger btn-md" onclick="ConfirmerSuppression()">Annuler la vente</button>
									</c:if>
								</div>
							</div>
								
						</form>
					</div>
		
				</div>
			</div>			
		</main>
		
		<!-- Pied de page -->
	    <footer>
	    	<p>
				<%@include file="pied_de_page.jsp" %>
			</p>
	    </footer>

	    <script src="vendor/jquery/jquery.min.js"></script>
	    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
		<script>
			function ConfirmerSuppression(){
				if (window.confirm("Voulez-vous supprimer définitivement la vente ?")) {
					document.getElementById('supprimer').value = "1";
					document.formSaisie.submit();
				}
			}
		</script>
		
	</body>
	
</html>