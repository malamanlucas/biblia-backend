package br.com.biblia.apps.expressao;

import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.ExpressaoKey;
import br.com.biblia.model.versiculo.VersiculoKey;

public interface ExpressaoApp {

	Expressao save(Expressao expressao);
	
	Expressao findByKeyAndInicioAndFim(VersiculoKey versiculoKey, Integer inicio, Integer fim);
	
	void delete(ExpressaoKey expressaoKey);
	
}
