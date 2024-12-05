package com.vilu.pombo.service;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.mock.UsuarioMockFactory;
import com.vilu.pombo.model.repository.PruuRepository;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.PruuSeletor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@DataJpaTest
public class PruuServiceTest {

    @Mock
    private PruuRepository pruuRepository;

    @InjectMocks
    private PruuService pruuService;

    @Mock
    private UsuarioRepository usuarioRepository;
    @Mock
    private AuthService authService;

    @AfterEach
    public void tearDown() {
        pruuRepository.deleteAll();
        usuarioRepository.deleteAll();
    }

    // Casos de teste para cadastrar
    @Test
    @DisplayName("Deve postar um Pruu com sucesso")
    public void deveCadastrarPruuComSucesso() throws PomboException {
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id");
        usuario.setNome("Teste");
        usuario.setEmail("teste@teste.com");
        usuario.setCpf("123.456.789-00");
        usuario.setSenha("senha123");
        usuario.setAdmin(false);

        Pruu pruu = new Pruu();
        pruu.setTexto("Texto do Pruu");
        pruu.setUsuario(usuario);

        when(usuarioRepository.findById("usuario-id")).thenReturn(Optional.of(usuario));
        when(pruuRepository.save(any(Pruu.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Pruu pruuSalvo = pruuService.cadastrar(pruu);

        assertNotNull(pruuSalvo);
        assertEquals("Texto do Pruu", pruuSalvo.getTexto());
        assertEquals("usuario-id", pruuSalvo.getUsuario().getId());
        verify(pruuRepository, times(1)).save(pruu);
        verify(usuarioRepository, times(1)).findById("usuario-id");
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao postar um Pruu")
    public void deveLancarExcecaoUsuarioNaoEncontradoAoCadastrar() {
        // Arrange
        Pruu pruu = new Pruu();
        Usuario usuario = new Usuario();
        usuario.setId("usuario-invalido");
        pruu.setUsuario(usuario);

        when(usuarioRepository.findById("usuario-invalido")).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> pruuService.cadastrar(pruu));
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById("usuario-invalido");
        verify(pruuRepository, never()).save(any(Pruu.class));
    }

    // Casos de teste para toggleCurtida
    @Test
    @DisplayName("Deve alternar a curtida de um Pruu com sucesso")
    public void deveAlternarCurtidaComSucesso() throws PomboException {
        String idPruu = "pruu-id";
        Usuario usuario = new Usuario();
        usuario.setId("usuario-id");

        Pruu pruu = new Pruu();
        pruu.setId(idPruu);
        pruu.setUsuariosQueCurtiram(new ArrayList<>());
        pruu.setQuantidadeCurtidas(0);

        when(authService.getUsuarioAutenticado()).thenReturn(usuario);
        when(pruuRepository.findById(idPruu)).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(usuario.getId())).thenReturn(Optional.of(usuario));

        pruuService.toggleCurtida(idPruu);

        assertEquals(1, pruu.getQuantidadeCurtidas());
        assertTrue(pruu.getUsuariosQueCurtiram().contains(usuario));
        verify(pruuRepository).save(pruu);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o Pruu não for encontrado ao alternar curtida")
    public void deveLancarExcecaoPruuNaoEncontradoAoAlternarCurtida() {
        String idPruu = "pruu-id";
        when(pruuRepository.findById(idPruu)).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> pruuService.toggleCurtida(idPruu));
        assertEquals("O pruu selecionado não foi encontrado.", exception.getMessage());
        verify(pruuRepository, never()).save(any(Pruu.class));
    }

    // Casos de teste para excluir
    @Test
    @DisplayName("Deve excluir um Pruu com sucesso")
    public void deveExcluirPruuComSucesso() throws PomboException {
        String idPruu = "pruu123";
        String idUsuario = "user123";

        Usuario usuario = new Usuario();
        usuario.setId(idUsuario);
        usuario.setAdmin(false);

        Pruu pruu = new Pruu();
        pruu.setId(idPruu);
        pruu.setUsuario(usuario);

        when(pruuRepository.findById(idPruu)).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(idUsuario)).thenReturn(Optional.of(usuario));

        pruuService.excluir(idPruu, idUsuario);

        assertTrue(pruu.isBloqueado(), "O pruu deve estar marcado como bloqueado.");
        verify(pruuRepository, times(1)).save(pruu);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for ADMIN ou autor ao excluir")
    public void deveLancarExcecaoUsuarioSemPermissaoAoExcluir() {
        String idPruu = "pruu123";
        String idUsuario = "user123";
        String idOutroUsuario = "user456";

        Usuario usuario = new Usuario();
        usuario.setId(idOutroUsuario);
        usuario.setAdmin(false);

        Usuario autorPruu = new Usuario();
        autorPruu.setId(idUsuario);

        Pruu pruu = new Pruu();
        pruu.setId(idPruu);
        pruu.setUsuario(autorPruu);

        when(pruuRepository.findById(idPruu)).thenReturn(Optional.of(pruu));
        when(usuarioRepository.findById(idOutroUsuario)).thenReturn(Optional.of(usuario));

        PomboException exception = assertThrows(PomboException.class, () -> pruuService.excluir(idPruu, idOutroUsuario));
        assertEquals("Você não é o Pombo dono deste Pruu, portanto não pode excluí-lo.", exception.getMessage());
        verify(pruuRepository, never()).save(any(Pruu.class));
    }

    // Casos de teste para pesquisarPorId
    @Test
    @DisplayName("Deve retornar um Pruu por ID com sucesso")
    public void deveRetornarPruuPorIdComSucesso() throws PomboException {
        Pruu pruu = new Pruu();
        pruu.setId("1");

        when(pruuRepository.findById("1")).thenReturn(Optional.of(pruu));

        Pruu result = pruuService.pesquisarPorId("1");

        assertNotNull(result);
        assertEquals("1", result.getId());
        verify(pruuRepository, times(1)).findById("1");
    }

    @Test
    @DisplayName("Deve lançar PomboException se o Pruu estiver bloqueado ao pesquisar por ID")
    public void deveLancarExcecaoPruuBloqueadoAoPesquisarPorId() {
        Pruu pruu = new Pruu();
        pruu.setId("1");
        pruu.setBloqueado(true);

        when(pruuRepository.findById("1")).thenReturn(Optional.of(pruu));

        PomboException thrown = assertThrows(PomboException.class, () -> {
            pruuService.pesquisarPorId("1");
        });

        assertEquals("Este pruu foi capturado e não está mais disponível.", thrown.getMessage());
        verify(pruuRepository, times(1)).findById("1");
    }

    // Casos de teste para pesquisarTodos
    @Test
    @DisplayName("Deve retornar todos os Pruus com sucesso")
    public void deveRetornarTodosPruusComSucesso() {
        List<Pruu> pruus = new ArrayList<>();
        pruus.add(new Pruu());
        pruus.add(new Pruu());
        pruus.add(new Pruu());

        when(pruuRepository.findAll()).thenReturn(pruus);

        List<Pruu> result = pruuService.pesquisarTodos();

        assertNotNull(result);
        assertFalse(result.isEmpty());
        assertEquals(3, result.size());
        verify(pruuRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver Pruus")
    public void deveRetornarListaVaziaSeNaoHouverPruus() {
        List<Pruu> pruus = new ArrayList<>();

        when(pruuRepository.findAll()).thenReturn(pruus);

        List<Pruu> result = pruuService.pesquisarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(pruuRepository, times(1)).findAll();
    }

    // Casos de teste para pesquisarComFiltros
    @Test
    @DisplayName("Deve retornar uma lista de Pruus filtrados com sucesso")
    public void deveRetornarPruusFiltradosComSucesso() throws PomboException {
        PruuSeletor seletor = new PruuSeletor();
        seletor.setTexto("test");
        seletor.setBloqueado(false);

        Pruu pruu1 = new Pruu();
        pruu1.setTexto("Test Pruu 1");

        Pruu pruu2 = new Pruu();
        pruu2.setTexto("Test Pruu 2");

        List<Pruu> mockPruus = List.of(pruu1, pruu2);
        Page<Pruu> mockPage = new PageImpl<>(mockPruus);

        when(pruuRepository.findAll(any(Specification.class), any(Pageable.class))).thenReturn(mockPage);

        List<Pruu> result = pruuService.pesquisarComFiltros(seletor);

        assertNotNull(result);
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver Pruus filtrados")
    public void deveRetornarListaVaziaSeNaoHouverPruusFiltrados() throws PomboException {
        PruuSeletor seletor = new PruuSeletor();
        seletor.setTexto("test");
        seletor.setBloqueado(false);

        List<Pruu> pruusVazios = new ArrayList<>();

        Mockito.when(pruuRepository.findAll(Mockito.eq(seletor))).thenReturn(pruusVazios);

        List<Pruu> resultado = pruuService.pesquisarComFiltros(seletor);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }

    // Casos de teste para pesquisarUsuariosQueCurtiram
    @Test
    @DisplayName("Deve retornar a lista de usuários que curtiram um Pruu com sucesso")
    public void deveRetornarUsuariosQueCurtiramComSucesso() throws PomboException {
        String pruuId = "pruu-123";
        List<Usuario> usuariosQueCurtiram = Arrays.asList(UsuarioMockFactory.criarUsuarioMock(), UsuarioMockFactory.criarUsuarioMock());

        Pruu pruu = new Pruu();
        pruu.setId(pruuId);
        pruu.setUsuariosQueCurtiram(usuariosQueCurtiram);

        when(pruuRepository.findById(pruuId)).thenReturn(Optional.of(pruu));

        List<Usuario> resultado = pruuService.pesquisarUsuariosQueCurtiram(pruuId);

        assertNotNull(resultado);
        assertEquals(2, resultado.size());
        assertEquals("Fernando Alonso", resultado.get(0).getNome());
        assertEquals("Fernando Alonso", resultado.get(1).getNome());
        verify(pruuRepository, times(1)).findById(pruuId);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o Pruu não for encontrado ao pesquisar usuários que curtiram")
    public void deveLancarExcecaoPruuNaoEncontradoAoPesquisarUsuariosQueCurtiram() {
        String pruuId = "pruu-inexistente";

        when(pruuRepository.findById(pruuId)).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> {
            pruuService.pesquisarUsuariosQueCurtiram(pruuId);
        });

        assertEquals("Pruu não encontrado.", exception.getMessage());
        verify(pruuRepository, times(1)).findById(pruuId);
    }

    // Casos de teste para pesquisarPruusCurtidosPeloUsuario
    @Test
    @DisplayName("Deve retornar a lista de Pruus curtidos por um usuário com sucesso")
    public void deveRetornarPruusCurtidosPeloUsuarioComSucesso() throws PomboException {
        String usuarioId = "usuario-123";
        Usuario usuario = new Usuario();
        usuario.setId(usuarioId);

        Pruu pruu1 = new Pruu();
        Pruu pruu2 = new Pruu();
        usuario.setPruus(Arrays.asList(pruu1, pruu2));

        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.of(usuario));

        List<Pruu> pruusCurtidos = pruuService.pesquisarPruusCurtidosPeloUsuario(usuarioId);

        assertNotNull(pruusCurtidos);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao pesquisar Pruus curtidos")
    public void deveLancarExcecaoUsuarioNaoEncontradoAoPesquisarPruusCurtidos() {
        String usuarioId = "usuario-inexistente";
        when(usuarioRepository.findById(usuarioId)).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> {
            pruuService.pesquisarPruusCurtidosPeloUsuario(usuarioId);
        });

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(usuarioId);
    }

    // Casos de teste para contarPaginas
    @Test
    @DisplayName("Deve retornar o número correto de páginas para os Pruus filtrados")
    public void deveRetornarNumeroCorretoDePaginasComSucesso() {
        PruuSeletor seletor = new PruuSeletor();
        seletor.setLimite(5);
        seletor.setPagina(1);

        Page<Pruu> paginaMock = new PageImpl<>(List.of(new Pruu(), new Pruu(), new Pruu()), PageRequest.of(0, 5), 12);

        when(pruuRepository.findAll(eq(seletor), any(PageRequest.class))).thenReturn(paginaMock);

        int totalPaginas = pruuService.contarPaginas(seletor);

        assertEquals(3, totalPaginas);
    }

    @Test
    @DisplayName("Deve retornar 0 páginas se não houver registros")
    public void deveRetornarZeroPaginasSemRegistros() {
        PruuSeletor seletor = new PruuSeletor();

        when(pruuRepository.count(seletor)).thenReturn(0L);

        int totalPaginas = pruuService.contarPaginas(seletor);

        assertEquals(0, totalPaginas);
    }

}
