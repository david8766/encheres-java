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
    <title>Renvoi du Mot de Passe</title>
</head>

<body>
    <!-- Entete de page -->
    <header>

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
    
     <form name="renvoi" method="post" action="renvoi">
    
            <label for="email" class="label">Adresse e-mail :</label>
            <input type="email" class="input" name="email" id="email" required="required"><br><br>
                    
            
			<button class="btn btn-lg btn-primary" type="submit">Envoyer</button>
      </form>
      <br>
      <c:if test="${!empty email}">
		<p>Votre mot de passe a été trouvé et sera bien envoyé à l'adresse : "${email}" lorsque la fonction sera disponible ;-)</p>
		<div class="col-4 text-left pl-5">
			<a href= "${pageContext.request.contextPath}/connexion"><input type="button" class="btn btn-lg btn-primary" value="Se connecter"></a>
		</div>	
		</c:if>
      
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