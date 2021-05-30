package br.com.biblia;

import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {


//	public static void main(String[] args) throws Exception {
//		PDFMergerUtility ut = new PDFMergerUtility();
//		ut.addSource(getFile("/Users/ucoliveira/Downloads/ate 132.pdf"));
//		ut.addSource(getFile("/Users/ucoliveira/Downloads/até 135.pdf"));
//		ut.setDestinationFileName("/Users/ucoliveira/ferramentas/bla.pdf");
//		ut.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
//	}
//
//	private static InputStream getFile(String path) throws IOException {
//		Path ate132 = Path.of(path);
//		InputStream inputStream = Files.newInputStream(ate132);
//		return inputStream;
//	}

}
