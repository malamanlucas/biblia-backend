package br.com.biblia.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.model.CapituloKey;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;

@RestController
@RequestMapping("/api/versiculos")
public class VersiculoRestController {

	@Autowired
    private VersiculoApp app;

    @GetMapping(value="/")
    public List<Versiculo> findAll(@RequestParam("livroId") Integer livroId,
    							   @RequestParam("capituloId") Integer capituloId,
    							   @RequestParam("versaoId") Integer versaoId) {
        return app.search( new CapituloKey(capituloId, livroId, versaoId) );
    }
    
    @PostMapping("/find")
    public Versiculo findOne(@RequestBody VersiculoKey key) {
    	return app.findOne(key);
    }

}
