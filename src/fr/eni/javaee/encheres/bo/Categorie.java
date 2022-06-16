package fr.eni.javaee.encheres.bo;

import java.io.Serializable;

public class Categorie implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private int no_categorie;
	private String libelle;
	
	public Categorie() {
	}

	public Categorie(String libelle) {
		super();
		this.libelle = libelle;
	}

	public int getNo_categorie() {
		return no_categorie;
	}

	public void setNo_categorie(int no_categorie) {
		this.no_categorie = no_categorie;
	}

	public String getLibelle() {
		return libelle;
	}

	public void setLibelle(String libelle) {
		this.libelle = libelle;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Categorie [libelle=");
		builder.append(libelle);
		builder.append("]");
		return builder.toString();
	}
	
	
}
