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
    <link rel="stylesheet" href="vendor/css/compte.css">
    <link rel="stylesheet" href="vendor/bootstrap/css/bootstrap.min.css">
    <link rel="stylesheet" href="vendor/bootstrap/css/bootstrap_yeti.min.css">
    <title>Création ou Modification du compte utilisateur</title>
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
            
            <c:if test="${!empty utilisateur}">
				<h1 class="my-5">Mon Profil</h1>
			</c:if>
			<c:if test="${empty utilisateur}">
				<h1 class="my-5">Créer un compte</h1>
			</c:if>
			
			
               
                <form name="inscription" <c:if test="${empty utilisateur}">action="creer"</c:if><c:if test="${!empty utilisateur}">action="modifier"</c:if> method="post">
               

                    <div class="col1">
                    	<label for="pseudo" class="label" >Pseudo :</label>
                    	<input type="text" class="input" name="pseudo" id="pseudo" value="${utilisateur.pseudo}" required="required"><br><br>
                    </div>
                    
                    <div class="col2">
                    	<label for="nom" class="label">Nom :</label>
                    	<input type="text" class="input" name="nom" id="nom" value="${utilisateur.nom}" required="required"><br><br>
                    </div>
                    
					<div class="col1">
                    	<label for="prenom" class="label">Prénom :</label>
                    	<input type="text" class="input" name="prenom" id="prenom" value="${utilisateur.prenom}" required="required"><br><br>
                    </div>
                    
                    <div class="col2">
                    	<label for="email" class="label">Email :</label>
                    	<input type="email" class="input" name="email" id="email" value="${utilisateur.email}" required="required"><br><br>
                    </div>
                    
                    <div class="col1">
                    	<label for="telephone" class="label">Téléphone :</label>
                    	<input type="tel" class="input" name="telephone" id="telephone" value="${utilisateur.telephone}" pattern="^((\+\d{1,3}(-| )?\(?\d\)?(-| )?\d{1,5})|(\(?\d{2,6}\)?))(-| )?(\d{3,4})(-| )?(\d{4})(( x| ext)\d{1,5}){0,1}$"><br><br>
                    </div>
                    
                    <div class="col2">
                   		<label for="rue" class="label">Rue </label>
                    	<input type="text" class="input" name="rue" id="rue" value="${utilisateur.rue}" required="required"><br><br>
                    </div>
                    
                    <div class="col1">
                   		<label for="code_postal" class="label">Code Postal </label>
                   		<input type="text" class="input" name="code_postal" id="code_postal" value="${utilisateur.code_postal}" required="required"><br><br>
                    </div>
                    
                    <div class="col2">
                   		<label for="ville" class="label">Ville </label>
                   		<input type="text" class="input" name="ville" id="ville" value="${utilisateur.ville}" required="required"><br><br>
                    </div>
                    
                    <div class="col1">
                    	<label for="mot_de_passe" class="label">Mot de passe :</label>
                    	<input type="password" class="input" name="mot_de_passe" id="mot_de_passe" <c:if test="${empty utilisateur}">value="password"</c:if><c:if test="${!empty utilisateur}">value="${utilisateur.mot_de_passe}"</c:if> required="required"><br><br>
                    </div>
                    
                    <div class="col2">
                    	<label for="password" class="label">Confirmation :</label>
                    	<input type="password" class="input" name="confirmation" id="confirmation" <c:if test="${empty utilisateur}">value="password"</c:if><c:if test="${!empty utilisateur}">value="${utilisateur.mot_de_passe}"</c:if> required="required"><br><br>
                    </div>
                    
                    <c:if test="${!empty utilisateur}">
                    	<label for="credit" class="label">Crédit :</label>
                    	<p class="inline"> ${utilisateur.credit}</p><br>
                    </c:if>
                    
                    <!--Validation du formulaire-->

					<c:if test="${empty utilisateur}">
                    <div class="row my-3">
						<div class="col-6 text-right pr-5">
							<button class="btn btn-lg btn-primary" type="submit">Créer</button>
						</div>
						<div class="col-6 text-left pl-5">
							<button class="btn btn-lg btn-primary" type="reset">Annuler</button>
						</div>		
					</div>
					</c:if>
					<c:if test="${!empty utilisateur}">
					<div class="row my-3">
						<div class="col-4 text-right pr-5">
							<button class="btn btn-lg btn-primary" type="submit">Enregistrer</button>
						</div>
						<div class="col-4 text-left pl-5">
							<a href="${pageContext.request.contextPath}/supprimer"><input type="button" class="btn btn-lg btn-primary" value="Supprimer"></a>
						</div>
						<div class="col-4 text-left pl-5">
							<a href= "${pageContext.request.contextPath}/profil"><input type="button" class="btn btn-lg btn-primary" value="Retour"></a>
						</div>			
					</div>
					</c:if>
                </form>
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
</body></html> 