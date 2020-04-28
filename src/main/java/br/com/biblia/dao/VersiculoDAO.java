package br.com.biblia.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;

public interface VersiculoDAO extends JpaRepository<Versiculo, VersiculoKey> {

	@Query(nativeQuery=true, value="SELECT COALESCE(MAX(id),0)+1 FROM versiculo")
	Integer retrieveNextId();
	
	@Query(nativeQuery=true, value="SELECT COALESCE(MAX(numero),0)+1 FROM versiculo WHERE livro_id = :#{#key.livroId}"
	        + " AND capitulo_id = :#{#key.capituloId} AND versao_id = :#{#key.versaoId}" )
	Integer retrieveNextVerso(@Param("key") VersiculoKey key);
	
	@Query("SELECT COUNT(p) FROM Versiculo p WHERE p.key.versaoId = :versaoId AND LOWER(p.texto) LIKE :#{'%' + #termo + '%'}")
	Integer getOcorrenciasTermo(@Param("termo") String termo, @Param("versaoId") Integer versaoId);
	
	@Query("SELECT p FROM Versiculo p WHERE p.key.livroId = :#{#key.livroId} "
	        + "AND p.key.capituloId = :#{#key.id} "
	        + "AND p.key.versaoId = :#{#key.versaoId}"
	        + " ORDER BY id ASC")
	List<Versiculo> search(@Param("key") CapituloKey key);

	@Query("SELECT COUNT(p) FROM Versiculo p WHERE p.key.livroId = :#{#livroId} AND p.key.versaoId = :#{#versaoId}")
	Integer countByVersaoAndLivro(@Param("versaoId") Integer versaoId, @Param("livroId") Integer livroId);

	@Query("SELECT COUNT(p) FROM Versiculo p WHERE p.key.livroId = :#{#livroId} "
			+ "AND p.key.versaoId = :#{#versaoId} AND p.key.capituloId = :#{#capituloId}")
	Integer countByCapituloAndVersaoAndLivro(@Param("capituloId") Integer capituloId,
			@Param("versaoId") Integer versaoId, @Param("livroId") Integer livroId);
	
	@Transactional
	@Modifying
	@Query(nativeQuery=true, value="DELETE FROM versiculo WHERE id = :#{#key.id} "
	        + "AND capitulo_id = :#{#key.capituloId} AND livro_id = :#{#key.livroId}")
	void deleteByKey(@Param("key") VersiculoKey key);
	
	
}
