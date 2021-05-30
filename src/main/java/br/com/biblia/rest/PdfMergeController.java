package br.com.biblia.rest;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.pdfbox.io.MemoryUsageSetting;
import org.apache.pdfbox.multipdf.PDFMergerUtility;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/pdf")
@Slf4j
public class PdfMergeController {

    @SneakyThrows
    @PostMapping(path = "/merge", value = "/merge", produces = {MediaType.APPLICATION_PDF_VALUE})
    public ResponseEntity<byte[]> mergeUpload(@RequestParam("file") List<MultipartFile> files, @RequestParam String destinationName) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        PDFMergerUtility pdfMergerUtility = new PDFMergerUtility();
        pdfMergerUtility.setDestinationStream(output);
//        pdfMergerUtility.setDestinationStream(response.getOutputStream());
        pdfMergerUtility.setDestinationFileName(destinationName);
        files.stream()
                .forEach(c -> {
                    try {
                        pdfMergerUtility.addSource(c.getInputStream());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
//        response.setHeader(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename:\"%s\"", destinationName));
        pdfMergerUtility.mergeDocuments(MemoryUsageSetting.setupMainMemoryOnly());
        HttpHeaders headers = new HttpHeaders();
        byte[] bytes = output.toByteArray();
        headers.setContentLength(bytes.length);
        headers.set("Content-Disposition", "attachment; filename=somefile.pdf");
        headers.setContentType(MediaType.APPLICATION_PDF);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + destinationName)
                .contentLength(bytes.length)
                .contentType(MediaType.APPLICATION_PDF)
                .body(bytes);
//        return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
    }

}
