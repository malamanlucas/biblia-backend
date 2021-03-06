package br.com.biblia.model.versiculo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor @EqualsAndHashCode @Builder
@ToString(of={"id","livroId", "capituloId", "versaoId"}, includeFieldNames=false)
public class VersiculoKey implements Serializable {
	
	private Integer id;
	
	@Column(name="capitulo_id")
	private Integer capituloId;
	
	@Column(name="livro_id")
	private Integer livroId;
	
	@Column(name="versao_id")
	private Integer versaoId;

	public String getJson() throws JsonProcessingException {
		VersiculoKeyAsJson object = new VersiculoKeyAsJson(id, capituloId, livroId, versaoId);
		return new ObjectMapper().writer().writeValueAsString(object);
	}

	@Data @AllArgsConstructor @NoArgsConstructor
	class VersiculoKeyAsJson { 
		private Integer id;
		private Integer capituloId;
		private Integer livroId;
		private Integer versaoId;
	}
	
}
