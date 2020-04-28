package br.com.biblia.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblia.apps.versao.VersaoApp;
import br.com.biblia.model.Versao;

@RestController("versao")
public class VersaoRestController {

	@Autowired
	VersaoApp app;
	
	@GetMapping("/")
	public List<Versao> findAll() {
		return app.findAll();
	}
	
}
