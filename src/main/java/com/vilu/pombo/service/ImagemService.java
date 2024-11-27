package com.vilu.pombo.service;

import com.vilu.pombo.exception.PomboException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

@Service
public class ImagemService {

    public String processarImagem(MultipartFile file) throws PomboException {
        byte[] imagemBytes;
        try {
            imagemBytes = file.getBytes();
        } catch (IOException e) {
            throw new PomboException("Houve um erro ao processar o arquivo.", HttpStatus.REQUEST_HEADER_FIELDS_TOO_LARGE);
        }

        String base64Imagem = Base64.getEncoder().encodeToString(imagemBytes);

        return base64Imagem;
    }
}
