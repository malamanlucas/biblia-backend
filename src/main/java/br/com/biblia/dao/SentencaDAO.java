package br.com.biblia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.biblia.model.sentenca.Cordenada;
import br.com.biblia.model.sentenca.Sentenca;

public interface SentencaDAO extends JpaRepository<Sentenca, Integer> {
	
	@Query("SELECT s FROM Sentenca s WHERE s.versaoId IN (:versoes) AND "
			+ "s.texto LIKE :#{'%' + #termo + '%'}"
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByTermoWithCaseSensitive(@Param("termo") String termo, @Param("versoes") List<Integer> versoes);

	@Query("SELECT s FROM Sentenca s WHERE s.versaoId IN (:versoes) AND "
			+ "unaccent(s.texto) LIKE unaccent(:#{'%' + #termo + '%'})"
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByTermoWithCaseSensitiveAndAccent(@Param("termo") String termo, @Param("versoes") List<Integer> versoes);
	
	@Query("SELECT s FROM Sentenca s WHERE s.versaoId IN (:versoes) AND "
			+ "LOWER(s.texto) LIKE LOWER(:#{'%' + #termo + '%'})"
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByTermoWithCaseInsensitive(@Param("termo") String termo, @Param("versoes") List<Integer> versoes);
	
	@Query("SELECT s FROM Sentenca s WHERE s.versaoId IN (:versoes) AND "
			+ "unaccent(LOWER(s.texto)) LIKE unaccent(LOWER(:#{'%' + #termo + '%'}))"
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByTermoWithCaseInsensitiveAndAccent(@Param("termo") String termo, @Param("versoes") List<Integer> versoes);

	@Query("SELECT s FROM Sentenca s"
			+ " WHERE "
			+ "(s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} "
			+ "AND :#{#cordenada.capitulos.start} = 0) AND s.versaoId IN (:#{#cordenada.versoes}) "
			+ "OR "
			+ "(s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} AND :#{#cordenada.capitulos.start} != 0 AND "
			+ ":#{#cordenada.versiculos.start} = 0 AND "
			+ "s.capitulo BETWEEN :#{#cordenada.capitulos.start} AND :#{#cordenada.capitulos.end} AND s.versaoId IN (:#{#cordenada.versoes}) ) "
			+ "OR "
			+ "(s.sigla = :#{#cordenada.livroEnum.siglaEmPortugues} AND :#{#cordenada.capitulos.start} != 0 AND "
			+ ":#{#cordenada.versiculos.start} != 0 AND "
			+ "s.capitulo BETWEEN :#{#cordenada.capitulos.start} AND :#{#cordenada.capitulos.end} AND "
			+ "s.versiculo BETWEEN :#{#cordenada.versiculos.start} AND :#{#cordenada.versiculos.end} AND s.versaoId IN (:#{#cordenada.versoes}) ) "
			+ " ORDER BY s.testamento DESC, s.ordemLivro, s.capitulo, s.versiculo")
	List<Sentenca> searchByCordenada(@Param("cordenada") Cordenada cordenada);
	
}
