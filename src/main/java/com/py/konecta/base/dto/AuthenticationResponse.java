package com.py.konecta.base.dto;

import com.py.konecta.base.entity.Usuario;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthenticationResponse {
    private UsuarioDTO usuario;
    private String token;
}