package com.vilu.pombo.service;

import com.vilu.pombo.exception.PomboException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ImagemServiceTest {

    @Test
    @DisplayName("Deve processar imagem com sucesso")
    public void deveProcessarImagemComSucesso() throws IOException, PomboException {
        ImagemService imagemService = new ImagemService();
        MultipartFile file = mock(MultipartFile.class);
        byte[] imagemBytes = "imagem de teste".getBytes();

        when(file.getBytes()).thenReturn(imagemBytes);

        String base64Imagem = imagemService.processarImagem(file);

        assertNotNull(base64Imagem);
        assertEquals(Base64.getEncoder().encodeToString(imagemBytes), base64Imagem);
    }

    @Test
    @DisplayName("Deve lançar exceção quando erro ao processar imagem")
    public void deveLancarExcecaoQuandoErroAoProcessarImagem() throws IOException {
        ImagemService imagemService = new ImagemService();
        MultipartFile file = mock(MultipartFile.class);

        when(file.getBytes()).thenThrow(new IOException("Erro ao ler o arquivo"));

        PomboException exception = assertThrows(PomboException.class, () -> {
            imagemService.processarImagem(file);
        });

        assertEquals("Houve um erro ao processar o arquivo.", exception.getMessage());
    }
}
