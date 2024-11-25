package com.vilu.pombo.controller;

import com.vilu.pombo.auth.AuthService;
import com.vilu.pombo.exeption.PomboException;
import com.vilu.pombo.model.entity.Usuario;
import com.vilu.pombo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "/auth")
public class AuthController {

    @Autowired
    private AuthService authenticationService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioService usuarioService;

    /**
     * Método de login padronizado -> Basic Auth
     * <p>
     * O parâmetro Authentication já encapsula login (username) e senha (senha)
     * Basic <Base64 encoded username and senha>
     *
     * @param authentication
     * @return o JWT gerado
     */
    @PostMapping("/login")
    public String login(Authentication authentication) {
        return authenticationService.authenticate(authentication);
    }

    @PostMapping("/novo")
    @ResponseStatus(code = HttpStatus.CREATED)
    public void cadastrar(@RequestBody Usuario novoUsuario) throws PomboException {
        String senhaCifrada = passwordEncoder.encode(novoUsuario.getSenha());
        novoUsuario.setSenha(senhaCifrada);
        usuarioService.cadastrar(novoUsuario);
    }

}