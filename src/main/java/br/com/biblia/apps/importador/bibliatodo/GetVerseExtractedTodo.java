package br.com.biblia.apps.importador.bibliatodo;

import com.google.common.base.CharMatcher;
import com.google.common.collect.Lists;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.NumberUtils;
import org.springframework.web.client.RestTemplate;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GetVerseExtractedTodo {
	
	private static final String CAPITULO_TAG = "imprimible";

	public static List<ImportarFromBibliaTodo.VerseExtracted> extract(String livroKey, Integer capituloId) {
		String urlFormatted = "https://www.bibliatodo.com/pt/a-biblia/almeida-revista-corrigida-1995/%s-%s";
		try {
			return x(String.format(urlFormatted, livroKey, capituloId));
		} catch (ParserConfigurationException | IOException | SAXException parserConfigurationException) {
			parserConfigurationException.printStackTrace();
			return null;
		}
	}

	private static List<ImportarFromBibliaTodo.VerseExtracted> x(String urlFormatted) throws ParserConfigurationException, IOException, SAXException {
		try {
			Document document = Jsoup.connect(urlFormatted).get();
			return document.getElementById(CAPITULO_TAG).getElementsByTag("p")
						.stream()
						.map(e -> {
							String textContent = e.ownText().trim();
							String verseNumber = e.getElementsByTag("strong")
									.stream()
									.findFirst()
									.get()
									.text();
							Integer verseNumberAsInteger = Integer.valueOf(CharMatcher.inRange('0', '9').retainFrom(verseNumber));
							return new ImportarFromBibliaTodo.VerseExtracted(verseNumberAsInteger, textContent);
						})
						.collect(Collectors.toList());
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}


}
