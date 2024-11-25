package com.vilu.pombo.controller;

import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.entity.Pruu;
import com.vilu.pombo.model.seletor.PruuSeletor;
import com.vilu.pombo.service.PruuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/pruus")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class PruuController {

    @Autowired
    private PruuService pruuService;

    @Operation(summary = "Inserir novo pruu", description = "Adiciona um novo pruu ao sistema.", responses = {@ApiResponse(responseCode = "200", description = "Pruu criado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))), @ApiResponse(responseCode = "400", description = "Erro de validação ou regra de negócio", content = @Content(mediaType = "application/json", examples = @ExampleObject(value = "{\"message\": \"Erro de validação: campo X é obrigatório\", \"status\": 400}")))})
    @PostMapping(path = "/cadastrar")
    public ResponseEntity<Pruu> cadastrar(@Valid @RequestBody Pruu novoPruu) throws PomboException {
        return ResponseEntity.ok(pruuService.cadastrar(novoPruu));
    }

    @Operation(summary = "Curtir ou descurtir um pruu", description = "Curte ou descurte um pruu pelo seu ID.", responses = {@ApiResponse(responseCode = "200", description = "Pruu curtido/descurtido com sucesso")})
    @PostMapping(path = "/curtir/{idPruu}/{idUsuario}")
    public ResponseEntity<Void> curtir(@PathVariable String idPruu, @PathVariable String idUsuario) throws PomboException {
        pruuService.toggleCurtida(idUsuario, idPruu);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Bloquear um pru", description = "Bloqueia um pruu pelo seu ID.")
    @DeleteMapping("/excluir/{idPruu}/{idUsuario}")
    public ResponseEntity<Void> excluir(@PathVariable String idPruu, @PathVariable String idUsuario) throws PomboException {
        pruuService.excluir(idPruu, idUsuario);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Listar todos os pruus", description = "Retorna uma lista de todos os pruus cadastrados no sistema.", responses = {@ApiResponse(responseCode = "200", description = "Lista de pruus retornada com sucesso")})
    @GetMapping(path = "/todos")
    public List<Pruu> pesquisarTodos(@RequestBody PruuSeletor paginacao) {
        return pruuService.pesquisarTodos(paginacao);
    }

    @Operation(summary = "Pesquisar pruu por ID", description = "Busca um pruu específico pelo seu ID.")
    @GetMapping(path = "/{id}")
    public ResponseEntity<Pruu> pesquisarPorId(@PathVariable String id) throws PomboException {
        Pruu pruu = pruuService.pesquisarPorId(id);
        return ResponseEntity.ok(pruu);
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de pruus de acordo com o filtro selecionado.", responses = {@ApiResponse(responseCode = "200", description = "Pruus filtrados com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Pruu.class))), @ApiResponse(responseCode = "500", description = "Erro interno do servidor", content = @Content(mediaType = "application/json", schema = @Schema(description = "Detalhes do erro interno", example = "{\"message\": \"Erro interno do servidor\", \"status\": 500}")))})
    @PostMapping("/filtrar")
    public List<Pruu> pesquisarComFiltros(@RequestBody PruuSeletor seletor) throws PomboException {
        return pruuService.pesquisarComFiltros(seletor);
    }

}
