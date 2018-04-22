package br.com.biblia.model.livro;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroDetalheKey implements Serializable {

	@Column(name="livro_id")
	private Integer livroId;
	
	@Column(name="versao_id")
	private Integer versaoId;
	
	@Column(name="capitulo_id")
	private Integer capituloId;
	
}
