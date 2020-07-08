package br.com.biblia.apps.importador.sbb;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.google.common.collect.Lists;

import br.com.biblia.apps.importador.sbb.ImportarFromBibliaSbb.VerseExtracted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class GetVerseExtractedSbb {
	
	private static final String NUMERO_VERSICULO_TAG = "v";

	private static final String CAPITULO_TAG = "p";

	public List<VerseExtracted> extract(String livroKey, Integer capituloId) {
		String urlFormatted = "";
		return x(urlFormatted);
	}

	private List<VerseExtracted> x(String urlFormatted) {
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

}
