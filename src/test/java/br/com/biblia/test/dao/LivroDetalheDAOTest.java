package br.com.biblia.test.dao;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import br.com.biblia.Application;
import br.com.biblia.dao.LivroDetalheDAO;
import br.com.biblia.model.livro.LivroDetalhe;
import br.com.biblia.test.base.LivroBaseTest;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=Application.class, webEnvironment=WebEnvironment.MOCK)
@Transactional
@Rollback
public class LivroDetalheDAOTest extends LivroBaseTest {

	@Autowired
	LivroDetalheDAO dao;
	
	@Test
	public void testSaveWhenDoesNotExists() {
		Integer key = getMateus().getId();
		LivroDetalhe findOne = dao.findOne(key);
		Assert.assertNull(findOne);
		LivroDetalhe detalhe = LivroDetalhe
								.builder()
								.autor("a")
								.dataCriacao(LocalDate.now())
								.dataLocal("qwewqewqeqw")
								.livroId(key)
								.tema("qweqw")
								.build();
		dao.save(detalhe);
		LivroDetalhe finded = dao.findOne(key);
		Assert.assertNotNull(finded);
		Assert.assertEquals(finded.getAutor(), detalhe.getAutor());
		Assert.assertEquals(finded.getDataLocal(), detalhe.getDataLocal());
		Assert.assertEquals(finded.getTema(), detalhe.getTema());
		Assert.assertEquals(finded.getDataCriacao(), detalhe.getDataCriacao());
		Assert.assertEquals(finded.getLivroId(), detalhe.getLivroId());
	}
	
}
