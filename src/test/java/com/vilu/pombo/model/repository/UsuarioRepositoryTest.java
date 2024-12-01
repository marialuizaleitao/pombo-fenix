package com.vilu.pombo.model.repository;

import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.mock.UsuarioMockFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@DataJpaTest
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @MockBean
    private UsuarioRepository mockUsuarioRepository;

    private Usuario usuario;

    @BeforeEach
    public void setUp() {
        usuario = UsuarioMockFactory.criarUsuarioMock();
    }

    // Casos de teste para existsByEmailIgnoreCase
    // Caso de sucesso
    @Test
    public void testExistsByEmailIgnoreCase_Success() {
        when(mockUsuarioRepository.existsByEmailIgnoreCase("fernando@alonso.com")).thenReturn(true);
        boolean exists = usuarioRepository.existsByEmailIgnoreCase("fernando@alonso.com");
        assertThat(exists).isTrue();
    }

    // Caso de erro
    @Test
    public void testExistsByEmailIgnoreCase_Failure() {
        when(mockUsuarioRepository.existsByEmailIgnoreCase("naoexistente@exemplo.com")).thenReturn(false);
        boolean exists = usuarioRepository.existsByEmailIgnoreCase("naoexistente@exemplo.com");
        assertThat(exists).isFalse();
    }

    // Casos de teste para findByEmail
    // Caso de sucesso
    @Test
    public void testFindByEmail_Success() {
        when(mockUsuarioRepository.findByEmail("fernando@alonso.com")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioRepository.findByEmail("fernando@alonso.com");
        assertThat(result).isPresent();
        assertThat(result.get().getEmail()).isEqualTo("fernando@alonso.com");
    }

    // Caso de erro
    @Test
    public void testFindByEmail_Failure() {
        when(mockUsuarioRepository.findByEmail("naoexistente@exemplo.com")).thenReturn(Optional.empty());
        Optional<Usuario> result = usuarioRepository.findByEmail("naoexistente@exemplo.com");
        assertThat(result).isEmpty();
    }

    // Casos de teste para existsByCpf
    // Caso de sucesso
    @Test
    public void testExistsByCpf_Success() {
        when(mockUsuarioRepository.existsByCpf("158.534.520-29")).thenReturn(true);
        boolean exists = usuarioRepository.existsByCpf("158.534.520-29");
        assertThat(exists).isTrue();
    }

    // Caso de erro
    @Test
    public void testExistsByCpf_Failure() {
        when(mockUsuarioRepository.existsByCpf("987.654.321-00")).thenReturn(false);
        boolean exists = usuarioRepository.existsByCpf("987.654.321-00");
        assertThat(exists).isFalse();
    }

    // Casos de teste para findById
    // Caso de sucesso
    @Test
    public void testFindById_Success() {
        when(mockUsuarioRepository.findById("12345-uuid")).thenReturn(Optional.of(usuario));
        Optional<Usuario> result = usuarioRepository.findById("12345-uuid");
        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo("12345-uuid");
    }

    // Caso de erro
    @Test
    public void testFindById_Failure() {
        when(mockUsuarioRepository.findById("67890-uuid")).thenReturn(Optional.empty());
        Optional<Usuario> result = usuarioRepository.findById("67890-uuid");
        assertThat(result).isEmpty();
    }
}
