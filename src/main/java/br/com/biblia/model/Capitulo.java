package br.com.biblia.model;

import java.util.List;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.common.collect.Lists;

import br.com.biblia.model.livro.Livro;
import br.com.biblia.model.versiculo.Versiculo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Table(name = "capitulo")
@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString(of="key")
public class Capitulo {

	public Capitulo(CapituloKey key) {
		this.key = key;
	}
	
	@EmbeddedId
	private CapituloKey key = new CapituloKey();
	
	private String titulo;
	
	@Transient
	List<Versiculo> versiculos = Lists.newArrayList();
	
}
