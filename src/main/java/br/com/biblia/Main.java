package br.com.biblia;

import java.util.Arrays;

import br.com.biblia.enums.LivroEnum;

public class Main {

	public static void main(String[] args) {
//		String termo = "Co 10.1";
//		System.out.println(needSearchByCordenada(termo));
//		Arrays.asList(LivroEnum.values()).forEach(t -> System.out.println(t.getSiglaEmPortugues()));
		System.out.println("abc".substring(0, "abc".length()));
	}
	
	static boolean needSearchByCordenada(String termo) {
		return Character.isDigit(termo.charAt(0)) || 
				LivroEnum.fromSiglaPortugues(termo.substring(0, termo.indexOf(' '))) != null;
	}
	
}
