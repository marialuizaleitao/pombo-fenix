package com.vilu.pombo.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import com.vilu.pombo.model.enums.Perfil;
import com.vilu.pombo.model.mock.UsuarioMockFactory;
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
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.repository.UsuarioRepository;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import org.springframework.web.multipart.MultipartFile;

@DataJpaTest
public class UsuarioServiceTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioService usuarioService;

    @AfterEach
    public void tearDown() {
        usuarioRepository.deleteAll();
    }

    // Casos de teste para loadUserByUsername
    @Test
    @DisplayName("Deve retornar um UserDetails ao buscar um usuário pelo e-mail")
    void testLoadUserByUsername_Success() {
        String username = "lewis@hamilton.com";
        Usuario mockUsuario = new Usuario();
        mockUsuario.setEmail(username);
        when(usuarioRepository.findByEmail(username)).thenReturn(Optional.of(mockUsuario));

        UserDetails result = usuarioService.loadUserByUsername(username);

        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(usuarioRepository, times(1)).findByEmail(username);
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException ao buscar um usuário inexistente pelo e-mail")
    void testLoadUserByUsername_UserNotFound() {
        String username = "alan@prost.com";
        when(usuarioRepository.findByEmail(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(UsernameNotFoundException.class, () -> usuarioService.loadUserByUsername(username));

        assertEquals("Usuário não encontrado " + username, exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(username);
    }

    @Test
    @DisplayName("Deve cadastrar um usuário com sucesso")
    void testCadastrar_Success() throws PomboException {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);

        usuarioService.cadastrar(usuario);

        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o e-mail cadastrado já existir")
    void testCadastrar_EmailAlreadyExists() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("existente@alpine.com");
        usuario.setCpf("610.813.640-50");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(true);

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.cadastrar(usuario));

        assertEquals("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, never()).existsByCpf(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar PomboException se o CPF cadastrado já existir")
    void testCadastrar_CpfAlreadyExists() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("sergio@perez.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.cadastrar(usuario));

        assertEquals("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para cadastrarAdmin
    @Test
    @DisplayName("Deve cadastrar um admin com sucesso")
    void testCadastrarAdmin_Success() throws PomboException {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("admin@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);

        usuarioService.cadastrarAdmin(usuario);

        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o e-mail cadastrado já existir")
    void testCadastrarAdmin_EmailAlreadyExists() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("adminexistente@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(true);

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.cadastrarAdmin(usuario));

        assertEquals("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, never()).existsByCpf(anyString());
        verify(usuarioRepository, never()).save(any());
    }

    @Test
    @DisplayName("Deve lançar PomboException se o CPF cadastrado já existir")
    void testCadastrarAdmin_CpfAlreadyExists() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("admin@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.cadastrarAdmin(usuario));

        assertEquals("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para atualizar
    @Test
    @DisplayName("Deve atualizar um usuário com sucesso")
    void testAtualizar_Success() throws PomboException {
        Usuario usuarioExistente = new Usuario();
        usuarioExistente.setId("123");
        usuarioExistente.setNome("Max Verstappen");
        usuarioExistente.setEmail("max@verstappen.com");

        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId("123");
        usuarioAtualizado.setNome("Max Emilian Verstappen");
        usuarioAtualizado.setEmail("max@verstappen.com");

        when(usuarioRepository.findById(usuarioAtualizado.getId())).thenReturn(Optional.of(usuarioExistente));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuarioAtualizado);

        Usuario resultado = usuarioService.atualizar(usuarioAtualizado);

        assertEquals("Max Emilian Verstappen", resultado.getNome());
        assertEquals("max@verstappen.com", resultado.getEmail());
        verify(usuarioRepository, times(1)).findById(usuarioAtualizado.getId());
        verify(usuarioRepository, times(1)).save(usuarioExistente);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao atualizar")
    void testAtualizar_UserNotFound() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId("123");
        usuarioAtualizado.setNome("Nome Novo");
        usuarioAtualizado.setEmail("email.novo@example.com");

        when(usuarioRepository.findById(usuarioAtualizado.getId())).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.atualizar(usuarioAtualizado));

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(usuarioAtualizado.getId());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para pesquisarTodos
    @Test
    @DisplayName("Deve excluir um usuário com sucesso")
    void excluirUsuario_Sucesso() throws PomboException {
        Usuario usuarioMock = UsuarioMockFactory.criarUsuarioMock();
        Mockito.when(usuarioMock.getPruus()).thenReturn(Collections.emptyList());
        Mockito.when(usuarioMock.getDenuncias()).thenReturn(Collections.emptyList());
        Mockito.when(usuarioRepository.findById("12345-uuid")).thenReturn(Optional.of(usuarioMock));

        usuarioService.excluir("12345-uuid");

        verify(usuarioRepository, times(1)).deleteById("12345-uuid");
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao excluir")
    void excluirUsuario_Erro_UsuarioNaoEncontrado() {
        Mockito.when(usuarioRepository.findById("12345-uuid")).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.excluir("12345-uuid"));
        verify(usuarioRepository, never()).deleteById(anyString());
        assertEquals("Usuário não encontrado.", exception.getMessage());
    }

    // Casos de teste para pesquisarTodos
    @Test
    @DisplayName("Deve retornar uma lista com todos os usuários")
    void testPesquisarTodos_Success() {
        Usuario vic = new Usuario();
        vic.setId("1");
        vic.setNome("Vic");
        vic.setEmail("vic@email.com");

        Usuario malu = new Usuario();
        malu.setId("2");
        malu.setNome("Malu");
        malu.setEmail("malu@email.com");

        List<Usuario> usuarios = new ArrayList<>();
        usuarios.add(vic);
        usuarios.add(malu);

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.pesquisarTodos();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Vic", result.get(0).getNome());
        assertEquals("Malu", result.get(1).getNome());
        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver usuários")
    void testPesquisarTodos_NoUsersFound() {
        List<Usuario> usuarios = new ArrayList<>();

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.pesquisarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    // Casos de teste para pesquisarPorId
    @Test
    @DisplayName("Deve retornar um usuário ao pesquisar por ID")
    void testPesquisarPorId_Success() throws PomboException {
        String id = "1";
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNome("Ayrton");
        usuario.setEmail("ayrton@senna.com");

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.pesquisarPorId(id);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("Ayrton", result.getNome());
        assertEquals("ayrton@senna.com", result.getEmail());
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    @DisplayName("Deve lançar PomboException se o usuário não for encontrado ao pesquisar por ID")
    void testPesquisarPorId_UserNotFound() {
        String id = "1";

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.pesquisarPorId(id));
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
    }

    // Casos de teste para pesquisarComFiltros
    @Test
    @DisplayName("Devem retornar usuários filtrados")
    public void testPesquisarComFiltros_ShouldReturnFilteredUsers() {
        UsuarioSeletor seletor = new UsuarioSeletor();
        seletor.setNome("Fernando");
        seletor.setEmail("fernando.alonso@example.com");

        List<Usuario> usuariosMock = List.of(UsuarioMockFactory.criarUsuarioMock());

        Mockito.when(usuarioRepository.findAll(Mockito.eq(seletor))).thenReturn(usuariosMock);

        List<Usuario> resultado = usuarioService.pesquisarComFiltros(seletor);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Fernando Alonso", resultado.get(0).getNome());
        assertEquals("fernando@alonso.com", resultado.get(0).getEmail());
    }

    @Test
    @DisplayName("Deve retornar uma lista vazia se não houver usuários filtrados")
    public void testPesquisarComFiltros_ShouldNotReturnFilteredUsers() {
        UsuarioSeletor seletor = new UsuarioSeletor();
        seletor.setNome("Usuário Inexistente");
        seletor.setEmail("inexistente@example.com");

        List<Usuario> usuariosVazios = new ArrayList<>();

        Mockito.when(usuarioRepository.findAll(Mockito.eq(seletor))).thenReturn(usuariosVazios);

        List<Usuario> resultado = usuarioService.pesquisarComFiltros(seletor);

        assertNotNull(resultado);
        assertTrue(resultado.isEmpty());
    }
}