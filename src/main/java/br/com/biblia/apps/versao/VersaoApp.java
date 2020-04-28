package br.com.biblia.apps.versao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblia.apps.idioma.IdiomaApp;
import br.com.biblia.dao.VersaoDAO;
import br.com.biblia.model.Idioma;
import br.com.biblia.model.Versao;

@Service
public class VersaoApp {
	
	@Autowired
	private VersaoDAO dao;

	@Autowired
	private IdiomaApp idiomaApp;

	public Versao save(Versao entity, String idioma) {
		entity.setIdiomaId( idiomaApp.getOrCreateIfNotExists(idioma).getId() );
		if ( entity.getId() == null ) {
			entity.setId( dao.retrieveNextId() );
		}
		dao.save(entity);
		return entity;
	}
	
	public Versao getOrCreateIfNotExists(String nomeVersao, String abreviacao, String idioma) {
		Versao versao = dao.findByAbreviacao(abreviacao.toUpperCase());
		if (versao == null) {
			return this.save(new Versao(null, null, abreviacao.toUpperCase(), nomeVersao, null), idioma);
		}
		return versao;
	}

	public List<Versao> findAll() {
		return dao.findAll();
	}
	
}
