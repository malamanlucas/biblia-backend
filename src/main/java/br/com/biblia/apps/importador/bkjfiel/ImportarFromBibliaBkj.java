package br.com.biblia.apps.importador.bkjfiel;

import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.biblia.apps.versao.VersaoApp;
import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.CapituloDAO;
import br.com.biblia.dao.LivroDAO;
import br.com.biblia.dao.LivroDetalheDAO;
import br.com.biblia.dao.VersaoDAO;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.Capitulo;
import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.Versao;
import br.com.biblia.model.livro.Livro;
import br.com.biblia.model.livro.LivroDetalhe;
import br.com.biblia.model.livro.LivroDetalheKey;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class ImportarFromBibliaBkj {
	
	private static final String PORTUGUES = "Português";

	private static final int VERSAO_ACF_FOR_EXISTENT_CAPITULO = 1;

	@Autowired
	VersiculoApp app;
	
	@Autowired
	private VersiculoDAO versiculoDAO;
	
	@Autowired
	private LivroDAO livroDAO;
	
	@Autowired
	private CapituloDAO capituloDAO;
	
	@Autowired
	private LivroDetalheDAO livroDetalheDAO;
	
	@Autowired
	private VersaoApp versaoApp;
	
	@Autowired
	private GetVerseExtractedBkj getVerseExtracted;
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class VerseExtracted {
		private Integer number;
		private String text;
	}
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class BibliaToImport {
		String nomeVersao;
		String abreviacao;
		String idioma;
	}
	
	public void executar() {
		List<BibliaToImport> lst = Lists.newArrayList();
		
		lst.add(new BibliaToImport("Biblia King James (BKJ1611)", "bkj1611", PORTUGUES));
		lst.forEach(b -> {
			exec(b.getNomeVersao(), b.getAbreviacao(), b.getIdioma());
		});
	}


	private void exec(String nomeVersao, String abreviacao, String idioma) {
		Versao versao = this.versaoApp.getOrCreateIfNotExists(nomeVersao, abreviacao, idioma);
		
		for (BkjLivroEnum bkjLivroEnum : BkjLivroEnum.values()) {
			LivroEnum livroEnum = bkjLivroEnum.getLivroEnum();
			Livro livro = livroDAO.findByNome(livroEnum.getNomeNoBD());
			
			if (areVersesCreated(bkjLivroEnum, versao, livro)) {
				System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), " já está criado"));
				continue;
			} 
			System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), " Cadastrando..."));
			for (int capitulo = 1; capitulo <= livroEnum.getQtdCapitulo(); capitulo++) {
				Integer capituloId = capitulo; 
				 if (areVersesCreatedAtThisCharpter(versao, livro, capituloId)) {
					System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), "-", capituloId, " já está criado"));
					continue;
				}
				System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), "-", capituloId, " cadastrando..."));
				List<VerseExtracted> versesExtracted = getVerseExtracted.extract(bkjLivroEnum.getLivroKey(), capituloId);
				
				createLivroDetalhesIfNotExists(capituloId, livro, versao);
				Capitulo capituloEntity = createCapituloIfNotExists(capituloId, livro, versao);
				
				List<Versiculo> verses = Lists.newArrayList();
				versesExtracted.forEach(ve -> {
					Versiculo v = new Versiculo();
					v.setIdioma(livroEnum.getIdioma());
					v.setNumero(ve.getNumber());
					v.setTexto(ve.getText());
					v.setLimpo(v.getTexto());
					v.setFormatado(v.getTexto());
					v.setQtdAumentado(0);
					VersiculoKey key = VersiculoKey.builder()
							.capituloId(capituloEntity.getKey().getId())
							.id(v.getNumero())
							.livroId(livro.getId())
							.versaoId(versao.getId())
							.build();
					v.setKey(key);
					verses.add(v);
				});
				versiculoDAO.saveAll(verses);
			}
		}
	}


	private boolean areVersesCreatedAtThisCharpter(Versao versao, Livro livro, Integer capituloId) {
		return this.versiculoDAO.countByCapituloAndVersaoAndLivro(capituloId, versao.getId(), livro.getId())
				> 1;
	}

	private boolean areVersesCreated(BkjLivroEnum nviLivroEnum, Versao versao, Livro livro) {
		return this.versiculoDAO.countByVersaoAndLivro(versao.getId(), livro.getId())
						.equals( nviLivroEnum.getLivroEnum().getQtdCapitulo() );
	}

	private void createLivroDetalhesIfNotExists(Integer capituloId, Livro livro, Versao versao) {
		Integer livroId = livro.getId();
		Integer versaoId = versao.getId();
		LivroDetalheKey key = new LivroDetalheKey(livroId, versaoId, capituloId);
		Optional<LivroDetalhe> findById = livroDetalheDAO.findById(key);
		if (!findById.isPresent()) {
			livroDetalheDAO.save(new LivroDetalhe(key));
		}
	}

	private Capitulo createCapituloIfNotExists(Integer capituloId, Livro livro, Versao versao) {
		CapituloKey capituloKey = new CapituloKey(capituloId, livro.getId(), versao.getId());
		
		Optional<Capitulo> findById = capituloDAO.findById(capituloKey);
		if (!findById.isPresent()) {
			Capitulo capitulo = new Capitulo( capituloKey );
			CapituloKey existentCapituloKey = new CapituloKey(capituloKey.getId(), capituloKey.getLivroId(), VERSAO_ACF_FOR_EXISTENT_CAPITULO);
			capitulo.setTitulo( capituloDAO.findById(existentCapituloKey).get().getTitulo() );
			capituloDAO.save(capitulo);
			return capitulo;
		}
		return findById.get();
	}

	@Autowired
	VersaoDAO versaoDAO;

}
