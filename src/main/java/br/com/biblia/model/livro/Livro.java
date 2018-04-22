package br.com.biblia.model.livro;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.google.common.collect.Lists;

import br.com.biblia.enums.Testamento;
import br.com.biblia.model.Capitulo;
import br.com.biblia.model.versiculo.Versiculo;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity @Builder
@Table(name = "livro")
@Data
@AllArgsConstructor
@NoArgsConstructor(force=true)
@EqualsAndHashCode
@ToString(of={"id","nome","ordem","testamento"})
@JsonIgnoreProperties("capitulos")
public class Livro {
	
	public Livro(Integer id) {
		this.id = id;
	}

	@Id
	private Integer id;
	private String nome;
	
	@Enumerated(EnumType.STRING)
	private Testamento testamento;
	
	private Integer ordem;
	
	private String sigla;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY, mappedBy="livro")
	private List<Capitulo> capitulos;
	
	@Transient
	private List<Versiculo> versiculos = Lists.newArrayList();
		
}
