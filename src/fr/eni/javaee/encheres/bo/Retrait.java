package fr.eni.javaee.encheres.bo;

import java.io.Serializable;

public class Retrait implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private ArticleVendu article;
	private String rue;
	private String code_postal;
	private String ville;
	private Boolean retire = Boolean.FALSE;
	
	public Retrait() {
	}

	public Retrait(ArticleVendu article, String rue, String code_postal, String ville) {
		super();
		this.article = article;
		this.rue = rue;
		this.code_postal = code_postal;
		this.ville = ville;
	}

	public ArticleVendu getArticle() {
		return article;
	}

	public void setArticle(ArticleVendu article) {
		this.article = article;
	}

	public String getRue() {
		return rue;
	}

	public void setRue(String rue) {
		this.rue = rue;
	}

	public String getCode_postal() {
		return code_postal;
	}

	public void setCode_postal(String code_postal) {
		this.code_postal = code_postal;
	}

	public String getVille() {
		return ville;
	}

	public void setVille(String ville) {
		this.ville = ville;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public Boolean getRetire() {
		return retire;
	}

	public void setRetire(Boolean retire) {
		this.retire = retire;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Retrait [article=");
		builder.append(article);
		builder.append(", rue=");
		builder.append(rue);
		builder.append(", code_postal=");
		builder.append(code_postal);
		builder.append(", ville=");
		builder.append(ville);
		builder.append(", retire=");
		builder.append(retire);
		builder.append("]");
		return builder.toString();
	}
	
}
