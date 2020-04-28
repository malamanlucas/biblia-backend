package br.com.biblia.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;

import lombok.Getter;

@JsonFormat(shape=Shape.OBJECT)
public enum Testamento {

	AMBOS("Ambos", null),
	NOVO("Novo Testamento", IdiomaEnum.GREGO),
	VELHO("Velho Testamento", IdiomaEnum.HEBRAICO);
	
	@Getter
	private String descricao;
	
	@Getter
	private IdiomaEnum idioma;

	private Testamento(String descricao, IdiomaEnum idioma) {
		this.descricao = descricao;
		this.idioma = idioma;
	}

	@JsonIgnore
	public boolean isNovo() {
		return this == NOVO;
	}
	
	@JsonIgnore
	public boolean isVelho() {
		return this == VELHO;
	}

	@JsonIgnore
	public boolean isAmbos() {
		return this == AMBOS;
	}
	
}
