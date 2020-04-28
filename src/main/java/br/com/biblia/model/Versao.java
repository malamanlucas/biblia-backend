package br.com.biblia.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode
@Data
@Entity(name="versao")
@ToString(of={"id","abreviacao", "nome"}, includeFieldNames=false)
public class Versao {
	
	@Id
	private Integer id;
	
	@Column(name = "idioma_id")
	private Integer idiomaId;
	
	private String abreviacao;
	
	private String nome;
	
	@JsonIgnore 
	@OneToOne
	@JoinColumn(name="idioma_id", referencedColumnName="id", insertable=false, updatable=false)
	private Idioma idioma;
	
}
