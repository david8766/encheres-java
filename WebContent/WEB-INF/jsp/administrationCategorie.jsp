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
		<title>Administration - Catégorie</title>
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
				<h1 class="my-5">Administration des catégories</h1> 
			</div>
		</div>
		
		<!-- Liste des catégories -->
		<div class="container">

		<c:if test="${!empty creer}">
			<h2>Créer une catégorie</h2>		
		</c:if>
		<c:if test="${!empty modifier}">
			<h2>Modifier cette catégorie</h2>		
		</c:if>

			<form action="<%=request.getContextPath()%>/AdministrationDesCategories" method="post">
			  <div class="form-group">
			    <label for="libelle" class="h5">Libellé :</label>
			    <input type="text" class="form-control" name="libelle" id="libelle"
				<c:if test="${!empty creer}">
					placeholder="libellé"		
				</c:if>
				<c:if test="${!empty modifier}">
					placeholder="${categorie.libelle}"		
				</c:if>
				>
				<c:if test="${!empty creer}">
					<input id="action" name="action" type="hidden" value="creer">		
				</c:if>
				<c:if test="${!empty modifier}">
					<input id="action" name="action" type="hidden" value="modifier">
					<input id="categorie" name="categorie" type="hidden" value="${categorie.no_categorie}">		
				</c:if>
			  </div>
			  <button type="submit" class="btn btn-primary font-weight-bold">Enregistrer</button>
			</form>
		</div>
	</main>	
	</body>
</html>