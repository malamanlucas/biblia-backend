package br.com.biblia.apps.expressao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import br.com.biblia.dao.ExpressaoDAO;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.ExpressaoKey;
import br.com.biblia.model.versiculo.VersiculoKey;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
@Transactional(propagation=Propagation.REQUIRED)
public class ExpressaoFacade implements ExpressaoApp {

	@Autowired
	private ExpressaoDAO dao;
	
	@Override
	public Expressao save(Expressao expressao) {
		
		if ( CollectionUtils.isEmpty(expressao.getDicionarios()) && CollectionUtils.isEmpty(expressao.getMapas()) ) {
			VersiculoKey versiculoKey = new VersiculoKey(expressao.getKey().getVersiculoId(), expressao.getKey().getCapituloId(), expressao.getKey().getLivroId());
			dao.deleteByKeyAndInicioAndFim(versiculoKey, expressao.getInicio(), expressao.getFim());
			return expressao;
		} else { 
			if (expressao.getKey().getExpressaoId() == null) {
				expressao.getKey().setExpressaoId( dao.retrieveNextExpressaoId(expressao.getKey()) );
			}
			
			if ( expressao.getDicionarios() != null ) {
				expressao.getDicionarios().forEach( t -> t.getKey().setExpressaoKey( expressao.getKey() ) );
			} else {
				expressao.setDicionarios(Lists.newArrayList());
			}
			
			if ( expressao.getMapas() != null ) {
				expressao.getMapas().forEach( t -> t.getKey().setExpressaoKey( expressao.getKey() ) );
			} else {
				expressao.setMapas(Lists.newArrayList());
			}
			
			return dao.save(expressao);
		}
		
	}

	@Override
	public Expressao findByKeyAndInicioAndFim(VersiculoKey versiculoKey, Integer inicio, Integer fim) {
		Expressao result = dao.findByKeyAndInicioAndFim(versiculoKey, inicio, fim);
		Preconditions.checkNotNull(result);
		return result;
	}

	@Override
	public void delete(ExpressaoKey expressaoKey) {
		Preconditions.checkNotNull(expressaoKey);
		dao.delete(expressaoKey);
	}

	
}
