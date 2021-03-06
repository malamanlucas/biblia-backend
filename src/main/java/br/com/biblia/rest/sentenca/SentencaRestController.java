package br.com.biblia.rest.sentenca;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import br.com.biblia.apps.sentenca.SentencaApp;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.sentenca.Sentenca;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/sentencas")
public class SentencaRestController {

	@Autowired
    private SentencaApp app;
    
    @PostMapping(value="/")
    public Map<String, Object> format(String termo, 
    		@RequestParam(defaultValue="true") Boolean ignoreCase,
    		@RequestParam(defaultValue="true") Boolean ignoreAccent,
    		@RequestBody List<Integer> versoes) {
    	List<Sentenca> result = null;
		if (needSearchByCordenada(termo)) {
			result = app.searchSentencasByCordenada(termo, versoes);
		} else {
			result = app.searchSentencasByTermo(termo, ignoreCase, ignoreAccent, versoes);
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

	@Data @NoArgsConstructor @AllArgsConstructor
	static class SentencaJson {
		@NotNull @NotEmpty List<Integer> versoesId;
		@NotNull Integer livroId;
		@NotNull Integer capituloId;
		@NotNull Integer versiculoId;
	}
	
	@PostMapping("/versoes")
	List<Sentenca> findByLivroCapituloAndVersiculoAndVersoes(@Valid @RequestBody SentencaJson json) {
		List<Sentenca> sentencas = this.app.findByLivroCapituloAndVersiculoAndVersoes(json.getVersoesId(), json.getLivroId(), json.getCapituloId(), json.getVersiculoId());
		sentencas.forEach(s -> {
			s.setTextoMontado( StringUtils.join("[", s.getVersao(), "] ", s.getTextoMontado()));
		});
		return sentencas;
	}

}
