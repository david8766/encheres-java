<!-- Navigation -->
<nav class="navbar navbar-expand-lg navbar-dark bg-primary static-top">
  <div class="container-fluid">
	<a class="navbar-brand" href="<%=request.getContextPath()%>/ListeEncheres" id="link-title">ENI Enchères</a>
	<button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
	  <span class="navbar-toggler-icon"></span>
	</button>
	<div class="collapse navbar-collapse" id="navbarResponsive">
	  <ul class="navbar-nav ml-auto">
	  	<!-- Affichage de liens en mode déconnecté -->
	  	<c:if test="${empty sessionScope.utilisateur}">
			<li class="nav-item active">
			  <a class="nav-link" href="<%=request.getContextPath()%>/Connexion" id="link-connect">Se connecter - S'inscrire
				<span class="sr-only">(current)</span>
			  </a>
			</li>
		</c:if>
		<!-- Affichage de liens en mode connecté -->
		<c:if test="${!empty sessionScope.utilisateur}">
			<li class="nav-item active">
			  <a class="nav-link" href="<%=request.getContextPath()%>/ListeEncheres?reset=true" id="link-connect">Enchères</a>
			</li>					
			<li class="nav-item active">
			  <a class="nav-link" href="<%=request.getContextPath()%>/vente" id="link-connect">Vendre un article</a>
			</li>
			<li class="nav-item active">
			  <a class="nav-link" href="<%=request.getContextPath()%>/mon_profil" id="link-connect">Mon profil</a>
			</li>
			<li class="nav-item active">
			  <a class="nav-link" href="<%=request.getContextPath()%>/SeDeconnecter" id="link-connect">Déconnexion</a>
			</li>					
		</c:if>
	  </ul>
	</div>
  </div>
</nav>