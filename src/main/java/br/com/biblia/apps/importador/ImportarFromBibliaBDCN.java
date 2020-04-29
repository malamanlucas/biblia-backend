package br.com.biblia.apps.importador;

import java.net.URI;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlMixed;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
public class ImportarFromBibliaBDCN {
	
	private static final String NUMERO_VERSICULO_TAG = "v";

	private static final String CAPITULO_TAG = "p";

	private static final String PORTUGUES = "Português";

	private static final int VERSAO_ACF_FOR_EXISTENT_CAPITULO = 1;

	private static final int VERSAO_ID_NVI = 3;

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
	
	private static String extractContent(Node versiculo, StringBuilder versiculoContent) {
		String newContent;
		if (versiculo.getNodeName().equals("#text")) {
			newContent = versiculo.getTextContent().trim();
		} else { // is c
			newContent = versiculo.getFirstChild().getTextContent();
		}
		if (versiculoContent.length() > 0 && newContent.length() > 1) {
			newContent = " " + newContent;
		}
		return newContent;
	}

	public void executar() {
		List<BibliaToImport> lst = Lists.newArrayList();
		
		lst.add(new BibliaToImport("Nova Versão Internacional (NVI)", "nvi", PORTUGUES));
		lst.add(new BibliaToImport("Almeida Revisada Imprensa Bíblica (AA)", "aa", PORTUGUES));
		lst.add(new BibliaToImport("Almeida Revisada Corrigida (ARC)", "arc", PORTUGUES));
		lst.add(new BibliaToImport("Almeida Revisada Corrigida 1969 (ARC69)", "rc69", PORTUGUES));
		lst.add(new BibliaToImport("Almeida Revisada Atualizada (ARA)", "ara", PORTUGUES));
		lst.add(new BibliaToImport("Nova Versão Transformadora (NVT)", "nvt", PORTUGUES));
//		lst.add(new BibliaToImport("O Livro (Ol)", "ol", PORTUGUES));
		lst.add(new BibliaToImport("Sociedade Biblica Britânica (TB)", "tb", PORTUGUES));
		lst.add(new BibliaToImport("Versão Católica (VC)", "vc", PORTUGUES));
		lst.forEach(b -> {
			exec(b.getNomeVersao(), b.getAbreviacao(), b.getIdioma());
		});
	}


	private void exec(String nomeVersao, String abreviacao, String idioma) {
		Versao versao = this.versaoApp.getOrCreateIfNotExists(nomeVersao, abreviacao, idioma);
		
		for (BcdnLivroEnum bdcnLivroEnum : BcdnLivroEnum.values()) {
//		for (BcdnLivroEnum bdcnLivroEnum : new BcdnLivroEnum[] { BcdnLivroEnum.JUDAS } ) {
			LivroEnum livroEnum = bdcnLivroEnum.getLivroEnum();
			Livro livro = livroDAO.findByNome(livroEnum.getNomeNoBD());
			
			if (areVersesCreated(bdcnLivroEnum, versao, livro)) {
				System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), " já está criado"));
				continue;
			} 
			System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), " Cadastrando..."));
			for (int capitulo = 1; capitulo <= livroEnum.getQtdCapitulo(); capitulo++) {
				Integer capituloId = capitulo; 
				Integer livroNumber = bdcnLivroEnum.getLivroNumber();
				 if (areVersesCreatedAtThisCharpter(versao, livro, capituloId)) {
					System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), "-", capituloId, " já está criado"));
					continue;
				}
				System.out.println(StringUtils.join(abreviacao, " - ", livro.getNome(), "-", capituloId, " cadastrando..."));
				String urlFormatted = String.format("https://data.bcdn.in/v19/bibles/%s/%d/%d.xml", 
						versao.getAbreviacao().toLowerCase(), livroNumber, capitulo);
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

	private boolean areVersesCreated(BcdnLivroEnum nviLivroEnum, Versao versao, Livro livro) {
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

	private List<VerseExtracted> extract(String urlFormatted) {
		try {
			String response = extractResponse(urlFormatted);
			Document document = DocumentBuilderFactory.newInstance()
					.newDocumentBuilder().parse(IOUtils.toInputStream(response));
			NodeList blocosVersiculos = document.getFirstChild().getChildNodes();
			
			List<VerseExtracted> lst = Lists.newArrayList();
			for (int i = 0; i < blocosVersiculos.getLength(); i++) {
				Node item = blocosVersiculos.item(i);
				if (item.getNodeName().equals(CAPITULO_TAG)) {
					NodeList versiculos = item.getChildNodes();
					Node versiculo = null;
					for (int versiculoIndex = 0; versiculoIndex < versiculos.getLength(); versiculoIndex++) {
						versiculo = versiculos.item(versiculoIndex);
						if (versiculo.getNodeName().equals(NUMERO_VERSICULO_TAG)) {
							String numeroVersiculo = versiculo.getAttributes().getNamedItem("n").getNodeValue();
							versiculoIndex++;
							StringBuilder versiculoContent = new StringBuilder();
							versiculo = versiculos.item(versiculoIndex);
							do {
								String nodeName = versiculo.getNodeName();
								boolean tagsExpectedForExtractVerse = Stream.of("#text", "c").anyMatch(c -> c.equals(nodeName));
								if (!tagsExpectedForExtractVerse) {
									throw new RuntimeException("Tag not expected for rextracted verse: " + nodeName);
								}
								String replaceAll = extractContent(versiculo, versiculoContent).replaceAll("\\\\", "");
								versiculoContent.append(replaceAll.replaceAll("\"", "'"));
								versiculoIndex++;
								if (versiculoIndex == versiculos.getLength()) break;
								versiculo = versiculos.item(versiculoIndex);
							} while (!versiculo.getNodeName().equals(NUMERO_VERSICULO_TAG));
							versiculoIndex--;
							lst.add(new VerseExtracted(Integer.valueOf(numeroVersiculo), versiculoContent.toString()));
						}
					}
				}
			}
			return lst;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

	private String extractResponse(String urlFormatted) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			URI url = URI.create(urlFormatted);
			return restTemplate.getForObject(url, String.class);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	private CapituloXml retrieveDoc(String urlFormatted) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			URI url = URI.create(urlFormatted);
//			System.out.println(urlFormatted);
			CapituloXml capituloXml = restTemplate.getForObject(url, CapituloXml.class);
			return capituloXml;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
}
