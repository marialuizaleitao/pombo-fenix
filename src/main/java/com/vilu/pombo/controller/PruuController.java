package com.vilu.pombo.controller;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.Perfil;
import com.vilu.pombo.model.seletor.PruuSeletor;
import com.vilu.pombo.service.PruuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(path = "/pruus")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class PruuController {

    @Autowired
    private PruuService pruuService;
    @Autowired
    private AuthService authService;

    @PostMapping("/salvar-foto")
    public void salvarImagem(@RequestParam("fotoDePerfil") MultipartFile foto, @RequestParam String idPruu) throws IOException, PomboException {
        Usuario subject = authService.getUsuarioAutenticado();
        if (foto == null) {
            throw new PomboException("O arquivo inserido é inválido.", HttpStatus.BAD_REQUEST);
        }
        pruuService.salvarImagem(foto, idPruu, subject.getId());
    }

    @Operation(summary = "Inserir novo pruu", description = "Adiciona um novo pruu ao sistema.", responses = {@ApiResponse(responseCode = "200", description = "Pruu criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))), @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}")))})
    @PostMapping(path = "/cadastrar")
    public ResponseEntity<Pruu> cadastrar(@Valid @RequestBody Pruu novoPruu) throws PomboException {
        Usuario subject = authService.getUsuarioAutenticado();

        if (subject.getPerfil() == Perfil.USUARIO) {
            novoPruu.setUsuario(subject);
            Pruu pruuCriado = pruuService.cadastrar(novoPruu);
            return ResponseEntity.status(201).body(pruuCriado);
        } else {
            throw new PomboException("Administradores não podem criar Pruus.", HttpStatus.BAD_REQUEST);
        }
    }

    @Operation(summary = "Curtir ou descurtir um pruu", description = "Curte ou descurte um pruu pelo seu ID.", responses = {
            @ApiResponse(responseCode = "200", description = "Pruu curtido/descurtido com sucesso")})
    @PostMapping(path = "/curtir/{idPruu}")
    public ResponseEntity<Void> curtir(@PathVariable String idPruu)
            throws PomboException {
        pruuService.toggleCurtida(idPruu);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bloquear um pru", description = "Bloqueia um pruu pelo seu ID.")
    @DeleteMapping("/excluir/{idPruu}")
    public ResponseEntity<Void> excluir(@PathVariable String idPruu)
            throws PomboException {
        Usuario subject = authService.getUsuarioAutenticado();
        pruuService.excluir(idPruu, subject.getId());
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os pruus", description = "Retorna uma lista de todos os pruus cadastrados no sistema.", responses = {
            @ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso")})
    @PostMapping(path = "/todos")
    public List<Pruu> pesquisarTodos() throws PomboException {
        return pruuService.pesquisarTodos();
    }

    @Operation(summary = "Pesquisar pruu por ID", description = "Busca um pruu específico pelo seu ID.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Pruu> pesquisarPorId(@PathVariable String id) throws PomboException {
        Pruu pruu = pruuService.pesquisarPorId(id);
        return ResponseEntity.ok(pruu);
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de pruus de acordo com o filtro selecionado.", responses = {
            @ApiResponse(responseCode = "200", description = "Pruus filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}")))})
    @PostMapping("/filtrar")
    public List<Pruu> pesquisarComFiltros(@RequestBody PruuSeletor seletor) throws PomboException {
        return pruuService.pesquisarComFiltros(seletor);
    }

    @Operation(summary = "Pesquisar usuários que curtiram", description = "Retorna uma lista de usuários que curtiram determinada postagem.", responses = {
            @ApiResponse(responseCode = "200", description = "Usuários listados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}")))})
    @GetMapping("/usuarios-que-curtiram/{pruuId}")
    public List<Usuario> pesquisarUsuariosQueCurtiram(@PathVariable String pruuId) throws PomboException {
        return pruuService.pesquisarUsuariosQueCurtiram(pruuId);
    }

    @Operation(summary = "Pesquisar pruus que o usuário curtiu", description = "Retorna uma lista de pruus curtidos pelo usuário.", responses = {
            @ApiResponse(responseCode = "200", description = "Pruus listados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))),
            @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}")))})
    @GetMapping("/meus-likes/{idUsuario}")
    public List<Pruu> pesquisarPruusCurtidosPeloUsuario(@PathVariable String idUsuario) throws PomboException {
        return pruuService.pesquisarPruusCurtidosPeloUsuario(idUsuario);
    }
    
    @Operation(summary = "Contador de páginas", description = "Retorna uma contagem com o número total de páginas.")    @PostMapping("/total-paginas")
	public int contarPaginas(@RequestBody PruuSeletor seletor) {
		return this.pruuService.contarPaginas(seletor);
	}

}
