package com.vilu.pombo.service;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.factory.DenunciaFactory;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.repository.DenunciaRepository;
import com.vilu.pombo.model.repository.PruuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class DenunciaServiceTest {

    @InjectMocks
    private DenunciaService denunciaService;

    @Mock
    private DenunciaRepository denunciaRepository;

    @Mock
    private PruuRepository pruuRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void deveSalvarDenunciaComSucesso() throws PomboException {
        Denuncia novaDenuncia = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findAll()).thenReturn(Collections.emptyList());
        when(denunciaRepository.save(novaDenuncia)).thenReturn(novaDenuncia);

        Denuncia resultado = denunciaService.denunciar(novaDenuncia);

        assertNotNull(resultado);
        assertEquals(novaDenuncia.getPruuId(), resultado.getPruuId());
        assertEquals(novaDenuncia.getUsuarioId(), resultado.getUsuarioId());
        verify(denunciaRepository, times(1)).save(novaDenuncia);
    }

    @Test
    void deveLancarExcecaoQuandoUsuarioJaDenunciou() {
        Denuncia denunciaExistente = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findAll()).thenReturn(List.of(denunciaExistente));

        Denuncia novaDenuncia = DenunciaFactory.criarDenunciaDefault();

        PomboException excecao = assertThrows(PomboException.class, () -> denunciaService.denunciar(novaDenuncia));
        assertEquals("O usuário já realizou uma denúncia para este Pruu.", excecao.getMessage());
        verify(denunciaRepository, never()).save(novaDenuncia);
    }

    @Test
    void deveListarTodasAsDenuncias() {
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findAll()).thenReturn(List.of(denuncia));

        List<Denuncia> resultado = denunciaService.listarDenuncias();

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(denuncia.getPruuId(), resultado.getFirst().getPruuId());
    }

    @Test
    void devePesquisarDenunciaPorId() {
        String idDenuncia = "123";
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findById(idDenuncia)).thenReturn(Optional.of(denuncia));

        Optional<Denuncia> resultado = denunciaService.pesquisarDenunciaPorId(idDenuncia);

        assertTrue(resultado.isPresent());
        assertEquals(denuncia.getPruuId(), resultado.get().getPruuId());
    }

    @Test
    void deveListarDenunciasPorIdPruu() {
        String idPruu = "uuidPruuTest";
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findAll()).thenReturn(List.of(denuncia));

        List<Denuncia> resultado = denunciaService.listarDenunciasPorIdPruu(idPruu);

        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals(idPruu, resultado.getFirst().getPruuId());
    }

    @Test
    void deveGerarRelatorioPorIdPruu() {
        String idPruu = "uuidPruuTest";
        Denuncia denuncia = DenunciaFactory.criarDenunciaDefault();
        when(denunciaRepository.findAll()).thenReturn(List.of(denuncia));

        DenunciaDTO relatorio = denunciaService.gerarRelatorioPorIdPruu(idPruu);

        assertNotNull(relatorio);
        assertEquals(idPruu, relatorio.getIdPruu());
        assertEquals(1, relatorio.getQuantidadeDenuncias());
    }
}
