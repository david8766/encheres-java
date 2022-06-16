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
		<link rel="stylesheet" href="vendor/css/profil.css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
		<title></title>
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
			<div class="row margin-content">
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Pseudo : </span>
					<span class="h5">${user.pseudo}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Nom : </span>
					<span class="h5">${user.nom}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Prénom : </span>
					<span class="h5">${user.prenom}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Email : </span>
					<span class="h5">${user.email}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Téléphone : </span>
					<span class="h5">${user.telephone}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Rue : </span>
					<span class="h5">${user.rue}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Code postal : </span>
					<span class="h5">${user.code_postal}</span>
				</div>
				<div class="col-12 my-2">
					<span class="h4 font-weight-bold">Ville : </span>
					<span class="h5">${user.ville}</span>
				</div>
				</div>
			</div>	
			<c:if test="${utilisateur.no_utilisateur == user.no_utilisateur}">
	            <div class="row my-3">
					<div class="col-12 text-center pl-5">
						<a href="${pageContext.request.contextPath}/modifier"><input type="button" class="btn btn-lg btn-primary" value="Modifier"></a>
					</div>
				</div>
			</c:if>		
		<br>
		
	</main>
	
	<!-- Pied de page -->
	<%@include file="pied_de_page.jsp" %>
	
	<!-- Placed at the end of the document so the pages load faster -->
    <!-- Placed at the end of the document so the pages load faster -->
    <script src="vendor/jquery/jquery.min.js"></script>
    <script src="vendor/bootstrap/js/bootstrap.bundle.min.js"></script>
</body>
</html>