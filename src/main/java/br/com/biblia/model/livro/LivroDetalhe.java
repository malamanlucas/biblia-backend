package br.com.biblia.model.livro;

import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="livro_detalhe")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LivroDetalhe {
	
	@Id @Column(name="livro_id")
	private Integer livroId;
	
	@Column(name="data_criacao", columnDefinition="DATE")
	private LocalDate dataCriacao;
	
	private String autor;
	
	@Column(name="data_local")
	private String dataLocal;
	
	private String tema;
	
	@JsonIgnore
	@OneToOne
	@JoinColumn(name="livro_id", referencedColumnName="id", insertable=false, updatable=false)
	private Livro livro;
	
}
