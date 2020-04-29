package br.com.biblia.apps.importador;

import java.net.URI;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.CapituloDAO;
import br.com.biblia.dao.LivroDAO;
import br.com.biblia.dao.LivroDetalheDAO;
import br.com.biblia.dao.VersaoDAO;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.Capitulo;
import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.Idioma;
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
public class ImportarACF {
	
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
	
	@Data
	@AllArgsConstructor
	@NoArgsConstructor
	static class VerseExtracted {
		private Integer number;
		private String text;
	}
	
	public void executar() {
		Versao versao = createVersaoIfNoExists();
		
//		for (AcfLivroEnum acfLivroEnum : new AcfLivroEnum[] { AcfLivroEnum.JUDAS }) {
		for (AcfLivroEnum acfLivroEnum : AcfLivroEnum.values()) {
			LivroEnum livroEnum = acfLivroEnum.getLivroEnum();
			Livro livro = livroDAO.findByNome(livroEnum.getNomeNoBD());
			
			if (areVersesCreated(acfLivroEnum, versao, livro)) {
				System.out.println(StringUtils.join(livro.getNome(), " já está criado"));
				continue;
			} 
			System.out.println(StringUtils.join(livro.getNome(), " Cadastrando..."));
			for (int capitulo = 1; capitulo <= livroEnum.getQtdCapitulo(); capitulo++) {
				Integer capituloId = capitulo; 
				Integer livroNumber = acfLivroEnum.getLivroNumber();
				 if (areVersesCreatedAtThisCharpter(versao, livro, capituloId)) {
					System.out.println(StringUtils.join(livro.getNome(), "-", capituloId, " já está criado"));
					continue;
				}
				System.out.println(StringUtils.join(livro.getNome(), "-", capituloId, " cadastrando..."));
				String urlFormatted = String.format("https://biblias.com.br/acfonline-versos?livro=%d&capitulo=%d", livroNumber, capitulo);
				List<VerseExtracted> versesExtracted = extract(urlFormatted);
				
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
							.id(ve.getNumber())
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


	public <T> Iterable<T> 
    getIterableFromIterator(Iterator<T> iterator) 
    { 
        return new Iterable<T>() { 
            @Override
            public Iterator<T> iterator() 
            { 
                return iterator; 
            } 
        }; 
    } 
  
	
	private boolean areVersesCreated(AcfLivroEnum acfLivroEnum, Versao versao, Livro livro) {
		return this.versiculoDAO.countByVersaoAndLivro(versao.getId(), livro.getId())
						.equals( acfLivroEnum.getLivroEnum().getQtdCapitulo() );
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
			CapituloKey existentCapituloKey = new CapituloKey(capituloKey.getId(), capituloKey.getLivroId(), 1);
			capitulo.setTitulo( capituloDAO.findById(existentCapituloKey).get().getTitulo() );
			capituloDAO.save(capitulo);
			return capitulo;
		}
		return findById.get();
	}

	@Autowired
	VersaoDAO versaoDAO;

	private Versao createVersaoIfNoExists() {
		Optional<Versao> findById = versaoDAO.findById(2);
		
		if (!findById.isPresent()) {
			Idioma portugues = new Idioma(1, "Português");
			Versao versao = new Versao(2, portugues.getId(), "ACF", "Almeida Corrigida Fiel", portugues, null);
			versaoDAO.save(versao);
			return versao;
		}
		
		return findById.get();
		
	}
//	public static void main(String[] args) throws Exception {

	private List<VerseExtracted> extract(String urlFormatted) {
		List<VerseExtracted> lst = Lists.newArrayList();
		Document doc = null;
		doc = retrieveDoc(urlFormatted);
		
		Elements elements = doc.getElementsByTag("div");
		
		elements.forEach(e -> {
			String verseNumber = e.children().get(0).text();
			verseNumber = verseNumber.replace('.', ' ');
			verseNumber = verseNumber.trim();
			String verseText = e.children().get(1).text();
			lst.add(new VerseExtracted(Integer.parseInt(verseNumber), verseText));
		});
		return lst;
	}


	private Document retrieveDoc(String url) {
		Document doc = null;
		try {
			doc = Jsoup.parse(URI.create(url).toURL(), 9000);
			return doc;
		} catch (Exception e) {
			e.printStackTrace();
			return this.retrieveDoc(url);
		}
	}
	
//		String xml = "<chapter osisID=\"Ezek.28\">"
//				+ "<verse osisID=\"Ezek.28.1\">¶ E veio a mim a palavra do SENHOR, dizendo:</verse>"
//				+ "<verse osisID=\"Ezek.28.2\">ballbal:</verse>"
//				+ "</chapter>";
//		
//		JAXBContext context = JAXBContext.newInstance(CapituloXml.class);
//		CapituloXml capitulo = (CapituloXml) context.createUnmarshaller().unmarshal(new StringReader(xml));
		
//		System.out.println(capitulo);
		
		
		
//	}
	
}
