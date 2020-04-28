package br.com.biblia.model.sentenca;

import java.util.List;

import br.com.biblia.enums.LivroEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Cordenada {

	private LivroEnum livroEnum;
	
	private Between capitulos;
	
	private Between versiculos;
	
	private List<Integer> versoes;
	
}
