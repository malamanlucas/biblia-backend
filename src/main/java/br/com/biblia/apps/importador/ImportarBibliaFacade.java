package br.com.biblia.apps.importador;

import java.io.IOException;
import java.net.URI;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Service;

import com.google.common.collect.Lists;

import br.com.biblia.enums.LivroEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@Service
public class ImportarBibliaFacade implements ImportarBiblia {

//	@Autowired
//	private JdbcTemplate jdbcTemplate;
	
	@Override
	public void importar(String livro, String abbreviation, Integer qtdCapitulos) {
//		System.out.println(jdbcTemplate);
//		t(livro, abbreviation, qtdCapitulos);
	}

	static JdbcTemplate jdbcTemplate;
	
	public static void main(String[] args) {
		
		Document doc = null;
		try {
			String url = "https://biblias.com.br/acfonline-versos?livro=27&capitulo=1";
			doc = Jsoup.parse(URI.create(url).toURL(), 9000);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		Elements elements = doc.getElementsByTag("div");
		
		elements.forEach(e -> {
			String verseNumber = e.children().get(0).text();
			verseNumber = verseNumber.replace('.', ' ');
			verseNumber = verseNumber.trim();
			String verseText = e.children().get(1).text();
			System.out.println(StringUtils.join(verseNumber, " - ", verseText));
		});
		
//		LivroEnum[] values = LivroEnum.values();
////		LivroEnum[] values = new LivroEnum[] { LivroEnum.SALMOS };
//		DataSource dataSource = DataSourceBuilder
//				.create()
//				.password("postgres")
//				.username("postgres")
//				.driverClassName("org.postgresql.Driver")
//				.url("jdbc:postgresql://localhost:5432/postgres")
//				.build();
//		jdbcTemplate = new JdbcTemplate(dataSource);
//			
//		for (LivroEnum enum1 : values) {
//			if (!enum1.isNovoTestamento() || enum1 == LivroEnum.MATEUS)
//				continue;
//			System.out.println("Inserindo: "+enum1.name());
//			long start = System.currentTimeMillis();
//			internalImport(enum1);
//			
//			while(true) {
//				boolean terminou = allTasks.stream().filter(i -> !i.isDone()).toArray().length == 0;
//				
//				if (terminou) {
//					break;
//				}
//			}
//			System.out.println("Demorou: " + (System.currentTimeMillis() - start));
//			
//		}
		
	}
		
	static List<Future<?>> allTasks = Lists.newArrayList();
	
	private static void internalImport(LivroEnum livro) {
		
		allTasks.clear();
		
		Integer livroId = jdbcTemplate.queryForObject( "SELECT id FROM livro WHERE nome=?", Integer.class, livro.getNomeNoBD());
		
		ExecutorService executor = Executors.newWorkStealingPool();
		
		for (int capituloId = 1; capituloId <= livro.getQtdCapitulo(); capituloId++) {
			
			Integer currentCapitulo = capituloId;
			
			allTasks.add(executor.submit(() -> extractCapitulo(livro, livroId, currentCapitulo)));
//			extractCapitulo(livro, livroId, currentCapitulo);
			
		}
		executor.shutdown();
	}

	private static void extractCapitulo(LivroEnum livro, Integer livroId, int capituloId) {
		Document doc = null;
		try {
			String url = null;
//			if ( livro.isStartingWithNumber()) {
//				url = String.format("https://www.biblegateway.com/passage/?search=%s+%s&version=ARC&interface=print", livro.getNomeSemAcentuacao().replace(" ", "%20"), capituloId);
////			} else if ( livro == LivroEnum.LAMENTACOES || livro == LivroEnum.LEVITICO ) {
//				url = String.format("https://www.biblegateway.com/passage/?search=%s+%s&version=ARC&interface=print", livro.getNomeNoBD(), capituloId);
//			} else {
//				url = String.format("https://www.biblegateway.com/passage/?search=%s+%s&version=ARC&interface=print", livro.getNomeSemAcentuacao(), capituloId);
//			}
			doc = Jsoup.parse(URI.create(url).toURL(), 9000);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		Elements elements = doc.getElementsByAttributeValueContaining("class", "text "+livro.getSiglaEmIngles());
		
		int numeroVersiculo = 1;
		List<VersData> versiculos = new ArrayList<>();
		StringBuffer titulo = new StringBuffer();
		for (Element e : elements) {
			if (!isVersiculo(e, livro)) { // titulo capitulo
				titulo.append(e.text());
				jdbcTemplate.update( "UPDATE capitulo SET titulo=? WHERE id=? AND livro_id=?",titulo.toString(), capituloId, livroId);
			} else { // versiculo
				String versiculo = e.text().substring(2).replaceAll("\\d", "").trim();
				versiculos.add( new VersData(versiculo, numeroVersiculo, capituloId, livroId) );
				numeroVersiculo++;
			}
		}
		
		persistToBD(livro, versiculos);
	}

	private static void persistToBD(LivroEnum livro, List<VersData> versiculos) {
		//			String sqlUpdate = "UPDATE versiculo SET limpo=? WHERE numero=? AND capitulo_id=? AND livro_id=?;";
		//			jdbcTemplate.batchUpdate(sqlUpdate, new BatchPreparedStatementSetter() {
		//				public void setValues(PreparedStatement ps, int i) throws SQLException {
		//					VersData e = versiculos.get(i);
		//					ps.setString(1, e.getText());
		//					ps.setInt(2, e.getNumeroVersiculo());
		//					ps.setInt(3, e.getCapituloId());
		//					ps.setInt(4, e.getLivroId());
		//				}
		//				public int getBatchSize() {
		//					return versiculos.size();
		//				}
		//			});
				
				String sqlInsert = "INSERT INTO versiculo(id,capitulo_id,livro_Id,texto,idioma,formatado,numero,limpo) VALUES(?,?,?,?,?,?,?,?)";
				for (VersData e : versiculos) {
					try {
						jdbcTemplate.update(sqlInsert, new PreparedStatementSetter() {
							@Override
							public void setValues(PreparedStatement ps) throws SQLException {
								String sqlMax = "SELECT COALESCE(MAX(numero),0)+1 FROM versiculo WHERE livro_id = ? AND capitulo_id = ?";
								Integer nextVal = jdbcTemplate.queryForObject(sqlMax, Integer.class, e.getLivroId(), e.getCapituloId());
								ps.setInt(1, nextVal);
								ps.setInt(2, e.getCapituloId());
								ps.setInt(3, e.getLivroId());
								ps.setString(4, e.getText());
								ps.setString(5, livro.getIdioma().name());
								ps.setString(6, e.getText());
								ps.setInt(7, e.getNumeroVersiculo());
								ps.setString(8, e.getText());
							}
						});
					} catch (Exception e1){}
				}
	}
	
	private static boolean isVersiculo(Element e, LivroEnum livro) {
		String nodeName = e.parent().nodeName();
		
		boolean hasClassVersiculo = e.classNames().stream()
									 .filter(i -> i.contains(livro.getSiglaEmIngles())).toArray().length != 0;
		
		return hasClassVersiculo && nodeName.equals("p");
	}

	@Data
	@AllArgsConstructor
	static class VersData {
		private String text;
		private Integer numeroVersiculo;
		private Integer capituloId;
		private Integer livroId;
	}
	
}
