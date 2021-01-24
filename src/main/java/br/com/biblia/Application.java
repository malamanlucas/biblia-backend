package br.com.biblia;

import java.util.Locale;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;

import br.com.biblia.apps.importador.ImportarACF;
import br.com.biblia.apps.importador.bdcn.ImportarFromBibliaBDCN;
import br.com.biblia.apps.importador.bkjfiel.ImportarFromBibliaBkj;
import br.com.biblia.config.AutowireHelper;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		Locale.setDefault(new Locale("pt", "BR"));
		ConfigurableApplicationContext ctx = SpringApplication.run(Application.class, args);
//		ctx.getBean(ImportarFromBibliaBDCN.class).executar();
//		ctx.getBean(ImportarFromBibliaBkj.class).executar();
	}
	
	@Bean
	public AutowireHelper autowireHelper(){
	    return AutowireHelper.getInstance();
	}
}
