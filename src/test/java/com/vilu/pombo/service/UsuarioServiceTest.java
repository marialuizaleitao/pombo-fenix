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
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to return a UserDetails when user is found by its email")
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


    // Caso de erro
    @Test
    @DisplayName("Should not be able to return a UserDetails when user email is not found")
    void testLoadUserByUsername_UserNotFound() {
        String username = "alan@prost.com";
        when(usuarioRepository.findByEmail(username)).thenReturn(Optional.empty());

        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> usuarioService.loadUserByUsername(username)
        );

        assertEquals("Usuário não encontrado " + username, exception.getMessage());
        verify(usuarioRepository, times(1)).findByEmail(username);
    }

    // Casos de teste para cadastrar
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to register user")
    void testCadastrar_Success() throws PomboException {
        Usuario usuario = new Usuario();
        usuario.setEmail("max@verstappen.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);

        usuarioService.cadastrar(usuario);

        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, times(1)).save(usuario);
    }

    // Caso de erro: email cadastrado já existe
    @Test
    @DisplayName("Should not be able to register a user with a used email")
    void testCadastrar_EmailAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("existente@alpine.com");
        usuario.setCpf("610.813.640-50");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(true);

        PomboException exception = assertThrows(
                PomboException.class,
                () -> usuarioService.cadastrar(usuario)
        );

        assertEquals("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, never()).existsByCpf(anyString());
        verify(usuarioRepository, never()).save(any());
    }


    // Caso de erro: CPF cadastrado já existe
    @Test
    @DisplayName("Should not be able to register a user with a used CPF")
    void testCadastrar_CpfAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("sergio@perez.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        // Act & Assert
        PomboException exception = assertThrows(
                PomboException.class,
                () -> usuarioService.cadastrar(usuario)
        );

        assertEquals("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para cadastrarAdmin
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to register admin")
    void testCadastrarAdmin_Success() throws PomboException {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(false);

        usuarioService.cadastrarAdmin(usuario);

        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, times(1)).save(usuario);
    }


    // Caso de erro: email cadastrado já existe
    @Test
    @DisplayName("Should not be able to register admin with a used email")
    void testCadastrarAdmin_EmailAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("adminexistente@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(true);

        PomboException exception = assertThrows(
                PomboException.class,
                () -> usuarioService.cadastrarAdmin(usuario)
        );

        assertEquals("O e-mail informado já está cadastrado. Por favor, utilize um e-mail diferente.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, never()).existsByCpf(anyString());
        verify(usuarioRepository, never()).save(any());
    }


    // Caso de erro: CPF cadastrado já existe
    @Test
    @DisplayName("Should not be able to register admin with a used CPF")
    void testCadastrarAdmin_CpfAlreadyExists() {
        Usuario usuario = new Usuario();
        usuario.setEmail("admin@example.com");
        usuario.setCpf("866.980.570-70");

        when(usuarioRepository.existsByEmailIgnoreCase(usuario.getEmail())).thenReturn(false);
        when(usuarioRepository.existsByCpf(usuario.getCpf())).thenReturn(true);

        PomboException exception = assertThrows(
                PomboException.class,
                () -> usuarioService.cadastrarAdmin(usuario)
        );

        assertEquals("O CPF informado já está registrado. Por favor, verifique os dados ou utilize outro CPF.", exception.getMessage());
        verify(usuarioRepository, times(1)).existsByEmailIgnoreCase(usuario.getEmail());
        verify(usuarioRepository, times(1)).existsByCpf(usuario.getCpf());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para atualizar
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to update user")
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

    // Caso de erro: usuário não encontrado
    @Test
    @DisplayName("Should not be able to update a nonexistent user")
    void testAtualizar_UserNotFound() {
        Usuario usuarioAtualizado = new Usuario();
        usuarioAtualizado.setId("123");
        usuarioAtualizado.setNome("Nome Novo");
        usuarioAtualizado.setEmail("email.novo@example.com");

        when(usuarioRepository.findById(usuarioAtualizado.getId())).thenReturn(Optional.empty());

        PomboException exception = assertThrows(
                PomboException.class,
                () -> usuarioService.atualizar(usuarioAtualizado)
        );

        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(usuarioAtualizado.getId());
        verify(usuarioRepository, never()).save(any());
    }

    // Casos de teste para excluir
    // Caso de sucesso
//    @Test
//    @DisplayName("Should be able to delete user")
//    void testExcluir_Success() throws PomboException {}

    // Caso de erro: usuário não encontrado
//    @Test
//    @DisplayName("Should not be able to delete a nonexistent user")
//    void testExcluir_UserNotFound() {}

    // Casos de teste para pesquisarTodos
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to return a list with users")
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

    // Caso de erro: usuários não encontrados
    @Test
    @DisplayName("Should not be able to find users")
    void testPesquisarTodos_NoUsersFound() {
        List<Usuario> usuarios = new ArrayList<>();

        when(usuarioRepository.findAll()).thenReturn(usuarios);

        List<Usuario> result = usuarioService.pesquisarTodos();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(usuarioRepository, times(1)).findAll();
    }

    // Casos de teste para pesquisarPorId
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to find user by its ID")
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

    // Caso de erro: usuário não encontrado
    @Test
    @DisplayName("Should not be able to find a nonexistent user")
    void testPesquisarPorId_UserNotFound() {
        String id = "1";

        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        PomboException exception = assertThrows(PomboException.class, () -> usuarioService.pesquisarPorId(id));
        assertEquals("Usuário não encontrado.", exception.getMessage());
        verify(usuarioRepository, times(1)).findById(id);
    }

    // Casos de teste para pesquisarComFiltros
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to return a list with filtered users")
    public void testPesquisarComFiltros_ShouldReturnFilteredUsers() {
        UsuarioSeletor seletor = new UsuarioSeletor();
        seletor.setNome("Fernando");
        seletor.setEmail("fernando.alonso@example.com");

        List<Usuario> usuariosMock = List.of(
                UsuarioMockFactory.criarUsuarioMock()
        );

        Mockito.when(usuarioRepository.findAll(Mockito.eq(seletor))).thenReturn(usuariosMock);

        List<Usuario> resultado = usuarioService.pesquisarComFiltros(seletor);

        assertNotNull(resultado);
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
        assertEquals("Fernando Alonso", resultado.get(0).getNome());
        assertEquals("fernando@alonso.com", resultado.get(0).getEmail());
    }

    // Caso de erro: usuários não encontrados
    @Test
    @DisplayName("Should not be able to find users with the selected filters")
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