package br.com.biblia.model.sentenca;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

import br.com.biblia.enums.Testamento;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "sentenca")
public class Sentenca implements Serializable {

	@Id
	@Column(name="sentenca_id")
	private String id;
	
	private String sigla;
	
	@Column(name="ordem")
	private Integer ordemLivro;
	
	@Enumerated(EnumType.STRING)
	private Testamento testamento;
	
	@Column(name="capitulo_id")
	private Integer capitulo;
	
	@Column(name="numero")
	private Integer versiculo;
	
	@Column(name="versao_id")
	private Integer versaoId;
	
	@Column(name="livro_id")
	private Integer livroId;
	
	private String texto;
	
	@Column(name="texto_montado")
	private String textoMontado;

	@Column(name="texto_limpo")
	private String textolimpo;

	@Column(name="texto_com_versiculo")
	private String textoComVersiculo;

	private String versao;
	
}
