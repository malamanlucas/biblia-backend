package br.com.biblia.apps.importador.sbb;

import br.com.biblia.enums.LivroEnum;
import lombok.Getter;

public enum SbbLivroEnum {

	GENESIS ("GENESIS", "gn"),
	EXODO ("EXODO", "ex"),
	LEVITICO ("LEVITICO", "lv"),
	NUMEROS ("NUMEROS", "nm"),
	DEUTERONOMIO ("DEUTERONOMIO", "dt"),
	JOSUE ("JOSUE", "js"),
	JUIZES ("JUIZES", "jz"),
	RUTE ("RUTE", "rt"),
	PRIMEIRO_SAMUEL ("PRIMEIRO_SAMUEL", "1sm"),
	SEGUNDO_SAMUEL ("SEGUNDO_SAMUEL", "2sm"),
	PRIMEIRO_REIS ("PRIMEIRO_REIS", "1rs"),
	SEGUNDO_REIS ("SEGUNDO_REIS", "2rs"),
	PRIMEIRO_CRONICAS ("PRIMEIRO_CRONICAS", "1cr"),
	SEGUNDO_CRONICAS ("SEGUNDO_CRONICAS", "2cr"),
	ESDRAS ("ESDRAS", "ed"),
	NEEMIAS ("NEEMIAS", "nm"),
	ESTER ("ESTER", "et"),
	JO ("JO", "joo"),
	SALMOS ("SALMOS", "sl"),
	PROVÉRBIOS ("PROVÉRBIOS", "pv"),
	ECLESIASTES ("ECLESIASTES", "ec"),
	CANTARES ("CANTARES", "ct"),
	ISAÍAS ("ISAÍAS", "is"),
	JEREMIAS ("JEREMIAS", "jr"),
	LAMENTACOES ("LAMENTACOES", "lm"),
	EZEQUIEL ("EZEQUIEL", "ez"),
	DANIEL ("DANIEL", "dn"),
	OSEIAS ("OSEIAS", "os"),
	JOEL ("JOEL", "jl"),
	AMOS ("AMOS", "am"),
	OBADIAS ("OBADIAS", "ob"),
	JONAS ("JONAS", "jn"),
	MIQUEIAS ("MIQUEIAS", "mq"),
	NAUM ("NAUM", "na"),
	HABACUQUE ("HABACUQUE", "hc"),
	SOFONIAS ("SOFONIAS", "sf"),
	AGEU ("AGEU", "ag"),
	ZACARIAS ("ZACARIAS", "zc"),
	MALAQUIAS ("MALAQUIAS", "ml"),
	MATEUS ("MATEUS", "mt"),
	MARCOS ("MARCOS", "mc"),
	LUCAS ("LUCAS", "lc"),
	JOAO ("JOAO", "jhn"),
	ATOS ("ATOS", "at"),
	ROMANOS ("ROMANOS", "rm"),
	PRIMEIRA_CORINTIOS ("PRIMEIRA_CORINTIOS", "1co"),
	SEGUNDA_CORINTIOS ("SEGUNDA_CORINTIOS", "2co"),
	GALATAS ("GALATAS", "gl"),
	EFÉSIOS ("EFÉSIOS", "ef"),
	FILIPENSES ("FILIPENSES", "fp"),
	COLOSSENSES ("COLOSSENSES", "cl"),
	PRIMEIRA_TESSALONICENSES ("PRIMEIRA_TESSALONICENSES", "1ts"),
	SEGUNDA_TESSALONICENSES ("SEGUNDA_TESSALONICENSES", "2ts"),
	PRIMEIRA_TIMOTEO ("PRIMEIRA_TIMOTEO", "1tm"),
	SEGUNDA_TIMOTEO ("SEGUNDA_TIMOTEO", "2tm"),
	TITO ("TITO", "tt"),
	FILEMOM ("FILEMOM", "fl"),
	HEBREUS ("HEBREUS", "hb"),
	TIAGO ("TIAGO", "tg"),
	PRIMEIRA_PEDRO ("PRIMEIRA_PEDRO", "1pe"),
	SEGUNDA_PEDRO ("SEGUNDA_PEDRO", "2pe"),
	PRIMEIRA_JOAO ("PRIMEIRA_JOAO", "1jo"),
	SEGUNDA_JOAO ("SEGUNDA_JOAO", "2jo"),
	TERCEIRA_JOAO ("TERCEIRA_JOAO", "3jo"),
	JUDAS ("JUDAS", "jd"),
	APOCALIPSE ("APOCALIPSE", "ap");
	
	@Getter
	private LivroEnum livroEnum;
	
	@Getter
	private String livroKey;
	
	private SbbLivroEnum(String enumName, String livroKey) {
		this.livroEnum = LivroEnum.valueOf(enumName);
		this.livroKey = livroKey;
	}
	
}
