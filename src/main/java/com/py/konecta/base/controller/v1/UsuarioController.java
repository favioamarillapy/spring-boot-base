package com.py.konecta.base.controller.v1;

import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PageDTO;
import com.py.konecta.base.entity.Usuario;
import com.py.konecta.base.service.UsuarioService;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService usuarioService;

    @GetMapping()
    public ResponseEntity<CustomResponse<PageDTO<Usuario>>> findAll(@RequestParam(required = false, defaultValue = "") String filtros,
                                                                    @RequestParam(defaultValue = "10") int cantidad,
                                                                    @RequestParam(defaultValue = "0") int pagina,
                                                                    @RequestParam(defaultValue = "id") String orderBy,
                                                                    @RequestParam(defaultValue = "desc") String orderDir) {
        var response = usuarioService.findAll(filtros, cantidad, pagina, orderBy, orderDir);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomResponse<Usuario>> findById(@PathVariable Long id) {
        var response = usuarioService.findById(id);
        return ResponseEntity.ok(response);
    }

    @PostMapping()
    public ResponseEntity<CustomResponse<Usuario>> create(@RequestBody Usuario body) {
        var response = usuarioService.create(body);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomResponse<Usuario>> update(@PathVariable Long id, @RequestBody Usuario body) {
        var response = usuarioService.update(id, body);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/activar/{id}")
    public ResponseEntity<CustomResponse<Usuario>> activar(@PathVariable Long id) {
        var response = usuarioService.activar(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/resetear-password/{id}")
    public ResponseEntity<CustomResponse<Usuario>> resetearPassword(@PathVariable Long id, @RequestBody Usuario body) {
        var response = usuarioService.resetearPassword(id, body);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/desactivar/{id}")
    public ResponseEntity<CustomResponse<Usuario>> desactivar(@PathVariable Long id) {
        var response = usuarioService.desactivar(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomResponse<Void>> delete(@PathVariable Long id) {
        var response = usuarioService.delete(id);
        return ResponseEntity.ok(response);
    }
}
