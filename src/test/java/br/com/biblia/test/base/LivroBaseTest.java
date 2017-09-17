package br.com.biblia.test.base;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.biblia.dao.LivroDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.enums.Testamento;
import br.com.biblia.model.livro.Livro;

public class LivroBaseTest {

	@Autowired
	LivroDAO dao;
	
	protected Livro getMateus() {
		return dao.searchByTestamento(Testamento.NOVO)
			.stream()
			.filter(t -> t.getSigla().equals(LivroEnum.MATEUS.getSiglaEmPortugues()))
			.findAny().get();
			
	}
	
}
