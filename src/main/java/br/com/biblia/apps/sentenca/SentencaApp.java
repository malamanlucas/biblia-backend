package br.com.biblia.apps.sentenca;

import java.util.List;

import br.com.biblia.model.sentenca.Sentenca;

public interface SentencaApp {

	List<Sentenca> searchSentencasByTermo(String termo, Boolean ignoreCase, Boolean ignoreAccent, List<Integer> versoes);
	List<Sentenca> searchSentencasByCordenada(String termo,  List<Integer> versoes);
	
}
