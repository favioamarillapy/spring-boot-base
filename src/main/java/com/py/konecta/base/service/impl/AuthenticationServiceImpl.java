package com.py.konecta.base.service.impl;

import com.py.konecta.base.dto.AuthenticationResponse;
import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.LoginRequest;
import com.py.konecta.base.dto.UsuarioDTO;
import com.py.konecta.base.entity.Usuario;
import com.py.konecta.base.repository.UsuarioRepository;
import com.py.konecta.base.security.CustomAuthenticationManager;
import com.py.konecta.base.service.AuthenticationService;
import com.py.konecta.base.service.JwtService;
import lombok.extern.slf4j.Slf4j;

import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final JwtService jwtService;
    private final CustomAuthenticationManager customAuthenticationManager;
    private final ModelMapper mapper;

    @Override
    public CustomResponse<AuthenticationResponse> login(LoginRequest request) {
        log.info("Request: {}", request);
        customAuthenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getPassword()));

        var record = usuarioRepository.findByUsuario(request.getUsuario())
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        record.setPassword(null);
        if (record.getActivo() == null || !record.getActivo()) {
            throw new BadCredentialsException("El usuario no se encuentra activado");
        }

        var jwt = jwtService.generateToken(record);
        var data = convertToDto(record);

        var authenticationResponse = new AuthenticationResponse(data, jwt);
        var response = new CustomResponse<>("Acceso concedido", Boolean.FALSE, authenticationResponse);

        log.info("Response: {}", response);
        return response;
    }

    @Override
    public CustomResponse<Void> logout() {
        try {
            SecurityContextHolder.clearContext();
            return new CustomResponse<>("Sesion cerrada", Boolean.FALSE, null);
        } catch (Exception e) {
            return new CustomResponse<>(e.getMessage(), Boolean.FALSE, null);
        }
    }

    private UsuarioDTO convertToDto(Usuario entity) {
        var data = mapper.map(entity, UsuarioDTO.class);
        data.setPassword(null);
        return data;
    }

}
