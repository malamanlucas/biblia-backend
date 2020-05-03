package br.com.biblia.test.app;

import java.util.List;
import java.util.stream.Collectors;

import javax.transaction.Transactional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.google.common.collect.Lists;

import br.com.biblia.Application;
import br.com.biblia.apps.sentenca.SentencaApp;
import br.com.biblia.model.sentenca.Sentenca;
import br.com.biblia.test.base.VersiculoBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class SentencaAppTest extends VersiculoBaseTest {

	private static final List<Integer> VERSAO_ID_FOR_TEST = Lists.newArrayList(1);
	@Autowired
	SentencaApp app;
	
	@Test
	public void testSearchSentencasByVersiculoKeyAndVersoes() {
		List<Integer> versoesID = Lists.newArrayList(2, 4, 8, 12);
		Integer livroId = 4;
		Integer capituloId = 1;
		Integer versiculoId = 1;
		List<Sentenca> sentencas = app.findByLivroCapituloAndVersiculoAndVersoes(versoesID, livroId, capituloId, versiculoId);
		Assert.assertEquals(4, sentencas.size());
		Assert.assertEquals(Lists.newArrayList("ACF", "AA", "NVT", "BKJ1611"), sentencas.stream().map(s -> s.getVersao()).collect(Collectors.toList()));
		String acfText = "No princípio era o Verbo, e o Verbo estava com Deus, e o Verbo era Deus.";
		String aaText = "No princípio era o Verbo, e o Verbo estava com Deus, e o Verbo era Deus.";
		String nvtText = "No princípio, aquele que é a Palavra já existia.";
		String bkjText = "No princípio era a Palavra, e a Palavra estava com Deus, e a Palavra era Deus.";
		Assert.assertTrue(sentencas.stream().anyMatch(s -> acfText.equals(s.getTexto())));
		Assert.assertTrue(sentencas.stream().anyMatch(s -> aaText.equals(s.getTexto())));
		Assert.assertTrue(sentencas.stream().anyMatch(s -> nvtText.equals(s.getTexto())));
		Assert.assertTrue(sentencas.stream().anyMatch(s -> bkjText.equals(s.getTexto())));
		
	}
	
	@Test
	public void testSearchSentencasByTermo() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("trombeta", true, false, VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(81, lstSentenca.size());
	}
	
	@Test
	public void testSearchSentencasByTermoWhenIsUppercase() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("TrOmBetA", true, false, VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(81, lstSentenca.size());
	}

	@Test
	public void testSearchSentencasByTermoIgnoringAccentWhenIsUppercaseAndHasAccent() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("ASSÍRIA", false, true, VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(0, lstSentenca.size());
	}

	@Test
	public void testSearchSentencasByTermoIgnoringAccentAndCaseSensitiveWhenIsUppercaseAndHasAccent() {
		List<Sentenca> lstSentenca = app.searchSentencasByTermo("ASSÍRIA", true, true, VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(lstSentenca);
		Assert.assertEquals(127, lstSentenca.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt", VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(getAllVersiculosMateus(VERSAO_ID_FOR_TEST).size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1", VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(sentencasFounded);
		System.out.println(sentencasFounded.size());
		Assert.assertEquals(getMateus1(VERSAO_ID_FOR_TEST).getVersiculos().size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1_10ao13() {
		System.out.println(getMateus1(VERSAO_ID_FOR_TEST).getVersiculos().size());
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1.10-13", VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(getMateus1_10ao13(VERSAO_ID_FOR_TEST).getVersiculos().size(), sentencasFounded.size());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDeMateus1_2() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("Mt 1.2", VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(1, sentencasFounded.size());
		Assert.assertEquals(getMateus1_2().getTexto(), sentencasFounded.get(0).getTexto());
	}
	
	@Test
	public void deveTrazerTodosOsVersiculosDe1Corintios1_4() {
		List<Sentenca> sentencasFounded = app.searchSentencasByCordenada("1 Co 1.4", VERSAO_ID_FOR_TEST);
		Assert.assertNotNull(sentencasFounded);
		Assert.assertEquals(get1Corintios1_4(VERSAO_ID_FOR_TEST).getVersiculos().size(), sentencasFounded.size());
		Assert.assertEquals(get1Corintios1_4(VERSAO_ID_FOR_TEST).getVersiculos().get(0).getTexto(), sentencasFounded.get(0).getTexto());
	}
	
	
}
