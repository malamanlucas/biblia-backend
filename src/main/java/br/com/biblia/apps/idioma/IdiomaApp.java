package br.com.biblia.apps.idioma;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.biblia.dao.IdiomaDAO;
import br.com.biblia.model.Idioma;

@Service
public class IdiomaApp {
	
	@Autowired
	private IdiomaDAO idiomaDao;

	public Idioma save(Idioma entity) {
		if ( entity.getId() == null ) {
			entity.setId( idiomaDao.retrieveNextId() );
		}
		idiomaDao.save(entity);
		return entity;
	}
	
	public Idioma getOrCreateIfNotExists(String language) {
		Idioma idioma = this.idiomaDao.findByNome(language);
		if (idioma == null) {
			return this.save(new Idioma(null, language));
		}
		return idioma;
	}
	
}
