package br.com.biblia.apps.dicionario;

import java.util.List;

import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.Dicionario;
import br.com.biblia.model.DicionarioKey;

public interface DicionarioApp {

	Dicionario save(Dicionario entity);

	List<Dicionario> search(IdiomaEnum idioma);

	Dicionario findOne(DicionarioKey key);
	
	void deleteById(DicionarioKey key);
	
	void createDefaultIfNotExists(Integer codigo, IdiomaEnum idioma);
	
}
