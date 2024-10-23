package com.py.konecta.base.service;

import com.py.konecta.base.entity.Usuario;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.py.konecta.base.dto.CustomResponse;

public interface UsuarioService extends GenericService<Usuario> {

    UserDetailsService userDetailsService();

    CustomResponse<Usuario> create(Usuario entity);

    CustomResponse<Usuario> resetearPassword(Long id, Usuario entity);
}
