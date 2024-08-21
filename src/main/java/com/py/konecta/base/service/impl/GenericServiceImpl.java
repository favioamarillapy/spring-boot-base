package com.py.konecta.base.service.impl;

import com.py.konecta.base.entity.Usuario;
import org.springframework.security.core.context.SecurityContextHolder;

public abstract class GenericServiceImpl<E> {

    public Usuario getUsuarioLogueado() {
        return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public String getUsername() {
        try {
            Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return usuario.getUsername();
        } catch (Exception e) {
            return "";
        }
    }
}

