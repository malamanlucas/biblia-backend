package br.com.biblia.rest.sentenca;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Maps;

import br.com.biblia.apps.sentenca.SentencaApp;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.sentenca.Sentenca;

@RestController
@RequestMapping("/api/sentencas")
public class SentencaRestController {

	@Autowired
    private SentencaApp app;
    
    @GetMapping(value="/")
    public Map<String, Object> format(String termo, 
    		@RequestParam(defaultValue="true") Boolean ignoreCase,
    		@RequestParam(defaultValue="true") Boolean ignoreAccent) {
    	List<Sentenca> result = null;
		if (needSearchByCordenada(termo)) {
			result = app.searchSentencasByCordenada(termo);
		} else {
			result = app.searchSentencasByTermo(termo, ignoreCase, ignoreAccent);
		}
		List<SentencaTexto> textos = result.stream().map(SentencaTexto::new).collect(Collectors.toList());
		
		Map<String, Object> map = Maps.newHashMap();
		
		map.put("termo", termo);
		map.put("total", textos.size());
		map.put("textos", textos);
		
		return map;
    }

	private boolean needSearchByCordenada(String termo) {
		if (Character.isDigit(termo.charAt(0))) {
			return true;
		}
		String[] parts = termo.split(" ");
		return parts.length > 1 && Objects.nonNull(LivroEnum.fromSiglaPortugues(parts[0])) 
				&& Character.isDigit(parts[1].charAt(0));
		
	}
	
}
