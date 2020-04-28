package br.com.biblia.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.biblia.model.livro.LivroDetalhe;
import br.com.biblia.model.livro.LivroDetalheKey;

public interface LivroDetalheDAO extends JpaRepository<LivroDetalhe, LivroDetalheKey> {

	
}
