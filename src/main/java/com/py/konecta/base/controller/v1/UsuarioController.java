package com.py.konecta.base.controller.v1;

import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PageDTO;
import com.py.konecta.base.dto.UsuarioDTO;
import com.py.konecta.base.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE','ROLE_REPORTES','ROLE_ENCARGADO_DEPOSITO','ROLE_CHOFER')")
    @GetMapping()
    public ResponseEntity<CustomResponse<PageDTO<UsuarioDTO>>> findAll(@RequestParam(required = false, defaultValue = "") String filtros,
                                                                       @RequestParam(defaultValue = "10") int cantidad,
                                                                       @RequestParam(defaultValue = "0") int pagina,
                                                                       @RequestParam(defaultValue = "id") String orderBy,
                                                                       @RequestParam(defaultValue = "desc") String orderDir) {
        var response = usuarioService.findAll(filtros, cantidad, pagina, orderBy, orderDir);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE','ROLE_REPORTES','ROLE_ENCARGADO_DEPOSITO','ROLE_CHOFER')")
    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<UsuarioDTO>> findById(@PathVariable Long id) {
        var response = usuarioService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @PostMapping()
    public ResponseEntity<CustomResponse<UsuarioDTO>> create(@RequestBody UsuarioDTO body) {
        var response = usuarioService.create(body);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<UsuarioDTO>> update(@PathVariable Long id, @RequestBody UsuarioDTO body) {
        var response = usuarioService.update(id, body);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @PutMapping("/activar/{id}")
    public ResponseEntity<CustomResponse<UsuarioDTO>> activar(@PathVariable Long id) {
        var response = usuarioService.activar(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @PutMapping("/resetear-password/{id}")
    public ResponseEntity<CustomResponse<UsuarioDTO>> resetearPassword(@PathVariable Long id, @RequestBody UsuarioDTO body) {
        var response = usuarioService.resetearPassword(id, body);
        return ResponseEntity.ok(response);
    }


    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @PutMapping("/desactivar/{id}")
    public ResponseEntity<CustomResponse<UsuarioDTO>> desactivar(@PathVariable Long id) {
        var response = usuarioService.desactivar(id);
        return ResponseEntity.ok(response);
    }

    @PreAuthorize("hasAnyRole('ROLE_BACKOFFICE')")
    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> delete(@PathVariable Long id) {
        var response = usuarioService.delete(id);
        return ResponseEntity.ok(response);
    }
}
