package com.vilu.pombo.service;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Motivo;
import com.vilu.pombo.model.enums.StatusDenuncia;
import com.vilu.pombo.model.repository.DenunciaRepository;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.DenunciaSeletor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DenunciaServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private DenunciaRepository denunciaRepository;

    @InjectMocks
    private DenunciaService denunciaService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Deve cadastrar uma denúncia com sucesso")
    void deveCadastrarDenunciaComSucesso() throws PomboException {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id");

        Pruu pruu = new Pruu();
        pruu.setId("pruu-id");

        Denuncia denuncia = new Denuncia();
        denuncia.setUsuario(usuario);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(Motivo.SPAM);

        when(pruuRepository.findById("pruu-id")).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById("usuario-id")).thenReturn(Optional.of(usuario));
        when(denunciaRepository.save(denuncia)).thenReturn(denuncia);

        Denuncia denunciaCadastrada = denunciaService.cadastrar(denuncia);

        assertNotNull(denunciaCadastrada);
        assertEquals(usuario, denunciaCadastrada.getUsuario());
        assertEquals(pruu, denunciaCadastrada.getPruu());
        assertEquals(Motivo.SPAM, denunciaCadastrada.getMotivo());
        assertEquals(StatusDenuncia.PENDENTE, denunciaCadastrada.getStatus());

        verify(pruuRepository).findById("pruu-id");
        verify(usuarioRepository).findById("usuario-id");
        verify(denunciaRepository).save(denuncia);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao cadastrar uma denúncia")
    void deveLancarExcecaoUsuarioNaoEncontradoAoCadastrar() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id-inexistente");

        Pruu pruu = new Pruu();
        pruu.setId("pruu-id");

        Denuncia denuncia = new Denuncia();
        denuncia.setUsuario(usuario);
        denuncia.setPruu(pruu);
        denuncia.setMotivo(Motivo.SPAM);

        when(pruuRepository.findById("pruu-id")).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById("usuario-id-inexistente")).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> {
            denunciaService.cadastrar(denuncia);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());

        verify(pruuRepository).findById("pruu-id");
        verify(usuarioRepository).findById("usuario-id-inexistente");
        verify(denunciaRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve atualizar o status de uma denúncia com sucesso")
    void deveAtualizarDenunciaComSucesso() throws PomboException {
        // Preparar dados de teste
        Pruu pruu = new Pruu();
        pruu.setId("pruu-id");
        pruu.setBloqueado(false);

        Denuncia denuncia = new Denuncia();
        denuncia.setId("denuncia-id");
        denuncia.setPruu(pruu);
        denuncia.setStatus(StatusDenuncia.PENDENTE);

        // Configurar comportamento dos mocks
        when(denunciaRepository.findById("denuncia-id")).thenReturn(Optional.of(denuncia));
        when(pruuRepository.save(any(Pruu.class))).thenReturn(pruu);
        when(denunciaRepository.save(any(Denuncia.class))).thenReturn(denuncia);

        // Executar método de atualização
        assertDoesNotThrow(() -> denunciaService.atualizar("denuncia-id", StatusDenuncia.ACEITA));

        // Verificações
        verify(denunciaRepository).findById("denuncia-id");
        verify(pruuRepository).save(pruu);
        verify(denunciaRepository).save(denuncia);

        assertEquals(StatusDenuncia.ACEITA, denuncia.getStatus());
        assertTrue(pruu.isBloqueado());
    }

    @Test
    @DisplayName("Deve lançar PomboException se a denúncia não for encontrada ao atualizar")
    void deveLancarExcecaoDenunciaNaoEncontradaAoAtualizar() {
        when(denunciaRepository.findById("denuncia-id-inexistente")).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> {
            denunciaService.atualizar("denuncia-id-inexistente", StatusDenuncia.ACEITA);
        });

        assertEquals("Denúncia não encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve excluir uma denúncia com sucesso")
    void deveExcluirDenunciaComSucesso() throws PomboException {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id");

        Denuncia denuncia = new Denuncia();
        denuncia.setId("denuncia-id");
        denuncia.setUsuario(usuario);

        when(denunciaRepository.findById("denuncia-id")).thenReturn(Optional.of(denuncia));

        assertDoesNotThrow(() -> denunciaService.excluir("denuncia-id", "usuario-id"));

        // Verificações
        verify(denunciaRepository).findById("denuncia-id");
        verify(denunciaRepository).deleteById("denuncia-id");
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for autor ao excluir")
    void deveLancarExcecaoUsuarioSemPermissaoAoExcluir() {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id-autor");

        Denuncia denuncia = new Denuncia();
        denuncia.setId("denuncia-id");
        denuncia.setUsuario(usuario);

        when(denunciaRepository.findById("denuncia-id")).thenReturn(Optional.of(denuncia));

        PomboException exception = assertThrows(PomboException.class, () -> {
            denunciaService.excluir("denuncia-id", "usuario-id-diferente");
        });

        assertEquals("Não é possível excluir denúncias que não foram feitas por você.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve lançar PomboException se a denúncia não existir ao pesquisar por ID")
    void deveLancarExcecaoDenunciaNaoExisteAoPesquisarPorId() {
        when(denunciaRepository.findById("denuncia-id-inexistente")).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> {
            denunciaService.pesquisarPorId("denuncia-id-inexistente");
        });

        assertEquals("A denúncia buscada não foi encontrada.", exception.getMessage());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver denúncias")
    void deveRetornarListaVaziaSeNaoHouverDenuncias() {
        when(denunciaRepository.findAll()).thenReturn(Collections.emptyList());

        List<DenunciaDTO> denunciasDTO = denunciaService.pesquisarTodas();

        assertNotNull(denunciasDTO);
        assertTrue(denunciasDTO.isEmpty());
        verify(denunciaRepository).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista de denúncias filtradas com sucesso")
    void deveRetornarDenunciasFiltradasComSucesso() {
        DenunciaSeletor seletor = new DenunciaSeletor();
        seletor.setPagina(1);
        seletor.setLimite(10);

        List<Denuncia> denuncias = new ArrayList<>();
        Denuncia denuncia1 = new Denuncia();
        Denuncia denuncia2 = new Denuncia();
        denuncias.add(denuncia1);
        denuncias.add(denuncia2);

        Page<Denuncia> paginaDenuncias = new PageImpl<>(denuncias);

        when(denunciaRepository.findAll(any(DenunciaSeletor.class), any(PageRequest.class))).thenReturn(paginaDenuncias);

        List<DenunciaDTO> denunciasDTO = denunciaService.pesquisarComFiltros(seletor);

        assertNotNull(denunciasDTO);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver denúncias filtradas")
    void deveRetornarListaVaziaSeNaoHouverDenunciasFiltradas() {
        DenunciaSeletor seletor = new DenunciaSeletor();
        seletor.setPagina(1);
        seletor.setLimite(10);

        when(denunciaRepository.findAll(any(DenunciaSeletor.class), any(PageRequest.class))).thenReturn(Page.empty());

        List<DenunciaDTO> denunciasDTO = denunciaService.pesquisarComFiltros(seletor);

        assertNotNull(denunciasDTO);
        assertTrue(denunciasDTO.isEmpty());
        verify(denunciaRepository).findAll(any(DenunciaSeletor.class), any(PageRequest.class));
    }

    @Test
    @DisplayName("Deve atualizar o status de um Pruu com sucesso")
    void deveAtualizarStatusPruuComSucesso() {
        Pruu pruu = new Pruu();
        pruu.setBloqueado(false);

        denunciaService.atualizarStatusPruu(pruu, StatusDenuncia.PENDENTE, StatusDenuncia.ACEITA);

        assertTrue(pruu.isBloqueado());
        verify(pruuRepository).save(pruu);
    }

    @Test
    @DisplayName("Deve desbloquear Pruu quando denúncia aceita for rejeitada")
    void deveDesbloquearPruuQuandoDenunciaAceitaForRejeitada() {
        Pruu pruu = new Pruu();
        pruu.setBloqueado(true);

        denunciaService.atualizarStatusPruu(pruu, StatusDenuncia.ACEITA, StatusDenuncia.REJEITADA);

        assertFalse(pruu.isBloqueado());
        verify(pruuRepository).save(pruu);
    }
}
