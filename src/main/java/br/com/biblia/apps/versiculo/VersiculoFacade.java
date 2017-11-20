package br.com.biblia.apps.versiculo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.google.common.base.Preconditions;

import br.com.biblia.apps.dicionario.DicionarioApp;
import br.com.biblia.dao.SentencaDAO;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.Idioma;
import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.Sentenca;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class VersiculoFacade implements VersiculoApp {

	@Autowired
	private VersiculoDAO dao;
	
	@Autowired
	private DicionarioApp dicionarioApp;
	
	@Autowired
	private SentencaDAO sentencaDAO;

	public Versiculo save(Versiculo entity) {
		VersiculoKey key = entity.getKey();
		if ( key.getId() == null ) {
			key.setId( dao.retrieveNextId() );
			entity.setNumero( dao.retrieveNextVerso( key ));
		}
		dao.save( entity );
		return entity;
	}

	private String limpar(String texto) {
		return texto.replaceAll("[\\[|\\]|0-9|=|*]", "");
	}

	public String formata(String text, Idioma idioma) {
		StringBuilder a = new StringBuilder( text );
		
		StringBuilder nova = new StringBuilder(); 
		
		while ( a.indexOf("[") != -1 ) {
			int start = a.indexOf("[");
			nova.append( a.substring(0, start) );
			a.delete(0, start);
			int end = a.indexOf("]");
			String codigo = a.substring(end+1, a.indexOf("="));
			
			dicionarioApp.createDefaultIfNotExists( Integer.valueOf(codigo), idioma );
			
			nova.append( String.format("<span class=\"texto\" dic=\"%s\">", codigo) );
			nova.append( a.substring(1, end) );
			nova.append( "</span>" );
			a.delete(0, 1);
			a.delete(0, a.indexOf("]"));
			a.delete(0, a.indexOf("=")+1);
		}
		nova.append( a );
		return nova.toString();
	}

	public List<Versiculo> findAll() {
		return dao.findAll();
	}

	public void deleteByKey(VersiculoKey key) {
		dao.delete(key);
	}

	@Override
	public List<Versiculo> search(CapituloKey key) {
		return dao.search(key);
	}

	@Override
	public void salvarExpressao(Expressao expressao) {
		
	}

	@Override
	public Integer qtdOcorrenciasTermo(String termo) {
		return dao.getOcorrenciasTermo(termo);
	}

	@Override
	public List<Sentenca> searchSentencasByTermo(String termo) {
		return sentencaDAO.searchByTermo(termo);
	}

	@Override
	public Versiculo findOne(VersiculoKey key) {
		Preconditions.checkNotNull(key);
		Preconditions.checkNotNull(key.getId());
		Preconditions.checkNotNull(key.getCapituloId());
		Preconditions.checkNotNull(key.getLivroId());
		Versiculo result = dao.findOne(key);
		Preconditions.checkNotNull(result);
		return result;
	}

	@Override
	public List<Sentenca> searchSentencasByCoordenates(String termo) {
		return null;
	}
	
}
