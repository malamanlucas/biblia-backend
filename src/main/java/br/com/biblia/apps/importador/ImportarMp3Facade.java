package br.com.biblia.apps.importador;

import static br.com.biblia.enums.IdiomaEnum.GREGO;
import static br.com.biblia.enums.IdiomaEnum.HEBRAICO;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import com.google.common.collect.Lists;

import br.com.biblia.enums.LivroEnum;
import br.com.biblia.enums.Testamento;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class ImportarMp3Facade {
	
	@AllArgsConstructor
	enum Mp3Enum {
		GENESIS (50, "GEN", "Gn", Testamento.VELHO),
		EXODO (40, "EXO", "Ex", Testamento.VELHO),
		LEVITICO (27, "LEV", "Lv", Testamento.VELHO),
		NUMEROS (36, "NUM", "Nm", Testamento.VELHO),
		DEUTERONOMIO (34, "DEU", "Dt", Testamento.VELHO),
		JOSUE (24, "JOS", "Js", Testamento.VELHO),
		JUIZES (21, "JDG", "Jz", Testamento.VELHO),
		RUTE (4, "RUT", "Rt", Testamento.VELHO),
		PRIMEIRO_SAMUEL (31, "1SA", "1 Sm", Testamento.VELHO),
		SEGUNDO_SAMUEL (24, "2SA", "2 Sm", Testamento.VELHO),
		PRIMEIRO_REIS  (22, "1KI", "1 Rs", Testamento.VELHO),
		SEGUNDO_REIS (25, "2KI", "2 Rs", Testamento.VELHO),
		PRIMEIRO_CRONICAS (29, "1CH", "1 Cr", Testamento.VELHO),
		SEGUNDO_CRONICAS (36, "2CH", "2 Cr", Testamento.VELHO),
		ESDRAS (10, "EZR", "Ed", Testamento.VELHO),
		NEEMIAS (13, "NEH", "Ne", Testamento.VELHO),
		ESTER (10, "EST", "Et", Testamento.VELHO),
		JO (42, "JOB", "Jó", Testamento.VELHO),
		SALMOS (150, "PSA", "Sl", Testamento.VELHO),
		PROVÉRBIOS (31, "PRO", "Pv", Testamento.VELHO),
		ECLESIASTES (12, "ECC", "Ec", Testamento.VELHO),
		CANTARES (8, "SNG", "Ct", Testamento.VELHO),
		ISAÍAS (66, "ISA", "Is", Testamento.VELHO),
		JEREMIAS (52, "JER", "Jr", Testamento.VELHO),
		LAMENTACOES (5, "LAM", "Lm", Testamento.VELHO),
		EZEQUIEL (48, "EZK", "Ez", Testamento.VELHO),
		DANIEL (12, "DAN", "Dn", Testamento.VELHO),
		OSEIAS (14, "HOS", "Os", Testamento.VELHO),
		JOEL (3, "JOL", "Jl", Testamento.VELHO),
		AMOS (9, "AMO", "Am", Testamento.VELHO),
		OBADIAS (1, "OBA", "Ob", Testamento.VELHO),
		JONAS (4, "JON", "Jn", Testamento.VELHO),
		MIQUEIAS (7, "MIC", "Mq", Testamento.VELHO),
		NAUM (3, "NAM", "Na", Testamento.VELHO),
		HABACUQUE (3, "HAB", "Hc", Testamento.VELHO),
		SOFONIAS (3, "ZEP", "Sf", Testamento.VELHO),
		AGEU (2, "HAG", "Ag", Testamento.VELHO),
		ZACARIAS (14, "ZEC", "Zc", Testamento.VELHO),
		MALAQUIAS (4, "MAL", "Ml", Testamento.VELHO),
		MATEUS (28, "MAT", "Mt", Testamento.NOVO),
		MARCOS (16, "MRK", "Mc", Testamento.NOVO),
		LUCAS (24, "LUK", "Lc", Testamento.NOVO),
		JOAO (21, "JHN", "Jo", Testamento.NOVO),
		ATOS (28, "ACT", "At", Testamento.NOVO),
		ROMANOS (16, "ROM", "Rm", Testamento.NOVO),
		PRIMEIRA_CORINTIOS (16, "1CO", "1 Co", Testamento.NOVO),
		SEGUNDA_CORINTIOS (13, "2CO", "2 Co", Testamento.NOVO),
		GALATAS (6, "GAL", "Gl", Testamento.NOVO),
		EFÉSIOS (6, "EPH", "Ef", Testamento.NOVO),
		FILIPENSES (4, "PHP", "Fp", Testamento.NOVO),
		COLOSSENSES (4, "COL", "Cl", Testamento.NOVO),
		PRIMEIRA_TESSALONICENSES (5, "1TH", "1 Ts", Testamento.NOVO),
		SEGUNDA_TESSALONICENSES (3, "2TH", "2 Ts", Testamento.NOVO),
		PRIMEIRA_TIMOTEO (6, "1TI", "1 Tm", Testamento.NOVO),
		SEGUNDA_TIMOTEO (4, "2TI", "2 Tm", Testamento.NOVO),
		TITO (3, "TIT", "Tt", Testamento.NOVO),
		FILEMOM (1, "PHM", "Fl", Testamento.NOVO),
		HEBREUS (13, "HEB", "Hb", Testamento.NOVO),
		TIAGO (5, "JAS", "Tg", Testamento.NOVO),
		PRIMEIRA_PEDRO (5, "1PE", "1 Pe", Testamento.NOVO),
		SEGUNDA_PEDRO (3, "2PE", "2 Pe", Testamento.NOVO),
		PRIMEIRA_JOAO (5, "1JN", "1 Jo", Testamento.NOVO),
		SEGUNDA_JOAO (1, "2JN", "2 Jo", Testamento.NOVO),
		TERCEIRA_JOAO (1, "3JN", "3 Jo", Testamento.NOVO),
		JUDAS (1, "JUD", "Jd", Testamento.NOVO),
		APOCALIPSE (22, "REV", "Ap", Testamento.NOVO);
		
		@Getter private Integer qtd;
		@Getter	private String nameToFind;
		@Getter private String originalName;
		@Getter private Testamento testamento;
	}
	
	static List<Future<?>> allTasks = Lists.newArrayList();

	public static void main(String[] args) throws Exception{
		
		Mp3Enum[] values = Mp3Enum.values();
//		Mp3Enum[] values = new Mp3Enum[] { Mp3Enum.CANTARES };
		
//		allTasks.clear();
//		ExecutorService executor = Executors.newWorkStealingPool();
		for (Mp3Enum livroEnum : values) {
			
			for (int i = 1; i <= livroEnum.getQtd(); i++) {
				final int capitulo = i;
//				allTasks.add(executor.submit(() -> bla(livroEnum, capitulo)));
				bla(livroEnum, capitulo);
			}
			
		}
//		executor.shutdown();
	}

	private static void bla(Mp3Enum livroEnum, int capitulo) {
		try {
			String rootFolder = livroEnum.getTestamento().equals(Testamento.VELHO) ? "AT" : "NT";
			
			File destination = new File(String.format("/home/lucasm/%s/%s/%s-%d.mp3", rootFolder, livroEnum.getOriginalName(), livroEnum.getOriginalName(), capitulo));
			
			if (!destination.exists()) {
				Document doc = null;
				String url = String.format("https://www.bible.com/bible/1608/%s.%d.ara", livroEnum.getNameToFind(), capitulo);
				
				System.out.println(url);
				
				doc = Jsoup.connect(url)
						.timeout(120000)
						.userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36")
						.get();
				
				StringBuilder urlToDownload = new StringBuilder("http:");
				urlToDownload.append(doc.select("audio source:last-child").get(0).attr("src"));
				System.out.println(urlToDownload.toString());
				FileUtils.copyURLToFile(URI.create(urlToDownload.toString()).toURL(), destination, 150000, 155000);
			}
		} catch (Exception e) {
			System.out.println(e);
		}
	}

}
