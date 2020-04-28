package br.com.biblia.rest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.biblia.enums.IdiomaEnum;
import br.com.biblia.enums.Testamento;

@RestController
@RequestMapping("/api/enums")
public class EnumsRestController {

	@GetMapping("/idiomas")
	public List<IdiomaEnum> getAllIdiomas() {
		return Arrays.asList(IdiomaEnum.values());
	}
	
	@GetMapping("/testamentos")
	public List<Testamento> getAllTestamentos() {
		return Arrays.asList( Testamento.values() ).stream()
					.filter(t -> !t.isAmbos())
					.collect(Collectors.toList());
	}
	
}
