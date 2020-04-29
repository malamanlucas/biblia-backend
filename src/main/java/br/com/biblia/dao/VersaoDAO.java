package br.com.biblia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import br.com.biblia.model.Versao;

public interface VersaoDAO extends JpaRepository<Versao, Integer> {
	
	@Query(nativeQuery=true, value="SELECT COALESCE(MAX(id),0)+1 FROM versao")
	Integer retrieveNextId();
	
	public Versao findByAbreviacao(String abreviacao);
	
	public List<Versao> findByOrderByIdAsc();

}
