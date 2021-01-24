package br.com.biblia.apps.importador.bkjfiel;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

import br.com.biblia.apps.importador.bkjfiel.ImportarFromBibliaBkj.VerseExtracted;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Service
public class GetVerseExtractedBkj {
	
	public List<VerseExtracted> extract(String livroKey, Integer capituloId) {
		try {
			List<ResponseVerseBkj> response = extractResponse(livroKey, capituloId);
			
			List<VerseExtracted> lst = Lists.newArrayList();
			response.forEach(r -> {
				lst.add(new VerseExtracted(r.getVerse(), r.getText().trim()));
			});
			return lst;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}
	
	@Data @AllArgsConstructor
	static class BodyBkj {
		String book;
		String chapter;
	}
	
	@Data @AllArgsConstructor @NoArgsConstructor
	static class ResponseVerseBkj {
		Integer verse;
		String text;
	}

	private List<ResponseVerseBkj> extractResponse(String livroKey, Integer capituloId) {
		try {
			RestTemplate restTemplate = new RestTemplate();
			restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());
			URI url = URI.create("https://www.bkjfiel.com.br/api/verses");
			BodyBkj request = new BodyBkj(livroKey, String.valueOf(capituloId));
			ResponseVerseBkj[] lst = restTemplate.postForObject(url, request, ResponseVerseBkj[].class);
			return Arrays.asList(lst);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		}
	}

}
