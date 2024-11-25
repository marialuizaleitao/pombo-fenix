package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.mock.PruuMockFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
public class PruuRepositoryTest {

    @Autowired
    private PruuRepository pruuRepository;

    @Test
    @DisplayName("Não deve ser possível cadastrar um Pruu com texto inválido")
    public void testInsert$TextoMaisDe500Caracteres() {
        String texto = "a".repeat(301);
        Pruu pruu = PruuMockFactory.criarPruuMock();
        pruu.setTexto(texto);

        assertThatThrownBy(() -> pruuRepository.saveAndFlush(pruu))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um Pruu com URL de imagem inválida")
    public void testInsert$ImagemURLInvalida() {
        Pruu pruu = PruuMockFactory.criarPruuMock();
        pruu.setImagem("imagem_invalida");

        assertThatThrownBy(() -> pruuRepository.saveAndFlush(pruu))
                .isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um Pruu com texto vazio")
    public void testInsert$PruuComTextoVazio() {
        Pruu pruu = PruuMockFactory.criarPruuMock();
        pruu.setTexto("");

        assertThatThrownBy(() -> pruuRepository.saveAndFlush(pruu))
                .isInstanceOf(ConstraintViolationException.class);
    }

}
