package br.com.biblia.enums;

import java.util.HashMap;
import java.util.Map;

import lombok.Getter;

public enum IdiomaEnum {

	HEBRAICO("Hebraico"),
	GREGO("Grego"),
	ARAMAICO("Aramaico");
	
	@Getter
	private String descricao;
	
	private IdiomaEnum(String descricao) {
		this.descricao = descricao;
	}
	
	private static Map<String, IdiomaEnum> hash;
	
	public static IdiomaEnum fromDescricao(String descricao) {
		return hash.get( descricao );
	}
	
	static {
		hash = new HashMap<>();
		for (IdiomaEnum idioma : IdiomaEnum.values()) {
			hash.put( idioma.getDescricao(), idioma );
		}
	}
	
	
}
