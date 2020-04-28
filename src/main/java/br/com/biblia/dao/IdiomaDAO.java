package br.com.biblia.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.biblia.model.Idioma;

public interface IdiomaDAO extends JpaRepository<Idioma, Integer> {
	
	@Query(nativeQuery=true, value="SELECT COALESCE(MAX(id),0)+1 FROM idioma")
	Integer retrieveNextId();
	
	public Idioma findByNome(String nome);

}
