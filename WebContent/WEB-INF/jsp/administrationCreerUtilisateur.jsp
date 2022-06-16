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
		<title>Administration - Utilisateur</title>
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
		
		<!-- Titre de la page -->
		<div class="row text-center">
			<div class="col">
				<h1 class="my-5">Administration des utilisateurs</h1> 
			</div>
		</div>
		
		<main class="container">
			<h2>Créer un utilisateur</h2>		
			<form action="<%=request.getContextPath()%>/AdministrationCreerUtilisateur" method="post">
			<div class="container">
				<div class="row">
					<div class="form-group col-6">
					  <label for="pseudo">Pseudo</label>
					  <input type="text" class="form-control" id="pseudo" name="pseudo"
						<c:if test="${!empty pseudo}"> placeholder="${pseudo}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="nom">Nom</label>
					  <input type="text" class="form-control" id="nom" name="nom"
					  <c:if test="${!empty nom}"> placeholder="${nom}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="prenom">Prénom</label>
					  <input type="text" class="form-control" id="prenom" name="prenom"
					  <c:if test="${!empty prenom}"> placeholder="${prenom}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="email">Email</label>
					  <input type="email" class="form-control" id="email" name="email"
					  <c:if test="${!empty email}"> placeholder="${email}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="rue">Rue</label>
					  <input type="text" class="form-control" id="rue" name="rue"
					  <c:if test="${!empty rue}"> placeholder="${rue}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="code_postal">Code postal</label>
					  <input type="text" class="form-control" id="code_postal" name="code_postal"
					  <c:if test="${!empty code_postal}"> placeholder="${code_postal}"</c:if>
					  >
					</div>
					<div class="form-group col-6">
					  <label for="ville">Ville</label>
					  <input type="text" class="form-control" id="ville" name="ville"
					  <c:if test="${!empty ville}"> placeholder="${ville}"</c:if>
					  >
					</div>		
				</div>	
			  	<button type="submit" class="btn btn-primary font-weight-bold">Enregistrer</button>
			</div>
			</form>
		</main>
	</main>	
	</body>
</html>