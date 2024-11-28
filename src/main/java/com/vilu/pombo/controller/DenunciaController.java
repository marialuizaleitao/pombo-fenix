package com.vilu.pombo.controller;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exception.PomboException;
import com.vilu.pombo.model.dto.DenunciaDTO;
import com.vilu.pombo.model.entity.Denuncia;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.model.enums.StatusDenuncia;
import com.vilu.pombo.model.seletor.DenunciaSeletor;
import com.vilu.pombo.service.DenunciaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "/denuncias")
@CrossOrigin(origins = {"http://localhost:4200"}, maxAge = 3600)
public class DenunciaController {

    @Autowired
    private DenunciaService denunciaService;
    @Autowired
    private AuthService authService;

    @Operation(summary = "Inserir nova denuncia", description = "Cria uma nova denuncia", responses = {@ApiResponse(responseCode = "200", description = "Denuncia registrada com sucesso"),})
    @PostMapping("/cadastrar")
    public ResponseEntity<Denuncia> cadastrar(@Valid @RequestBody Denuncia denuncia) throws PomboException {
    	Usuario subject = authService.getUsuarioAutenticado();
    	
    	denuncia.setUsuario(subject);
    	
        return ResponseEntity.ok(denunciaService.cadastrar(denuncia));
    }

    @Operation(summary = "Atualizar status da denuncia", description = "Atualiza o status da denuncia", responses = {@ApiResponse(responseCode = "200", description = "Status da denuncia atualizado com sucesso"), @ApiResponse(responseCode = "400", description = "Denuncia não encontrada")})
    @PutMapping("/atualizar/{idDenuncia}")
    public ResponseEntity<Void> atualizar(@PathVariable String idDenuncia, @RequestBody StatusDenuncia statusDenuncia) throws PomboException {
        denunciaService.atualizar(idDenuncia, statusDenuncia);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Excluir denuncia", description = "Exclui a denuncia", responses = {@ApiResponse(responseCode = "200", description = "Denuncia excluida com sucesso"), @ApiResponse(responseCode = "400", description = "Denuncia não encontrada")})
    @DeleteMapping("/excluir/{idDenuncia}")
    public ResponseEntity<Void> excluir(@PathVariable String idDenuncia) throws PomboException {
        Usuario subject = authService.getUsuarioAutenticado();

        denunciaService.excluir(idDenuncia, subject.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Pesquisar todas as denúncias", description = "Retorna uma lista com todas as denúncias cadastradas.", responses = {@ApiResponse(responseCode = "200", description = "Denuncias retornadas com sucesso."), @ApiResponse(responseCode = "400", description = "Denuncias não encontradas."), @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")})
    @GetMapping("/todas")
    public List<DenunciaDTO> pesquisarTodas() throws PomboException {
        return denunciaService.pesquisarTodas();
    }

    @Operation(summary = "Pesquisar uma denúncia pelo id", description = "Retorna denuncia especifica para o admin.", responses = {@ApiResponse(responseCode = "200", description = "Denuncia retornada com sucesso."), @ApiResponse(responseCode = "400", description = "Denuncia não encontrada."), @ApiResponse(responseCode = "401", description = "Usuário não autorizado.")})
    @GetMapping("/{id}")
    public ResponseEntity<DenunciaDTO> pesquisarPorId(@PathVariable String id) throws PomboException {
        DenunciaDTO denuncia = denunciaService.pesquisarPorId(id);
        return ResponseEntity.ok(denuncia);
    }

    @Operation(summary = "Pesquisar com filtro", description = "Retorna uma lista de denúncias de acordo com o filtro selecionado.")
    @PostMapping("/filtrar")
    public List<DenunciaDTO> pesquisarComFiltros(@RequestBody DenunciaSeletor seletor) {
        return denunciaService.pesquisarComFiltros(seletor);
    }

}