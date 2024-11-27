package com.vilu.pombo.controller;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.seletor.UsuarioSeletor;
import com.vilu.pombo.service.UsuarioService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/usuarios")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
@MultipartConfig(fileSizeThreshold = 10485760)
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private AuthService authService;

    @PostMapping("/salvar-foto")
    public void salvarFotoDePerfil(@RequestParam("fotoDePerfil") MultipartFile foto) throws IOException, PomboException {
        Usuario subject = authService.getUsuarioAutenticado();
        if (foto == null) {
            throw new PomboException("O arquivo inserido é inválido.", HttpStatus.BAD_REQUEST);
        }
        usuarioService.salvarFotoDePerfil(foto, subject.getId());
    }

    @Operation(summary = "Atualizar um usuário", description = "Atualiza os dados de um usuário existente.")
    @PutMapping(path = "/atualizar")
    public ResponseEntity<Usuario> atualizar(@Valid @RequestBody Usuario usuarioAlterado) throws PomboException {
        return ResponseEntity.ok(usuarioService.atualizar(usuarioAlterado));
    }

    @Operation(summary = "Deletar usuário", description = "Exclui um usuário através do seu ID.", responses = {@ApiResponse(responseCode = "200", description = "Usuário excluído com sucesso"),})
    @DeleteMapping(path = "/excluir/{id}")
    public ResponseEntity<Void> excluir(@PathVariable String id) throws PomboException {
        usuarioService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os usuários", description = "Retorna uma lista de todos os usuários cadastrados no sistema.", responses = {@ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso")})
    @GetMapping(path = "/todos")
    public List<Usuario> pesquisarTodos() {
        return usuarioService.pesquisarTodos();
    }

    @Operation(summary = "Pesquisar usuário por ID", description = "Busca um usuário específico através do seu ID.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Usuario> pesquisarPorId(@PathVariable String id) throws PomboException {
        Usuario usuario = usuarioService.pesquisarPorId(id);
        return ResponseEntity.ok(usuario);
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de usuários de acordo com o filtro selecionado.")
    @PostMapping("/filtrar")
    public List<Usuario> pesquisarComFiltros(@RequestBody UsuarioSeletor seletor) {
        return usuarioService.pesquisarComFiltros(seletor);
    }

    @Operation(summary = "Obter o usuário autenticado", description = "Retorna o usuário autenticado.")
    @GetMapping("/usuario-autenticado")
    public ResponseEntity<Usuario> buscarUsuarioAutenticado() throws PomboException {
        Usuario usuario = authService.getUsuarioAutenticado();
        return ResponseEntity.ok(usuario);
    }

}