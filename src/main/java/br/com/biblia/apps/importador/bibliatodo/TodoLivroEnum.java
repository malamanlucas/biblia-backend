package br.com.biblia.apps.importador.bibliatodo;

import br.com.biblia.enums.LivroEnum;
import lombok.Getter;

public enum TodoLivroEnum {

	GENESIS ("GENESIS", "genesis"),
	EXODO ("EXODO", "exodo"),
	LEVITICO ("LEVITICO", "levitico"),
	NUMEROS ("NUMEROS", "numeros"),
	DEUTERONOMIO ("DEUTERONOMIO", "deuteronomio"),
	JOSUE ("JOSUE", "josue"),
	JUIZES ("JUIZES", "juizes"),
	RUTE ("RUTE", "rute"),
	PRIMEIRO_SAMUEL ("PRIMEIRO_SAMUEL", "1samuel"),
	SEGUNDO_SAMUEL ("SEGUNDO_SAMUEL", "2samuel"),
	PRIMEIRO_REIS ("PRIMEIRO_REIS", "1reis"),
	SEGUNDO_REIS ("SEGUNDO_REIS", "2reis"),
	PRIMEIRO_CRONICAS ("PRIMEIRO_CRONICAS", "1cronicas"),
	SEGUNDO_CRONICAS ("SEGUNDO_CRONICAS", "2cronicas"),
	ESDRAS ("ESDRAS", "esdras"),
	NEEMIAS ("NEEMIAS", "neemias"),
	ESTER ("ESTER", "ester"),
	JO ("JO", "jo"),
	SALMOS ("SALMOS", "salmos"),
	PROVÉRBIOS ("PROVÉRBIOS", "proverbios"),
	ECLESIASTES ("ECLESIASTES", "eclesiastes"),
	CANTARES ("CANTARES", "cantares"),
	ISAÍAS ("ISAÍAS", "isaias"),
	JEREMIAS ("JEREMIAS", "jeremias"),
	LAMENTACOES ("LAMENTACOES", "lamentacoes"),
	EZEQUIEL ("EZEQUIEL", "ezequiel"),
	DANIEL ("DANIEL", "daniel"),
	OSEIAS ("OSEIAS", "oseias"),
	JOEL ("JOEL", "joel"),
	AMOS ("AMOS", "amos"),
	OBADIAS ("OBADIAS", "obadias"),
	JONAS ("JONAS", "jonas"),
	MIQUEIAS ("MIQUEIAS", "miqueias"),
	NAUM ("NAUM", "naum"),
	HABACUQUE ("HABACUQUE", "habacuque"),
	SOFONIAS ("SOFONIAS", "sofonias"),
	AGEU ("AGEU", "ageu"),
	ZACARIAS ("ZACARIAS", "zacarias"),
	MALAQUIAS ("MALAQUIAS", "malaquias"),
	MATEUS ("MATEUS", "mateus"),
	MARCOS ("MARCOS", "marcos"),
	LUCAS ("LUCAS", "lucas"),
	JOAO ("JOAO", "joao"),
	ATOS ("ATOS", "atos"),
	ROMANOS ("ROMANOS", "romanos"),
	PRIMEIRA_CORINTIOS ("PRIMEIRA_CORINTIOS", "1corintios"),
	SEGUNDA_CORINTIOS ("SEGUNDA_CORINTIOS", "2corintios"),
	GALATAS ("GALATAS", "galatas"),
	EFÉSIOS ("EFÉSIOS", "efesios"),
	FILIPENSES ("FILIPENSES", "filipenses"),
	COLOSSENSES ("COLOSSENSES", "colossenses"),
	PRIMEIRA_TESSALONICENSES ("PRIMEIRA_TESSALONICENSES", "1tessalonicenses"),
	SEGUNDA_TESSALONICENSES ("SEGUNDA_TESSALONICENSES", "2tessalonicenses"),
	PRIMEIRA_TIMOTEO ("PRIMEIRA_TIMOTEO", "1timoteo"),
	SEGUNDA_TIMOTEO ("SEGUNDA_TIMOTEO", "2timoteo"),
	TITO ("TITO", "tito"),
	FILEMOM ("FILEMOM", "filemom"),
	HEBREUS ("HEBREUS", "hebreus"),
	TIAGO ("TIAGO", "tiago"),
	PRIMEIRA_PEDRO ("PRIMEIRA_PEDRO", "1pedro"),
	SEGUNDA_PEDRO ("SEGUNDA_PEDRO", "2pedro"),
	PRIMEIRA_JOAO ("PRIMEIRA_JOAO", "1joao"),
	SEGUNDA_JOAO ("SEGUNDA_JOAO", "2joao"),
	TERCEIRA_JOAO ("TERCEIRA_JOAO", "3joao"),
	JUDAS ("JUDAS", "judas"),
	APOCALIPSE ("APOCALIPSE", "apocalipse");
	
	@Getter
	private LivroEnum livroEnum;
	
	@Getter
	private String livroKey;
	
	private TodoLivroEnum(String enumName, String livroKey) {
		this.livroEnum = LivroEnum.valueOf(enumName);
		this.livroKey = livroKey;
	}
	
}
