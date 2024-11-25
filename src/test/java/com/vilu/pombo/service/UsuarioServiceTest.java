package com.vilu.pombo.service;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.factory.UsuarioFactory;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private PruuRepository pruuRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserir_DeveSalvarUsuario_QuandoCpfNaoExiste() throws PomboException {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);

        usuarioService.inserir(usuario);

        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    void inserir_DeveLancarExcecao_QuandoCpfJaExiste() {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        assertThrows(PomboException.class, () -> usuarioService.inserir(usuario));
    }

    @Test
    void curtir_DeveAdicionarLike_QuandoUsuarioAindaNaoCurtiu() throws PomboException {
        Usuario usuario = UsuarioFactory.criarUsuarioMaxVerstappen();
        String idPruu = "pruuId";

        when(usuarioRepository.findById(usuario.getUuid())).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(usuario)).thenReturn(usuario);

        Pruu pruu = new Pruu();
        pruu.setUuid(idPruu);
        pruu.setQuantidadeLikes(0);

        when(pruuRepository.findById(idPruu)).thenReturn(Optional.of(pruu));
        when(pruuRepository.save(pruu)).thenReturn(pruu);

        usuarioService.curtir(usuario.getUuid(), idPruu);

        verify(usuarioRepository, times(1)).save(usuario);

        verify(pruuRepository, times(1)).save(pruu);
    }

}
