package com.vilu.pombo.service;

import org.junit.jupiter.api.AfterEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import com.vilu.pombo.model.repository.UsuarioRepository;

@SpringBootTest
@ActiveProfiles("test")
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
    
    // Caso de erro
    @Test
    @DisplayName("Should not be able to return a UserDetails when user email is not found")
    
    // Casos de teste para cadastrar
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to register user")
    
    // Caso de erro: email cadastrado já existe
    @Test
    @DisplayName("Should not be able to register a user with a used email")
    
    // Caso de erro: CPF cadastrado já existe
    @Test
    @DisplayName("Should not be able to register a user with a used CPF")
    
    // Casos de teste para cadastrarAdmin
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to register admin")
    
    // Caso de erro: email cadastrado já existe
    @Test
    @DisplayName("Should not be able to register admin with a used email")
    
    // Caso de erro: CPF cadastrado já existe
    @Test
    @DisplayName("Should not be able to register admin with a used CPF")
    
    // Casos de teste para atualizar
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to update user")
    
    // Caso de erro: usuário não encontrado
    @Test
    @DisplayName("Should not be able to update a nonexistent user")
    
    // Casos de teste para excluir
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to delete user")
    
    // Caso de erro: usuário não encontrado
    @Test
    @DisplayName("Should not be able to delete a nonexistent user")
    
    // Casos de teste para pesquisarTodos
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to return a list with users")
    
    // Caso de erro: usuários não encontrados
    @Test
    @DisplayName("Should not be able to find users")
    
    // Casos de teste para pesquisarPorId
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to find user by its ID")
    
    // Caso de erro: usuário não encontrado
    @Test
    @DisplayName("Should not be able to find a nonexistent user")
    
    // Casos de teste para pesquisarComFiltros
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to return a list with filtered users")
    
    // Caso de erro: usuários não encontrados
    @Test
    @DisplayName("Should not be able to find users with the selected filters")
    
    // Casos de teste para salvarFotoDePerfil
    // Caso de sucesso
    @Test
    @DisplayName("Should be able to save user profile picture")
    
    // Caso de erro
    @Test
    @DisplayName("Should not be able to process image")
    

}
