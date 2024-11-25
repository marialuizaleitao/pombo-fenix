package com.vilu.pombo.model.repository;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.mock.UsuarioMockFactory;
import jakarta.validation.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
public class UsuarioRepositoryTest {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Test
    @DisplayName("Não deve ser possível cadastrar um usuário com nome inválido")
    public void testInsert$nameMoreThan200Characters() {
        String name = "a";
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setNome(name.repeat(201));

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um usuário com email inválido")
    public void testInsert$userWithInvalidEmail() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setEmail("bolinha");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um usuário com CPF inválido")
    public void testInsert$userWithInvalidCpf() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setCpf("123.456.789-10");

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario)).isInstanceOf(ConstraintViolationException.class);
    }

    @Test
    @DisplayName("Não deve ser possível cadastrar um usuário sem senha")
    public void testInsert$userWithoutPassword() {
        Usuario usuario = UsuarioMockFactory.criarUsuarioMock();
        usuario.setSenha(null);

        assertThatThrownBy(() -> usuarioRepository.saveAndFlush(usuario)).isInstanceOf(ConstraintViolationException.class);
    }
}