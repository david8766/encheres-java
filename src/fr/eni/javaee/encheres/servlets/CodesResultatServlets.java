package fr.eni.javaee.encheres.servlets;

/**
 * Les codes disponibles sont entre 30000 et 39999
 */
public abstract class CodesResultatServlets {
	
	// Connexion
	public static final int MOTS_DE_PASSE_DIFFERENTS = 30001;
	public static final int MOT_DE_PASSE_INCORRECT = 30002;
	public static final int UTILISATEUR_INCONNU = 30003;
	//Renvoi de mot de passe
	public static final int ADRESSE_EMAIL_INCONNUE = 30004;
	
	// Encheres
	public static final int LECTURE_PARAMETRE_ENCHERE = 30041;
	public static final int ENCHERE_MODIFIABLE = 30042;
	
	// Ventes
	public static final int LECTURE_PARAMETRE_VENTE = 30100;
	public static final int VENTE_INCONNUE = 30101;
	
	// Liste Encheres
	public static final int SELECTION_DE_TOUTES_LES_CATEGORIES_ERREUR = 30600;
	public static final int SELECTION_DUNE_CATEGORIE_ERREUR = 30601;
	public static final int SELECTION_DE_TOUTES_LES_ENCHERES_EN_COURS_ERREUR = 30602;
	public static final int SELECTION_DUN_UTILISATEUR_ERREUR = 30603;
	public static final int FORMAT_NUMERO_CATEGORIE_ERREUR = 30604;
	public static final int SELECTION_DE_TOUTES_LES_ENCHERES_EN_COURS_EN_MODE_CONNECTE_ERREUR = 30605;
	public static final int SELECTION_DES_ENCHERES_EN_COURS_EN_MODE_CONNECTE_ERREUR = 30606;
	public static final int SELECTION_DES_ENCHERES_REMPORTEES_EN_MODE_CONNECTE_ERREUR = 30607;
	public static final int SELECTION_DES_VENTES_EN_COURS_EN_MODE_CONNECTE_ERREUR = 30608;
	public static final int SELECTION_DES_VENTES_NON_DEBUTEES_EN_MODE_CONNECTE_ERREUR = 30609;
	public static final int SELECTION_DES_VENTES_TERMINEES_EN_MODE_CONNECTE_ERREUR = 30610;
	
	// Administration
	public static final int SELECTION_DES_UTILISATEURS_ERREUR = 30700;
	
	
}