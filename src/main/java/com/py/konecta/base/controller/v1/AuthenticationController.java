package com.py.konecta.base.controller.v1;

import com.py.konecta.base.dto.AuthenticationResponse;
import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.LoginRequest;
import com.py.konecta.base.dto.UsuarioDTO;
import com.py.konecta.base.entity.Usuario;
import com.py.konecta.base.service.AuthenticationService;
import com.py.konecta.base.service.UsuarioService;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioService usuarioService;

    @PostMapping("/signin")
    public ResponseEntity<CustomResponse<AuthenticationResponse>> login(@RequestBody LoginRequest request) {
        var response = authenticationService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<CustomResponse<UsuarioDTO>> create(@RequestBody UsuarioDTO body) {
        var response = usuarioService.create(body);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<CustomResponse<Void>> logout() {
        var response = authenticationService.logout();
        return ResponseEntity.ok(response);
    }

}
