package br.com.biblia.apps.sentenca;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import br.com.biblia.dao.SentencaDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.sentenca.Between;
import br.com.biblia.model.sentenca.Cordenada;
import br.com.biblia.model.sentenca.Sentenca;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class SentencaFacade implements SentencaApp {

	@Autowired
	private SentencaDAO sentencaDAO;
	
	@Override
	public List<Sentenca> searchSentencasByTermo(String termo, Boolean ignoreCase, Boolean ignoreAccent, List<Integer> versoes) {
		if (!ignoreCase && !ignoreAccent) {
			return sentencaDAO.searchByTermoWithCaseSensitive(termo, versoes);
		} else if (ignoreCase && ignoreAccent) {
			return sentencaDAO.searchByTermoWithCaseInsensitiveAndAccent(termo, versoes);
		} else if (ignoreAccent) {
			return sentencaDAO.searchByTermoWithCaseSensitiveAndAccent(termo, versoes);
		}
		//else if ignoreCase == true
		return sentencaDAO.searchByTermoWithCaseInsensitive(termo, versoes);
	}
	
	@Override
	public List<Sentenca> searchSentencasByCordenada(String cordenada, List<Integer> versoes) {
		String[] options = customSplit(cordenada);
		Between capitulos = new Between(0, 0);
		Between versiculos = new Between(0, 0);
		if (options.length > 1) {
			String[] betweens = options[1].split("[.|-]");
			capitulos.setStart(Integer.valueOf(betweens[0]));
			capitulos.setEnd(Integer.valueOf(betweens[0]));
			if (betweens.length > 1) {
				versiculos.setStart(Integer.valueOf(betweens[1]));
				versiculos.setEnd(Integer.valueOf(betweens[1]));
				if (2 < betweens.length) {
					versiculos.setEnd(Integer.valueOf(betweens[2]));
				}
			}
		}
		Cordenada cordenadaObject = Cordenada.builder()
								.livroEnum(LivroEnum.fromSiglaPortugues(options[0]))
								.capitulos(capitulos)
								.versiculos(versiculos)
								.versoes(versoes)
								.build();
		return sentencaDAO.searchByCordenada(cordenadaObject);
	}

	private String[] customSplit(String cordenada) {
		if (Character.isDigit(cordenada.charAt(0))) {
			cordenada = String.format("%c[]%s", cordenada.charAt(0), cordenada.substring(2));
		}
		
		String[] options = cordenada.split(" ");
		options[0] = options[0].replace("[]", " ");
		return options;
	}

	@Override
	public List<Sentenca> findByLivroCapituloAndVersiculoAndVersoes(List<Integer> versoesID, Integer livroId, Integer capituloId,
			Integer versiculoId) {
		return sentencaDAO
				.findByVersaoIdInAndLivroIdAndCapituloAndVersiculo(versoesID, livroId, capituloId, versiculoId);
	}


}
