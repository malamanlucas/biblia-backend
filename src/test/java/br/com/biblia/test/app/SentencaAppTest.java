package br.com.biblia.test.app;

import java.util.List;

import javax.swing.JOptionPane;
import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import br.com.biblia.Application;
import br.com.biblia.apps.sentenca.SentencaApp;
import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.model.sentenca.Sentenca;
import br.com.biblia.test.base.VersiculoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class SentencaAppTest extends VersiculoBaseTest {

	@Autowired
	SentencaApp app;
	
	@Test
	public void testSearchSentencasByTermo() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("trombeta");
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(74, lstSentenca.size());
	}
	
	@Test
	public void testSearchSentencasByTermoWhenIsUppercase() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("TrOmBetA");
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(74, lstSentenca.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(getAllVersiculosMateus().size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(getMateus1().getVersiculos().size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1_10ao13() {
		System.out.println(getMateus1().getVersiculos().size());
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1.10-13");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(getMateus1_10ao13().getVersiculos().size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1_2() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1.2");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(1, sentencasFounded.size());
		Assert.assertEquals(getMateus1_2().getTexto(), sentencasFounded.get(0).getTexto());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDe1Corintios1_4() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("1 Co 1.4");
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(get1Corintios1_4().getVersiculos().size(), sentencasFounded.size());
		Assert.assertEquals(get1Corintios1_4().getVersiculos().get(0).getTexto(), sentencasFounded.get(0).getTexto());
	}
	
	
}
