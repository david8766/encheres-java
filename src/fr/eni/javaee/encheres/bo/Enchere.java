package fr.eni.javaee.encheres.bo;

import java.io.Serializable;
import java.time.LocalDate;


public class Enchere implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private Utilisateur encherisseur;
	private ArticleVendu article;
	private LocalDate date_enchere;
	private int montant_enchere;
	
	public Enchere() {
	}

	public Enchere(Utilisateur encherisseur, ArticleVendu article, LocalDate date_enchere, int montant_enchere) {
		super();
		this.encherisseur = encherisseur;
		this.article = article;
		this.date_enchere = date_enchere;
		this.montant_enchere = montant_enchere;
	}

	public Utilisateur getEncherisseur() {
		return encherisseur;
	}

	public void setEncherisseur(Utilisateur encherisseur) {
		this.encherisseur = encherisseur;
	}

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

	public LocalDate getDate_enchere() {
		return date_enchere;
	}

	public void setDate_enchere(LocalDate date_enchere) {
		this.date_enchere = date_enchere;
	}

	public int getMontant_enchere() {
		return montant_enchere;
	}

	public void setMontant_enchere(int montant_enchere) {
		this.montant_enchere = montant_enchere;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Enchere [encherisseur=");
		builder.append(encherisseur);
		builder.append(", article=");
		builder.append(article);
		builder.append(", date_enchere=");
		builder.append(date_enchere);
		builder.append(", montant_enchere=");
		builder.append(montant_enchere);
		builder.append("]");
		return builder.toString();
	}

}
