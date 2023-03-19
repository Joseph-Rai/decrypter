package net.oxstone.decrypter.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.nio.file.StandardOpenOption.APPEND;
import static java.nio.file.StandardOpenOption.CREATE;

@Controller
@RequestMapping("/decrypt")
@Slf4j
public class DecryptController {
    @PostMapping
    public ResponseEntity<String> handleXml(@RequestBody byte[] fileBytes, HttpServletRequest request) {

        try {
            // 합쳐질 파일을 저장할 경로와 파일명 지정
            String userHomePath = System.getProperty("user.home");
            int bytesNumber = request.getIntHeader("bytesNumber");
            Path filePath = Paths.get(userHomePath).resolve("decryptedFileTemp.dat");

            // 파일 스트림 열기
            OutputStream outStream = new BufferedOutputStream(Files.newOutputStream(filePath, CREATE, APPEND));

            if (bytesNumber > 0) {
                // chunk 요청 병합
                outStream.write(fileBytes, 0, bytesNumber);
            } else {
                log.info("Decryption of the requested file has been completed.");
            }

            outStream.close();

            return ResponseEntity.ok().body("File decryption succeed.");
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File decryption failed.");
        }
    }
}
