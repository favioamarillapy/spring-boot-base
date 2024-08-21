package com.py.konecta.base.security;

import com.py.konecta.base.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/*
 * Class to validate user login
 * */
@Component
@RequiredArgsConstructor
public class CustomAuthenticationManager implements AuthenticationManager {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = (String) authentication.getCredentials();

        if (username == null || username.isEmpty()) {
            throw new BadCredentialsException("El nombre de usuario no puede estar vacio");
        }
        if (password == null || password.isEmpty()) {
            throw new BadCredentialsException("La contraseña no puede estar vacia");
        }

        return getUser(username, password);
    }


    private Authentication getUser(String username, String password) {
        var user = usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new BadCredentialsException("Credenciales incorrectas"));

        if (user.getRol() == null) {
            throw new BadCredentialsException("El usuario no cuenta con un rol");
        }


        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Credenciales incorrectas");
        }

        List<GrantedAuthority> roleList = new ArrayList<>();
        roleList.add(new SimpleGrantedAuthority(user.getRol().name()));


        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                new UsernamePasswordAuthenticationToken(user, password, roleList);

        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(usernamePasswordAuthenticationToken);
        SecurityContextHolder.setContext(context);

        return usernamePasswordAuthenticationToken;
    }


}
