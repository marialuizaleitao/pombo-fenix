package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.mock.PruuMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class PruuRepositoryTest {

    @Autowired
    private PruuRepository pruuRepository;

    @MockBean
    private PruuRepository mockPruuRepository;

    private Pruu pruu;

    @BeforeEach
    public void setUp() {
        pruu = PruuMockFactory.criarPruuMock();
    }

    // Casos de teste para findPruusCurtidosPorUsuario
    // Caso de sucesso
    @Test
    public void testFindPruusCurtidosPorUsuario_Success() {
        when(mockPruuRepository.findPruusCurtidosPorUsuario("12345-usuario-id"))
                .thenReturn(List.of(pruu));

        List<Pruu> result = pruuRepository.findPruusCurtidosPorUsuario("12345-usuario-id");

        assertThat(result).isNotEmpty();
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getId()).isEqualTo("pruu-uuid-12345");
    }

    // Caso de erro
    @Test
    public void testFindPruusCurtidosPorUsuario_Failure() {
        when(mockPruuRepository.findPruusCurtidosPorUsuario("67890-usuario-id"))
                .thenReturn(List.of());

        List<Pruu> result = pruuRepository.findPruusCurtidosPorUsuario("67890-usuario-id");

        assertThat(result).isEmpty();
    }
}
