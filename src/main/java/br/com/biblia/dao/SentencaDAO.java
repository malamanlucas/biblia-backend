package br.com.biblia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biblia.model.sentenca.Cordenada;
import br.com.biblia.model.sentenca.Sentenca;

public interface SentencaDAO extends JpaRepository<Sentenca, Integer> {
	
	@Query("SELECT s FROM Sentenca s WHERE LOWER(s.texto) LIKE LOWER(:#{'%' + #termo + '%'})"
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByTermo(@Param("termo") String termo);
	
	@Query("SELECT s FROM Sentenca s"
			+ " WHERE (s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} "
			+ "AND :#{#cordenada.capitulos.start} = 0) "
			+ "OR "
			+ "(s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} AND :#{#cordenada.capitulos.start} != 0 AND "
			+ ":#{#cordenada.versiculos.start} = 0 AND "
			+ "s.capitulo BETWEEN :#{#cordenada.capitulos.start} AND :#{#cordenada.capitulos.end}) "
			+ "OR "
			+ "(s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} AND :#{#cordenada.capitulos.start} != 0 AND "
			+ ":#{#cordenada.versiculos.start} != 0 AND "
			+ "s.capitulo BETWEEN :#{#cordenada.capitulos.start} AND :#{#cordenada.capitulos.end} AND "
			+ "s.versiculo BETWEEN :#{#cordenada.versiculos.start} AND :#{#cordenada.versiculos.end}) "
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByCordenada(@Param("cordenada") Cordenada cordenada);
	
	
	
	
}
