package br.com.biblia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biblia.enums.Testamento;
import br.com.biblia.model.livro.Livro;

public interface LivroDAO extends JpaRepository<Livro, Integer> {

	@Query(nativeQuery=true, value="SELECT COALESCE(MAX(id),0)+1 FROM livro")
	Integer retrieveNextId();
	
	@Query("SELECT p FROM Livro p WHERE p.testamento = :testamento ORDER BY p.ordem ASC")
	List<Livro> searchByTestamento(@Param("testamento") Testamento testamento);
	
	Livro findByNome(String nome);
	
}
