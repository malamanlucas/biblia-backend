package br.com.biblia.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Builder @EqualsAndHashCode
@Data
@Entity(name="idioma")
@ToString(of={"id","nome"}, includeFieldNames=false)
public class Idioma {

	@Id
	private Integer id;
	
	private String nome;
	
}
