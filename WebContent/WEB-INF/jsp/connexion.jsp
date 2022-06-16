<%@ page import="fr.eni.javaee.encheres.messages.LecteurMessage" %>
<%@ page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
                                                                                                                                                                                        
                                                                                                                                                                                        
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
		<link rel="stylesheet" href="vendor/css/connexion.css">
		<link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.13.0/css/all.min.css" rel="stylesheet">
		<link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
    <title>Page de connexion</title>
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
            
            <c:if test="${!empty listeCodesErreur}">
			<div class="alert alert-danger" role="alert">
			  <strong>Erreur!</strong>
			  <ul>
			  	<c:forEach var="code" items="${listeCodesErreur}">
			  		<li>${LecteurMessage.getMessageErreur(code)}</li>
			  	</c:forEach>
			  </ul>
			</div>
		</c:if>
            
            <h1> </h1><br>
    
     <form name="connexion" method="post" action="Connexion">
    
            <label for="identifiant" class="label h5 font-weight-bold">Identifiant :</label>
            <input type="text" class="input" name="identifiant" id="identifiant" <c:if test="${!empty identifiant}">value="${identifiant}"</c:if> required="required"><br><br>
                    
            <label for="mot_de_passe" class="label h5 font-weight-bold">Mot de passe :</label>
            <input type="password" class="input" name="mot_de_passe" id="mot_de_passe" <c:if test="${!empty mot_de_passe}">value="${mot_de_passe}"</c:if> required="required"><br><br>
     
	<div class="row my-2">
		<div class="col-6 text-right pr-5">
			<button class="btn btn-lg btn-primary" type="submit">Connexion</button>
		</div>
		<div class="col-6 text-left pl-5">
			<input type="checkbox" name="SeSouvenir" id="SeSouvenir" value="OK">
			<label for="SeSouvenir">Se souvenir de moi</label><br> 
			<a href= "${pageContext.request.contextPath}/passwordrequest">Mot de passe oublié ?</a>
		</div>
				
	</div>		     
     
      </form>
      <br>
      <div >
			<a href= "${pageContext.request.contextPath}/creer"><input type="button" class="btn btn-lg btn-primary btnCreer" value="Créer un compte"></a>
	</div>	
      
    </div>
    </div>
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