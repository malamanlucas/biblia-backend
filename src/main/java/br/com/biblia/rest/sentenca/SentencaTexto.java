package br.com.biblia.rest.sentenca;

import br.com.biblia.model.sentenca.Sentenca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SentencaTexto {

	private String textoComVersiculo;
	private String textoLimpo;

	public SentencaTexto(Sentenca e) {
		this.texto = e.getTextoMontado();
		this.id = e.getId();
		this.capituloId = e.getCapitulo();
		this.livroId = e.getLivroId();
		this.versiculoId = e.getVersiculo();
		this.textoLimpo = e.getTextolimpo();
		this.textoComVersiculo = e.getTextoComVersiculo();
	}
	
	private String id;
	private String texto;
	private Integer capituloId;
	private Integer livroId;
	private Integer versiculoId;
	
}
