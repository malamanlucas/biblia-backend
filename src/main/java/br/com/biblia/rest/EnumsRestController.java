package br.com.biblia.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblia.enums.IdiomaEnum;

@RestController
@RequestMapping("/api/enums")
public class EnumsRestController {

	@GetMapping("/idiomas")
	public List<IdiomaEnum> getAllIdiomas() {
		return Arrays.asList(IdiomaEnum.values());
	}
	
}
