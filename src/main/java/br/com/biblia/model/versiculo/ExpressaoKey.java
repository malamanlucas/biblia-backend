package br.com.biblia.model.versiculo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

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
@ToString(of={"expressaoId","versiculoId", "livroId", "capituloId"}, includeFieldNames=false)
public class ExpressaoKey implements Serializable {

	@NotNull
    @Column(name="expressao_id")
	private Integer expressaoId;
	
	@NotNull
	@Column(name="versiculo_id")
	private Integer versiculoId;
	
	@NotNull
	@Column(name="capitulo_id")
	private Integer capituloId;
	
	@NotNull
	@Column(name="livro_id")
	private Integer livroId;
	
}
