package br.com.biblia.test.app;

import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblia.Application;
import br.com.biblia.apps.expressao.ExpressaoApp;
import br.com.biblia.dao.DicionarioDAO;
import br.com.biblia.dao.ExpressaoDAO;
import br.com.biblia.dao.MapaDAO;
import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.model.Dicionario;
import br.com.biblia.model.DicionarioKey;
import br.com.biblia.model.Mapa;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.ExpressaoDicionario;
import br.com.biblia.model.versiculo.ExpressaoKey;
import br.com.biblia.model.versiculo.ExpressaoMapa;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;
import br.com.biblia.test.base.ExpressaoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class ExpressaoAppTest extends ExpressaoBaseTest {

	@Autowired
	private ExpressaoApp app;

	@Autowired
	private ExpressaoDAO dao;
	
	@Autowired
	private DicionarioDAO dicionarioDAO;
	
	@Autowired
	private MapaDAO mapaDAO;
	
	@Test(expected=NullPointerException.class)
	public void shouldDoErrorWhenTestDeleteWhenKeyIsNull() {
		app.delete(null);
	}
	
	@Test
	public void testDeleteWhenExpressaoExists() {
		ExpressaoKey key = garantirExpressao().getKey();
		app.delete(key);
		Assert.assertNull(dao.findOne(key));
	}
	
	@Test
	public void testFindWhenExpressaoExists() {
		Expressao expressao = garantirExpressao();
		VersiculoKey versiculoKey = VersiculoKey
				.builder()
				.capituloId(expressao.getKey().getCapituloId())
				.id(expressao.getKey().getVersiculoId())
				.livroId(expressao.getKey().getLivroId())
				.versaoId(expressao.getKey().getVersaoId())
				.build();
		Expressao expressaoFinded = app.findByKeyAndInicioAndFim(versiculoKey, expressao.getInicio(), expressao.getFim());
		genericAssert(expressaoFinded);
	}
	
	@Test(expected=NullPointerException.class)
	public void shouldDoErrorWhenFindExpressaoInexistent() {
		VersiculoKey versiculoKey = VersiculoKey
				.builder()
				.capituloId(-9)
				.id(-9)
				.livroId(-9)
				.build();
		Expressao expressaoFinded = app.findByKeyAndInicioAndFim(versiculoKey, -9, -9);
		System.out.println(expressaoFinded);
	}
	
	@Test
	public void testSaveWhenDoesNotHaveAnyExpressionAndAnyDicionaryAndAnyMapa() {
		
		Versiculo mateus1_1 = getMateus1_1();
		
		Dicionario dicionario = Dicionario.builder()
												.key( DicionarioKey.builder()
																		  .id(-1234)
																		  .idioma(IdiomaEnum.GREGO)
																		  .build() )
												.build();
		Expressao expressao = addOneExpressaoDicionario( dicionario, mateus1_1.getKey() );
		Mapa mapa = Mapa.builder()
								.id(-1234)
								.build();
		addOneExpressaoMapa(mapa, expressao);
		
		expressao.getKey().setExpressaoId(null);
		
		Assert.assertNull( expressao.getKey().getExpressaoId() );
		
		app.save(expressao);
		
		Expressao expressaoInBD = dao.getOne( expressao.getKey() );
		
		Assert.assertNotNull( dicionarioDAO.findOne(dicionario.getKey()) );
		Assert.assertNotNull( mapaDAO.findOne(mapa.getId()) );
		
		genericAssert(expressaoInBD);
		
		assertDicionarios( expressao.getDicionarios() );
		assertMapas( expressao.getMapas() );
		
	}

	private void genericAssert(Expressao expressaoInBD) {
		Assert.assertNotNull(expressaoInBD);
		Assert.assertNotNull(expressaoInBD.getDescricao());
		Assert.assertNotNull(expressaoInBD.getTexto());
		Assert.assertNotNull(expressaoInBD.getFim());
		Assert.assertNotNull(expressaoInBD.getInicio());
		Assert.assertNotNull(expressaoInBD.getFim());
		
		assertExpressaoKey( expressaoInBD.getKey() );
		Assert.assertEquals( new Integer(1), expressaoInBD.getKey().getExpressaoId() );
	}
	
	@Test
	public void testSaveWhenDoesNotHaveAnyExpression() {
		
		Versiculo mateus1_1 = getMateus1_1();
		
		Expressao expressao = addOneExpressaoDicionario( saveDicionario(), mateus1_1.getKey() );
		addOneExpressaoMapa(expressao);
		
		expressao.getKey().setExpressaoId(null);
		
		Assert.assertNull( expressao.getKey().getExpressaoId() );
		
		app.save(expressao);
		
		Expressao expressaoInBD = dao.getOne( expressao.getKey() );
		
		genericAssert(expressaoInBD);
		
		assertDicionarios( expressao.getDicionarios() );
		assertMapas( expressao.getMapas() );
		
	}

	private void assertMapas(List<ExpressaoMapa> mapas) {
		Assert.assertNotNull( mapas );
		for (ExpressaoMapa mapa : mapas) {
			Assert.assertNotNull(mapa);
			Assert.assertNotNull(mapa.getKey());
			assertExpressaoKey(mapa.getKey().getExpressaoKey());
			Assert.assertNotNull(mapa.getKey().getId());
		}
	}

	private void assertDicionarios(List<ExpressaoDicionario> dicionarios) {
		Assert.assertNotNull( dicionarios );
		for (ExpressaoDicionario dicionario : dicionarios) {
			Assert.assertNotNull( dicionario );
			Assert.assertNotNull( dicionario.getKey() );
			assertExpressaoKey( dicionario.getKey().getExpressaoKey() );
			Assert.assertNotNull( dicionario.getKey().getId() );
			Assert.assertNotNull( dicionario.getKey().getIdioma() );
		}
	}

	private void assertExpressaoKey(ExpressaoKey key) {
		Assert.assertNotNull(key);
		Assert.assertNotNull(key.getCapituloId());
		Assert.assertNotNull(key.getExpressaoId());
		Assert.assertNotNull(key.getLivroId());
		Assert.assertNotNull(key.getVersiculoId());
	}
	
}
