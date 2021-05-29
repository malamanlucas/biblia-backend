package br.com.biblia.rest;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.pdfbox.io.IOUtils;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController("/test")
@Slf4j
public class ControllerTest {

    @SneakyThrows
    @PostMapping(produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    public @ResponseBody byte[] test(@RequestParam("file") List<MultipartFile> file) {
        return IOUtils.toByteArray(file.get(1).getInputStream());
    }

}
