package hcy.upload.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.Part;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;

@Slf4j
@Controller
@RequestMapping("/servlet/v2")
public class ServletUploadControllerV2 {

    // application.properties 의 값을 가져옴.
    @Value("${file.dir}")
    private String fileDir;

    @GetMapping("/upload")
    public String newFile() {
        return "upload-form";
    }


    /**
     * 출력 예시
     *
     * request=org.springframework.web.multipart.support.StandardMultipartHttpServletRequest@14deecda
     *  itemName = spring
     *  parts=[org.apache.catalina.core.ApplicationPart@4269a6cb, org.apache.catalina.core.ApplicationPart@1cf80ffa]
     * ==== PART ====
     *  name=itemName
     * header content-disposition: form-data; name="itemName"
     * submittedFilename=null
     *  size=6
     *  body=spring
     *  ==== PART ====
     *  name=file
     * header content-disposition: form-data; name="file"; filename="스크린샷 2021-07-08 오후 7.18.32.png"
     *  header content-type: image/png
     *  submittedFilename=스크린샷 2021-07-08 오후 7.18.32.png
     *  size=148637
     *  body=�PNG ~~~~~~~~~
     */
    @PostMapping("/upload")
    public String saveFileV1(HttpServletRequest request) throws IOException, ServletException {
        log.info("request={}", request);

        String itemName = request.getParameter("itemName");
        log.info("itemName = {}", itemName);

        Collection<Part> parts = request.getParts();
        log.info("parts={}", parts);

        for (Part part : parts) {
            log.info("==== PART ====");
            log.info("name={}", part.getName());
            Collection<String> headerNames = part.getHeaderNames();
            // PART 마다 있는 header들 나옴.
            for (String headerName : headerNames) {
                log.info("header {}: {}", headerName, part.getHeader(headerName));
            }
            // 편의 메서드
            // content-disposition; filename
            log.info("submittedFilename={}", part.getSubmittedFileName());
            log.info("size={}", part.getSize());

            // 데이터 읽기
            InputStream inputStream = part.getInputStream();
            String body = StreamUtils.copyToString(inputStream, StandardCharsets.UTF_8);
//            log.info("body={}", body);

            // 파일에 저장하기
            if (StringUtils.hasText(part.getSubmittedFileName())) {
                String fullPath = fileDir + part.getSubmittedFileName();
                log.info("파일 저장 fullPath = {}", fullPath);
                part.write(fullPath); // 실제 파일 저장.
            }
        }

        return "upload-form";
    }

}
