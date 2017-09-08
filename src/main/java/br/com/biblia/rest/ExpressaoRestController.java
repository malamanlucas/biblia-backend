package br.com.biblia.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblia.apps.expressao.ExpressaoApp;
import br.com.biblia.model.versiculo.Expressao;
import br.com.biblia.model.versiculo.VersiculoKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@RestController
@RequestMapping("/api/expressao")
public class ExpressaoRestController {

	@Autowired
	private ExpressaoApp app;
	
    @PostMapping(value="/search")
    public Expressao findAll(@RequestBody ExpressaoSearchParam param) {
    	return app.findByKeyAndInicioAndFim(param.getVersiculoKey(), param.getInicio(), param.getFim());
    }
    
    @Data @AllArgsConstructor @NoArgsConstructor
    static class ExpressaoSearchParam {
    	private VersiculoKey versiculoKey;
    	private Integer inicio;
    	private Integer fim;
    }

}
