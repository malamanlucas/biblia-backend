package br.com.biblia.model.livro;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="livro_detalhe")
@Data @Builder
@AllArgsConstructor
@NoArgsConstructor
public class LivroDetalhe {
	
	@EmbeddedId
	private LivroDetalheKey key;
	
}
