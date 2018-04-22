package br.com.biblia.apps.importador;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.google.common.collect.Lists;

import br.com.biblia.apps.versiculo.VersiculoApp;
import br.com.biblia.dao.LivroDAO;
import br.com.biblia.dao.VersiculoDAO;
import br.com.biblia.enums.LivroEnum;
import br.com.biblia.model.livro.Livro;
import br.com.biblia.model.versiculo.Versiculo;
import br.com.biblia.model.versiculo.VersiculoKey;

@Service
public class ImportarACF {
	
	@Autowired
	VersiculoApp app;
	
	@Autowired
	private VersiculoDAO dao;
	
	@Autowired
	private LivroDAO livroDAO;
	
	public void executar() {
		System.out.println("bla");
		List<Versiculo> vs = Lists.newArrayList();
		for (LivroEnum livroEnum : LivroEnum.values()) {
			RestTemplate restTemplate = new RestTemplate();
			Livro livro = livroDAO.findByNome(livroEnum.getNomeNoBD());
			
			for (int capitulo = 1; capitulo <= livroEnum.getQtdCapitulo(); capitulo++) {
				String urlFormatted = String.format("https://data.biblebox.com/v18/bibles/acf/%s/%d.xml", livroEnum.getSiglaEmIngles(), capitulo);
				System.out.println(urlFormatted);
				URI url = URI.create(urlFormatted);
				CapituloXml capituloXml = restTemplate.getForObject(url, CapituloXml.class);
				
				capituloXml.getVersiculos().stream().forEach(verse -> {
					Versiculo v = new Versiculo();
					v.setIdioma(livroEnum.getIdioma());
					v.setNumero(verse.getVersiculoId());
					v.setTexto(verse.getContent().trim());
					v.setLimpo(v.getTexto());
					v.setFormatado(v.getTexto());
					v.setQtdAumentado(0);
					VersiculoKey key = VersiculoKey.builder()
							.capituloId(capituloXml.getCapituloId())
							.id(verse.getVersiculoId())
							.livroId(livro.getId())
							.versaoId(1)
							.build();
					v.setKey(key);
					
					dao.save(v);
				});
				
			}
		}
	}
//	public static void main(String[] args) throws Exception {
	
//		String xml = "<chapter osisID=\"Ezek.28\">"
//				+ "<verse osisID=\"Ezek.28.1\">¶ E veio a mim a palavra do SENHOR, dizendo:</verse>"
//				+ "<verse osisID=\"Ezek.28.2\">ballbal:</verse>"
//				+ "</chapter>";
//		
//		JAXBContext context = JAXBContext.newInstance(CapituloXml.class);
//		CapituloXml capitulo = (CapituloXml) context.createUnmarshaller().unmarshal(new StringReader(xml));
		
//		System.out.println(capitulo);
		
		
		
//	}
	
}
