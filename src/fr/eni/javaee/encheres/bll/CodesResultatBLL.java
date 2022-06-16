package fr.eni.javaee.encheres.bll;

/**
 * Les codes disponibles sont entre 20000 et 29999
 */
public abstract class CodesResultatBLL {
	
	/******* ARTICLES *******/
	public static final int REGLE_ARTICLE_LIBELLE_MANQUANT = 20021;
	public static final int REGLE_ARTICLE_LIBELLE_LONG = 20022;
	public static final int REGLE_ARTICLE_DESCRIPTION_LONG = 20023;
	public static final int REGLE_ARTICLE_DATES_NULL = 20024;
	public static final int REGLE_ARTICLE_DATE_PASSEE = 20025;
	public static final int REGLE_ARTICLE_DATE_INCOHERENTE = 20026;
	public static final int REGLE_ARTICLE_PRIX_INITIAL = 20027;
	public static final int REGLE_ARTICLE_PRIX_VENTE = 20028;
	public static final int REGLE_ARTICLE_CATEGORIE_MANQUANTE = 20029;
	public static final int REGLE_ARTICLE_CATEGORIE_INCONNUE = 20030;
	public static final int REGLE_ARTICLE_VENDEUR_MANQUANT = 20031;
	public static final int REGLE_ARTICLE_VENDEUR_INCONNU = 20032;
	public static final int REGLE_ARTICLE_SUPPRIMER = 20033;
	public static final int REGLE_ARTICLE_ENCHERE_EN_COURS = 20034;
	
	/******* ENCHERES  *******/
	public static final int REGLE_ENCHERE_ARTICLE_INCONNU = 20041;
	public static final int REGLE_ENCHERE_UTILISATEUR_INCONNU = 20042;
	public static final int REGLE_ENCHERE_DATE_MANQUANTE = 20043;
	public static final int REGLE_ENCHERE_DATE_INVALIDE = 20044;
	public static final int REGLE_ENCHERE_ARTICLE_INTROUVABLE = 20045;
	public static final int REGLE_ENCHERE_CREDIT_INSUFFISANT = 20046;
	public static final int REGLE_ENCHERE_INSUFFISANTE = 20047;
	
	/******* RETRAITS  *******/
	public static final int REGLE_RETRAIT_ARTICLE_INCONNU = 20061;
	public static final int REGLE_RETRAIT_RUE_MANQUANT = 20062;
	public static final int REGLE_RETRAIT_RUE_LONG = 20063;
	public static final int REGLE_RETRAIT_CODE_POSTAL_MANQUANT = 20064;
	public static final int REGLE_RETRAIT_CODE_POSTAL_LONG= 20065;
	public static final int REGLE_RETRAIT_VILLE_MANQUANT = 20066;
	public static final int REGLE_RETRAIT_VILLE_LONG = 20067;
	
	/******* CATEGORIE *******/
	public static final int REGLE_CATEGORIE_LIBELLE_MANQUANT = 20081;
	public static final int REGLE_CATEGORIE_LIBELLE_LONG = 20082;
	
	/******* UTILISATEURS *******/
	public static final int REGLE_UTILISATEUR_NOM_MANQUANT = 20100;
	public static final int REGLE_UTILISATEUR_NOM_LONG = 20101;
	public static final int REGLE_UTILISATEUR_PRENOM_MANQUANT = 20102;
	public static final int REGLE_UTILISATEUR_PRENOM_LONG = 20103;
	public static final int REGLE_UTILISATEUR_EMAIL_MANQUANT = 20104;
	public static final int REGLE_UTILISATEUR_EMAIL_LONG = 20105;
	public static final int REGLE_UTILISATEUR_EMAIL_DOUBLON = 20106;
	public static final int REGLE_UTILISATEUR_PSEUDO_MANQUANT = 20107;
	public static final int REGLE_UTILISATEUR_PSEUDO_LONG = 20108;
	public static final int REGLE_UTILISATEUR_PSEUDO_CARACTERE_NON_AUTORISE = 20109;
	public static final int REGLE_UTILISATEUR_PSEUDO_DOUBLON = 20110;
	public static final int REGLE_UTILISATEUR_TELEPHONE_LONG = 20111;
	public static final int REGLE_UTILISATEUR_TELEPHONE_CARACTERE_NON_AUTORISE = 20112;
	public static final int REGLE_UTILISATEUR_RUE_MANQUANT = 20113;
	public static final int REGLE_UTILISATEUR_RUE_LONG = 20114;
	public static final int REGLE_UTILISATEUR_CODE_POSTAL_ERREUR = 20115;
	public static final int REGLE_UTILISATEUR_VILLE_MANQUANT = 20116;
	public static final int REGLE_UTILISATEUR_VILLE_LONG = 20117;
	public static final int REGLE_UTILISATEUR_MOT_DE_PASSE_MANQUANT = 20118;
	public static final int REGLE_UTILISATEUR_MOT_DE_PASSE_LONG = 20119;
}
