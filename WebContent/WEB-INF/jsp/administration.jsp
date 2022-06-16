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
		<title>Administration - Accueil</title>
	</head>
	<body>
	<header>
		<!-- Navigation -->
		<nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">
		  <div class="container-fluid">
			<a class="navbar-brand" href="<%=request.getContextPath()%>/ListeEncheres" id="link-title">ENI Enchères</a>
			<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
			  <span class="navbar-toggler-icon"></span>
			</button>
		  </div>
		</nav>	
	</header>
	
	<!--  Corps de la page  -->
	<main class="container-fluid">
		<!-- Messages d'alertes -->
		<div class="container">
			<c:if test="${!empty listeCodesErreur}">
				<div class="alert alert-danger" role="alert">
					<c:forEach var="codeErreur" items="${listeCodesErreur}">
						${LecteurMessage.getMessageErreur(codeErreur)} <!-- Ne fonctionne pas si la classe LecteurMesasge est abstraite -->
					</c:forEach>
				</div>
			</c:if>
		</div>
		<div class=container>	
			<!-- Titre de la page -->
			<div class="row text-center">
				<div class="col">
					<h1 class="my-5">Administration de ENI-Enchères</h1> 
				</div>
			</div>
			
			<!-- Liste des catégories -->
			<h2>Liste des catégories</h2>
			<a class="navbar-brand" href="<%=request.getContextPath()%>/AdministrationDesCategories?action=creer">Créer une nouvelle catégorie</a>
			<table class="table table-dark">
			  <thead>
			    <tr>
			      <th scope="col">Numéro</th>
			      <th scope="col">Libellé</th>
			      <th scope="col">Actions</th>
			    </tr>
			  </thead>
			  <tbody>
				<c:if test="${!empty listeCategories}">
					<c:forEach var="categorie" items="${listeCategories}">
				    <tr>
				      <th scope="col">${categorie.no_categorie}</th>
				      <th scope="col">${categorie.libelle}</th>
				      <th scope="col">
				      	<a class="navbar-brand text-light" href="<%=request.getContextPath()%>/AdministrationDesCategories?action=modifier&categorie=${categorie.no_categorie}">Modifier</a>
				      	<a class="navbar-brand text-danger" href="<%=request.getContextPath()%>/SupprimerCategorie?categorie=${categorie.no_categorie}">Supprimer</a>
				      </th>
				    </tr>						
					</c:forEach>
				</c:if>
			  </tbody>
			</table>
			<!-- Liste des utilisateurs -->
			<h2>Liste des utilisateurs</h2>
			<a class="navbar-brand" href="<%=request.getContextPath()%>/AdministrationCreerUtilisateur">Créer un nouvel utilisateur</a>
			<table class="table table-dark">
			  <thead>
				<tr>
					<th scope="col">Numéro</th>
					<th scope="col">Nom Prénom</th>
					<th scope="col">Actions</th>
				</tr>
			  </thead>
			  <tbody>
				<c:if test="${!empty listeCategories}">
					<c:forEach var="utilisateur" items="${listeUtilisateurs}">
					<tr>
						<th scope="col">${utilisateur.no_utilisateur}</th>
						<th scope="col">${utilisateur.nom}   ${utilisateur.prenom}</th>
						<th scope="col">
						<a class="navbar-brand text-light" href="<%=request.getContextPath()%>">Désactiver</a>
						<a class="navbar-brand text-danger" href="<%=request.getContextPath()%>//AdministrationUtilisateurSupprimer?utilisateur=${utilisateur.no_utilisateur}">Supprimer</a>
						</th>
					</tr>						
					</c:forEach>
				</c:if>
			  </tbody>
			</table>
		</div>
	</main>
		
	</body>
</html>