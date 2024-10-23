package com.py.konecta.base.service.impl;

import com.google.gson.Gson;
import com.py.konecta.base.core.Mensajes;
import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PageDTO;
import com.py.konecta.base.entity.Usuario;
import com.py.konecta.base.repository.UsuarioRepository;
import com.py.konecta.base.service.UsuarioService;

import com.py.konecta.base.specification.GenericSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class UsuarioServiceImpl extends GenericServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final GenericSpecification<Usuario> genericSpecification;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final ModelMapper mapper;

    @Override
    public UserDetailsService userDetailsService() {
        return username -> usuarioRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontro el usuario"));
    }

    @Override
    public CustomResponse<PageDTO<Usuario>> findAll(String filtros, int cantidad, int pagina, String orderBy,
                                                    String orderDir) {

        log.info("Request {}: filtros[{}],cantidad[{}], pagina[{}], orderBy[{}], orderDir[{}]", getUsername(), filtros,
                cantidad, pagina, orderBy, orderDir);

        try {
            Usuario ejemplo = new Usuario();
            if (!filtros.isBlank()) {
                ejemplo = new Gson().fromJson(filtros, Usuario.class);
            }
            Specification<Usuario> specification = genericSpecification.getSpec(ejemplo);

            var sort = Sort.by(orderDir.equals("asc") ? Sort.Direction.ASC : Sort.Direction.DESC, orderBy);
            Pageable pageable;
            if (cantidad == -1) {
                pageable = Pageable.unpaged();
            } else {
                pageable = PageRequest.of(pagina, cantidad, sort);
            }

            Page<Usuario> page = usuarioRepository.findAll(specification, pageable);
            List<Usuario> content = page.getContent().stream()
                    .peek(record -> record.setPassword(null))
                    .toList();

            PageDTO<Usuario> data = new PageDTO<>();
            data.setContent(content);
            data.setSize(page.getSize());
            data.setTotalPages(page.getTotalPages());
            data.setTotalElements(page.getTotalElements());

            var response = new CustomResponse<>(Mensajes.SUCCESS_GET, Boolean.FALSE, data);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> findById(Long id) {
        log.info("Request {}: id[{}]", getUsername(), id);
        try {
            var entity = usuarioRepository.findById(id)
                    .orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));
            entity.setPassword(null);

            var response = new CustomResponse<>(Mensajes.SUCCESS_GET, Boolean.FALSE, entity);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> create(Usuario body) {
        log.info("Request: entity[{}]", body);

        try {
            if (usuarioRepository.findByUsuario(body.getUsuario()).isPresent()) {
                throw new Exception("Ya existe el usuario " + body.getUsuario());
            }

            body.setActivo(Boolean.TRUE);
            body.setPassword(passwordEncoder.encode(body.getPassword()));
            body.setFechaCreacion(new Date());
            body = usuarioRepository.save(body);

            body.setPassword(null);

            var response = new CustomResponse<>(Mensajes.SUCCESS_SAVE, Boolean.FALSE, body);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> update(Long id, Usuario body) {
        log.info("Request {}: id[{}], entity[{}]", getUsername(), id, body);

        try {
            var record = usuarioRepository.findById(id)
                    .orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));

            if (!record.getUsuario().equals(body.getUsuario())) {
                if (usuarioRepository.findByUsuario(body.getUsuario()).isPresent()) {
                    throw new Exception("Ya existe un usuario: " + body.getUsuario());
                }
            }

            var response = new CustomResponse<>(Mensajes.SUCCESS_SAVE, Boolean.FALSE, record);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> activar(Long id) {
        log.info("Request {}: id[{}]", getUsername(), id);

        try {
            var record = usuarioRepository.findById(id)
                    .orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));
            record.setActivo(Boolean.TRUE);
            record = usuarioRepository.save(record);

            var response = new CustomResponse<>(Mensajes.SUCCESS_SAVE, Boolean.FALSE, record);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> desactivar(Long id) {
        log.info("Request {}: id[{}]", getUsername(), id);

        try {
            var record = usuarioRepository.findById(id)
                    .orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));
            record.setActivo(Boolean.FALSE);
            record = usuarioRepository.save(record);

            var response = new CustomResponse<>(Mensajes.SUCCESS_SAVE, Boolean.FALSE, record);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Void> delete(Long id) {
        log.info("Request {}: id[{}]", getUsername(), id);

        try {
            usuarioRepository.findById(id).orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));
            usuarioRepository.deleteById(id);

            var response = new CustomResponse<Void>(Mensajes.SUCCESS_DELETE, Boolean.FALSE, null);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

    @Override
    public CustomResponse<Usuario> resetearPassword(Long id, Usuario body) {
        log.info("Request {}: id[{}], entity[{}]", getUsername(), id, body);

        try {
            var record = usuarioRepository.findById(id)
                    .orElseThrow(() -> new Exception(Mensajes.REGISTRO_NO_ENCONTRADO));

            record.setPassword(passwordEncoder.encode(body.getPassword()));
            record = usuarioRepository.save(record);

            var response = new CustomResponse<>(Mensajes.SUCCESS_DELETE, Boolean.FALSE, record);
            log.info("Response {}: {}", getUsername(), response);

            return response;
        } catch (Exception e) {
            log.error("Error {}: {}", getUsername(), e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }
}
