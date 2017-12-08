package br.com.biblia.test.base;

import java.util.List;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

import br.com.biblia.dao.LivroDAO;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.Capitulo;
import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.livro.Livro;
import br.com.biblia.model.versiculo.Versiculo;

@Sql(executionPhase=ExecutionPhase.BEFORE_TEST_METHOD, statements="DELETE FROM expressao")
public abstract class VersiculoBaseTest {
	
	@Autowired
    private LivroDAO livroDAO;
	
	@Autowired
    private VersiculoDAO versiculoDAO;
	
	@PersistenceContext
	private EntityManager entityManager;
	
	
	protected List<Versiculo> getAllVersiculosMateus() {
		return getMateus().getVersiculos();
	}
	
	protected List<Capitulo> getAllCapitulosMateus() {
		return getMateus().getCapitulos();
	}

	private Livro getMateus() {
		Livro mateus = livroDAO.findByNome(LivroEnum.MATEUS.getNomeNoBD());
		mateus.getCapitulos().sort((a,b) -> a.getKey().getId() > b.getKey().getId() ? 1 : -1);
		mateus.getCapitulos().forEach(c -> {
			c.setVersiculos(versiculoDAO.search(c.getKey()));
			mateus.getVersiculos().addAll(versiculoDAO.search(c.getKey()));
		});
		return mateus;
	}
	
	private Livro get1Corintios() {
		Livro mateus = livroDAO.findByNome(LivroEnum.PRIMEIRA_CORINTIOS.getNomeNoBD());
		mateus.getCapitulos().sort((a,b) -> a.getKey().getId() > b.getKey().getId() ? 1 : -1);
		mateus.getCapitulos().forEach(c -> {
			c.setVersiculos(versiculoDAO.search(c.getKey()));
			mateus.getVersiculos().addAll(versiculoDAO.search(c.getKey()));
		});
		return mateus;
	}
	
	protected Capitulo get1Corintios1_4() {
		Capitulo capitulo = this.get1Corintios().getCapitulos().get(0);
		List<Versiculo> verisculosFiltered = capitulo.getVersiculos().stream().filter(v -> {
			Integer id = v.getKey().getId();
			return id == 4;
		}).collect(Collectors.toList());
		capitulo.setVersiculos(verisculosFiltered);
		return capitulo;
	}
	
	protected Capitulo getMateus1() {
		return this.getMateus().getCapitulos().get(0);
	}
	
	protected Capitulo getMateus1_10ao13() {
		Capitulo capitulo = this.getMateus().getCapitulos().get(0);
		List<Versiculo> verisculosFiltered = capitulo.getVersiculos().stream().filter(v -> {
			Integer id = v.getKey().getId();
			return id >= 10 && id <= 13;
		}).collect(Collectors.toList());
		capitulo.setVersiculos(verisculosFiltered);
		return capitulo;
	}
	
	protected Versiculo getMateus1_1() {
		Livro mateus = livroDAO.findByNome(LivroEnum.MATEUS.getNomeNoBD());
        
		Versiculo mateus1_1 = versiculoDAO.search(new CapituloKey(1, mateus.getId())).get(0);
        mateus1_1 = versiculoDAO.getOne(mateus1_1.getKey());
        entityManager.detach(mateus1_1);
        Assert.assertNotNull(mateus1_1);
        Assert.assertNotNull(mateus1_1.getKey().getId());
		return mateus1_1;
	}
	
	protected Versiculo getMateus1_2() {
		Livro mateus = livroDAO.findByNome(LivroEnum.MATEUS.getNomeNoBD());
        
        Versiculo mateus1_2 = versiculoDAO.search(new CapituloKey(1, mateus.getId())).get(1);
        mateus1_2 = versiculoDAO.getOne(mateus1_2.getKey());
        entityManager.detach(mateus1_2);
        Assert.assertNotNull(mateus1_2);
        Assert.assertNotNull(mateus1_2.getKey().getId());
		return mateus1_2;
	}
	
	protected Versiculo getMateus1_16() {
		Livro mateus = livroDAO.findByNome(LivroEnum.MATEUS.getNomeNoBD());
        
        Versiculo mateus1_16 = versiculoDAO.search(new CapituloKey(15, mateus.getId())).get(1);
        mateus1_16 = versiculoDAO.getOne(mateus1_16.getKey());
        entityManager.detach(mateus1_16);
        Assert.assertNotNull(mateus1_16);
        Assert.assertNotNull(mateus1_16.getKey().getId());
		return mateus1_16;
	}
	
	protected Versiculo getMateus5_6() {
		Livro mateus = livroDAO.findByNome(LivroEnum.MATEUS.getNomeNoBD());
        
        Versiculo mateus5_6 = versiculoDAO.search(new CapituloKey(5, mateus.getId())).get(5);
        mateus5_6 = versiculoDAO.getOne(mateus5_6.getKey());
        entityManager.detach(mateus5_6);
        Assert.assertNotNull(mateus5_6);
        Assert.assertNotNull(mateus5_6.getKey().getId());
		return mateus5_6;
	}
	
}
