package br.com.biblia.apps.importador;

import java.net.URI;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class RevistasDigitais {

	static final String baseUrl = "https://www.editoracentralgospel.com/_gutenweb/_loja/revista-LPD-assinatura/00%d/professor/files/assets/"; 
	
	public static void main(String[] args) throws Exception {
		
		for (int i=48; i <= 53; i++) {
			Document doc = null;
			String url = "https://www.editoracentralgospel.com/_gutenweb/_loja/revista-LPD-assinatura/00%d/professor/files/assets/basic-html/page-1.html";
			url = String.format(url, i);
			doc = Jsoup.parse(URI.create(url).toURL(), 9000);
			
			String href = doc.getElementById("downloadPdfLink").attr("href");
			String fullUrl = String.format("%s%s", String.format(baseUrl, i), href.substring(href.indexOf("common")));
			System.out.println(fullUrl);
//			File file = new File(String.format("/home/lucasm/livros/%d.pdf", i));
//			org.apache.commons.io.FileUtils.copyURLToFile(new URL(fullUrl), file, 99999999, 99999999);
		}
		
	}
	
}
