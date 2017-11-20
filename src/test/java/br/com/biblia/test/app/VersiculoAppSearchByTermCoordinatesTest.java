package br.com.biblia.test.app;

import java.util.List;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.biblia.Application;
import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.model.Sentenca;
import br.com.biblia.test.base.VersiculoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class VersiculoAppSearchByTermCoordinatesTest extends VersiculoBaseTest {

	@Autowired
	VersiculoApp app;
	
	@Autowired
	VersiculoDAO dao;
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCoordenates("Mt");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(sentencasFounded.size(), getAllVersiculosMateus().size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1() {
		System.out.println(getMateus1_10ao13().getVersiculos().size());
//		List<Sentenca> sentencasFounded = app.searchSentencasByCoordenates("Mt 1");
//		Assert.assertNotNull(sentencasFounded);
//		Assert.assertEquals(sentencasFounded.size(), );
	}
	
	
}