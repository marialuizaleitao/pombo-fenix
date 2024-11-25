package com.vilu.pombo.service;

import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.factory.PruuFactory;
import com.vilu.pombo.factory.UsuarioFactory;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ActiveProfiles("test")
public class PruuServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DenunciaService denunciaService;

    @InjectMocks
    private PruuService pruuService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void inserir_DeveSalvarPruu_QuandoUsuarioExiste() throws PomboException {
        Usuario usuario = UsuarioFactory.criarUsuarioAdmin();
        Pruu pruu = PruuFactory.criarPruuComUsuario(usuario);
        when(usuarioRepository.findById(usuario.getUuid())).thenReturn(Optional.of(usuario));
        when(pruuRepository.save(pruu)).thenReturn(pruu);

        Pruu resultado = pruuService.inserir(pruu);

        assertNotNull(resultado);
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    void inserir_DeveLancarExcecao_QuandoUsuarioNaoExiste() {
        Pruu pruu = PruuFactory.criarPruuComUsuario(null);
        when(usuarioRepository.findById(anyString())).thenReturn(Optional.empty());

        assertThrows(NullPointerException.class, () -> pruuService.inserir(pruu));
    }

    @Test
    void bloquear_DeveBloquearPruu_QuandoUsuarioAdminEExistemDenuncias() throws PomboException {
        Usuario usuario = UsuarioFactory.criarUsuarioAdmin();
        Pruu pruu = PruuFactory.criarPruuComUsuario(usuario);

        when(usuarioRepository.findById(usuario.getUuid())).thenReturn(Optional.of(usuario));
        when(denunciaService.listarDenunciasPorIdPruu(pruu.getUuid()))
                .thenReturn(List.of(new Denuncia()));
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));
        when(pruuRepository.save(pruu)).thenReturn(pruu);

        Pruu resultado = pruuService.bloquear(pruu.getUuid(), usuario.getUuid());

        assertNotNull(resultado);
        assertTrue(resultado.isBloqueado());
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    void bloquear_DeveLancarExcecao_QuandoNaoExistemDenuncias() {
        Usuario usuario = UsuarioFactory.criarUsuarioAdmin();
        Pruu pruu = PruuFactory.criarPruuComUsuario(usuario);

        when(usuarioRepository.findById(usuario.getUuid())).thenReturn(Optional.of(usuario));
        when(denunciaService.listarDenunciasPorIdPruu(pruu.getUuid()))
                .thenReturn(List.of());
        when(pruuRepository.findById(pruu.getUuid())).thenReturn(Optional.of(pruu));

        assertThrows(PomboException.class, () -> pruuService.bloquear(pruu.getUuid(), usuario.getUuid()));
    }

}
