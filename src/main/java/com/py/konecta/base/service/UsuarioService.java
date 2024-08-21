package com.py.konecta.base.service;

import com.py.konecta.base.dto.UsuarioDTO;
import org.springframework.security.core.userdetails.UserDetailsService;

import com.py.konecta.base.dto.CustomResponse;

public interface UsuarioService extends GenericService<UsuarioDTO> {

    UserDetailsService userDetailsService();

    CustomResponse<UsuarioDTO> create(UsuarioDTO entity);

    CustomResponse<UsuarioDTO> resetearPassword(Long id, UsuarioDTO entity);
}
