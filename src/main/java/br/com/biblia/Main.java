package br.com.biblia;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import org.springframework.util.StringUtils;

import br.com.biblia.enums.LivroEnum;

public class Main {

	public static void main(String[] args) throws Exception {
//		String s = "/home/lucasm/Documents/livros_detalhes_old.txt";
//		FileOutputStream fileOutputStream = new FileOutputStream(new File(s));
//		System.out.println(fileOutputStream);
		
		String termo = "Jó b";
		String termo2 = "1 Jo 3.3";
				
		String[] split = termo.split(" ");
		LivroEnum livroEnum = LivroEnum.fromSiglaPortugues(split[0]);
		System.out.println(livroEnum != null && Character.isDigit(split[1].charAt(0)));
		
//		List<String> readAllLines = Files.readAllLines(Paths.get(new File(s).toURI()));
//		readAllLines.stream().forEach(c -> {
//			if (c.indexOf('"') != -1) {
//				System.out.println(c.substring(0, c.indexOf('"')-2)+");");
//			}
//		});
//		
	}
	
}
