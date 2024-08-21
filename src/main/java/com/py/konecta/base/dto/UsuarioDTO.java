package com.py.konecta.base.dto;

import com.py.konecta.base.entity.Rol;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioDTO {
    private Long id;

    private String nombre;

    private String apellido;

    private String usuario;

    private String password;

    @Enumerated(EnumType.STRING)
    private Rol rol;

    private Boolean activo;
}
