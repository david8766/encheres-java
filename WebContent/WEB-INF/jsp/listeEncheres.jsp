<%@ page import="fr.eni.javaee.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!DOCTYPE html>
<html>
	<head>
		<meta charset="utf-8">
	    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	    <meta name="viewport" content="width=device-width, initial-scale=1">
	    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
	    <meta name="description" content="">
	    <meta name="author" content="">
	    <link rel="icon" href="favicon.ico">
		<link rel="stylesheet" href="vendor/css/listeEncheres.css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
		<title>Listes des enchères</title>
	</head>
	<body>
	
		<!-- Entete de page -->
	<header>
		
		<!-- Navigation -->
		<%@include file="navigation.jsp" %>
	
	</header>
	
	<!--  Corps de la page  -->
	<main class="container-fluid">
		<!-- Messages d'alertes -->
		<div class="container">
			<c:if test="${!empty listeCodesErreur}">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="codeErreur" items="${listeCodesErreur}">
						${LecteurMessage.getMessageErreur(codeErreur)} 
					</c:forEach>
				</div>
			</c:if>
		</div>
		
		<!-- Lien vers l'administration -->
		<c:if test="${!empty sessionScope.utilisateur.administrateur and sessionScope.utilisateur.administrateur == true}">
			<div class="row">
				<a class="btn btn-danger font-weight-bold" href="<%=request.getContextPath()%>/Administration" role="button">Administration</a>			
			</div>
		</c:if>
		
		<!-- Titre de la page -->
		<div class="row text-center">
			<div class="col">
				<h1 class="my-5">Liste des enchères</h1> 
			</div>
		</div>
			
			<!--  Formulaire de recherche -->
			<form class="row mx-md-5" action="<%=request.getContextPath()%>/ListeEncheres" method="post">
				<div class="col-12 col-md-6 px-md-5">
					<c:if test="${!empty sessionScope.utilisateur}">
					<p>${ sessionScope.utilisateur.prenom } ${ sessionScope.utilisateur.nom } : vous êtes connecté!</p>
					</c:if>
					<c:if test="${empty sessionScope.utilisateur}">
					<p>Vous êtes déconnecté!</p> 
					<p>Vous pouvez vous identifier ou vous inscrire en cliquant sur le lien correspondant dans la barre de navigation</p>
					</c:if>
					<h2 class="my-3">Filtres :</h2>
					<div class="input-group flex-nowrap">
					  <span class="input-group-text" id="addon-wrapping"><jsp:include page="../icons/loupe.html" /></span>
					  <input type="text" class="form-control" name="article" id="article"
					   <c:if test="${empty nomArticleSaisi}"> placeholder="Le nom de l'article contient" </c:if>
					   <c:if test="${!empty nomArticleSaisi}"> placeholder="${nomArticleSaisi}" </c:if>
					   aria-label="article" aria-describedby="addon-wrapping">
					</div>
					<div class="row align-items-center my-3">
					  <div class="col-auto">
					    <label for="categorie" class="col-form-label" id="label-categorie">Catégorie :</label>
					  </div>
					  <div class="col-auto">
					    <select class="form-control" name="categorie">
					    	<c:if test="${empty categorieSelectionnee}"><option value="-1" selected>Toutes les catégories</option></c:if>
					    	<c:if test="${!empty categorieSelectionnee}"><option value="-1">Toutes les catégories</option></c:if>
						    <c:if test="${!empty listeCategories}">
							    <c:forEach var="categorie" items="${listeCategories}">
							    	<option value="${categorie.no_categorie}"
							    	<c:if test="${categorieSelectionnee.libelle.equals(categorie.libelle) }"> selected </c:if>	
							    	>${categorie.libelle}</option>
							    </c:forEach>			    			    
						    </c:if>
						    <c:if test="${empty listeCategories}">
						    	<option value="null">Pas de categorie disponible</option>
						    </c:if>
						</select>
					  </div>
					</div>	
 					<c:if test="${!empty sessionScope.utilisateur}">
						<div class="row form-check-inline">
						 	<div class="col-12 col-md-6">
						 	<c:if test="${choixUtilisateur.equals('achats')}">
								<input class="form-check-input" type="radio" name="choix" id="achats" value="achats" checked>	
						 	</c:if>
						 	<c:if test="${empty choixUtilisateur || choixUtilisateur.equals('ventes')}">
								<input class="form-check-input" type="radio" name="choix" id="achats" value="achats">	
						 	</c:if>
								<label class="form-check-label" for="choix">Achats</label>
								<div class="row mx-3 my-3">
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixAchat" id="encheresOuvertes" value="encheresOuvertes"
									  <c:if test="${choixAchat.equals('encheresOuvertes')}"> checked</c:if>
									   >
									  <label class="form-check-label" for="encheresOuvertes">enchères ouvertes</label>
									</div>
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixAchat" id="encheresEnCours" value="encheresEnCours"
									  <c:if test="${choixAchat.equals('encheresEnCours')}"> checked</c:if>
									  >
									  <label class="form-check-label" for="encheresEnCours">mes enchères en cours</label>
									</div>
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixAchat" id="encheresRemportees" value="encheresRemportees"
									  <c:if test="${choixAchat.equals('encheresRemportees')}"> checked</c:if>
									  >
									  <label class="form-check-label" for="encheresRemportees">mes enchères remportées</label>
									</div>												 						
								</div>
						 	</div>
						 	<div class="col-12 col-md-6">
						 	<c:if test="${choixUtilisateur.equals('ventes')}">
								<input class="form-check-input" type="radio" name="choix" id="ventes" value="ventes" checked>	
						 	</c:if>
						 	<c:if test="${empty choixUtilisateur || choixUtilisateur.equals('achats')}">
								<input class="form-check-input" type="radio" name="choix" id="ventes" value="ventes">	
						 	</c:if>
								<label class="form-check-label" for="choix">Mes ventes</label>	
								<div class="row mx-3 my-3">
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixVente" id="ventesEnCours" value="ventesEncours"
									  <c:if test="${choixVente.equals('ventesEncours')}"> checked</c:if>
									  >
									  <label class="form-check-label" for="choixVente">mes ventes en cours</label>
									</div>
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixVente" id="ventesNonDebutees" value="ventesNonDebutees"
									  <c:if test="${choixVente.equals('ventesNonDebutees')}"> checked</c:if>
									  >
									  <label class="form-check-label" for="choixVente">ventes non débutées</label>
									</div>
									<div class="form-check col-12">
									  <input class="form-check-input" type="radio" name="choixVente" id="ventesTerminees" value="ventesTerminees"
									  <c:if test="${choixVente.equals('ventesTerminees')}"> checked</c:if>
									  >
									  <label class="form-check-label" for="choixVente">ventes terminées</label>
									</div>	
								</div>									
						 	</div>
						</div>	
 					</c:if>
				</div>
				<div class="col-12 col-md-6 text-center m-auto">
					<button type="submit" class="btn btn-primary btn-lg" id="btn-rechercher">Rechercher</button>
				</div>	
			</form>
			<!-- Fin formulaire de recherche -->
			
			<!--  Liste des enchères -->
			<div class="row my-5 mx-md-5">
				<c:if test="${!empty liste}">
					<c:forEach var="enchere" items="${liste}">
						<div class="col-12 col-md-6 my-2">
							<div class="card">
								<!--Card content-->
								<div class="card-body row">
									<div class="col-md-5">
										<img class="card-img-left img-fluid" src="https://via.placeholder.com/250x150" alt="image">
										</div>
									<div class="col-md-7">
										<!--Title-->
										<h3 class="card-title mt-2 mt-md-0">
										<a href="
										<c:if test="${enchere.vendeur.no_utilisateur.equals(sessionScope.utilisateur.no_utilisateur)}">
										<%=request.getContextPath()%>/vente?no_article=${enchere.no_article}
										</c:if>
										<c:if test="${!enchere.vendeur.no_utilisateur.equals(sessionScope.utilisateur.no_utilisateur)}">
										<%=request.getContextPath()%>/enchere?no_article=${enchere.no_article}
										</c:if>
										">
										${enchere.nom_article}</a></h3>
										<!--Text-->
										<c:if test="${enchere.prix_initial > enchere.prix_vente}">
										<p class="card-text">Prix: ${enchere.prix_initial}</p>
										</c:if>
										<c:if test="${enchere.prix_vente > enchere.prix_initial}">
										<p class="card-text">Prix: ${enchere.prix_vente}</p>
										</c:if>
										<p class="card-text">Fin de l'enchère: ${enchere.date_fin_encheres}</p>
										<p class="card-text">Vendeur : <a href="<%=request.getContextPath()%>/profil?user=${enchere.vendeur.no_utilisateur}">${enchere.vendeur.pseudo}</a></p>    
									</div>			
								</div>
							</div>
						</div>
				    </c:forEach>
				</c:if>
				<c:if test="${empty liste}">
					<p class="mx-5">Aucun résultat correspondant à votre recherche</p>
				</c:if>
			</div>
			<!-- Fin Liste des enchères -->
	</main>
		
	<!-- Pied de page -->
	<%@include file="pied_de_page.jsp" %>
	
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
    <script type="text/javascript"> 
    /* Décoche les autres boutons radio en fonction du click sur un des boutons radio*/
    $('#achats').click(function(){
		$('#ventesEnCours').prop('checked', false);
		$('#ventesNonDebutees').prop('checked', false);
		$('#ventesTerminees').prop('checked', false);
		$('#encheresOuvertes').prop('checked', true);
    });
    $('#ventes').click(function(){
		$('#encheresOuvertes').prop('checked', false);
		$('#encheresEnCours').prop('checked', false);
		$('#encheresRemportees').prop('checked', false);
		$('#ventesEnCours').prop('checked', true);
 	});
    $('#encheresOuvertes').click(function(){
		$('#ventes').prop('checked', false);
		$('#achats').prop('checked', true);
		$('#ventesEnCours').prop('checked', false);
		$('#ventesNonDebutees').prop('checked', false);
		$('#ventesTerminees').prop('checked', false);
 	});
    $('#encheresEnCours').click(function(){
		$('#ventes').prop('checked', false);
		$('#achats').prop('checked', true);
		$('#ventesEnCours').prop('checked', false);
		$('#ventesNonDebutees').prop('checked', false);
		$('#ventesTerminees').prop('checked', false);
 	});
    $('#encheresRemportees').click(function(){
		$('#ventes').prop('checked', false);
		$('#achats').prop('checked', true);
		$('#ventesEnCours').prop('checked', false);
		$('#ventesNonDebutees').prop('checked', false);
		$('#ventesTerminees').prop('checked', false);
 	});
    $('#ventesEnCours').click(function(){
		$('#ventes').prop('checked', true);
		$('#achats').prop('checked', false);
		$('#encheresOuvertes').prop('checked', false);
		$('#encheresEnCours').prop('checked', false);
		$('#encheresRemportees').prop('checked', false);
 	});
    $('#ventesNonDebutees').click(function(){
		$('#ventes').prop('checked', true);
		$('#achats').prop('checked', false);
		$('#encheresOuvertes').prop('checked', false);
		$('#encheresEnCours').prop('checked', false);
		$('#encheresRemportees').prop('checked', false);
 	});
    $('#ventesTerminees').click(function(){
		$('#ventes').prop('checked', true);
		$('#achats').prop('checked', false);
		$('#encheresOuvertes').prop('checked', false);
		$('#encheresEnCours').prop('checked', false);
		$('#encheresRemportees').prop('checked', false);
 	});
    </script>
	</body>
</html>