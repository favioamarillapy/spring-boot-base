package com.py.konecta.base.controller.v1;

import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PingDTO;
import com.py.konecta.base.service.PingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/ping")
@RequiredArgsConstructor
public class PingController {
    private final PingService pingService;

    @GetMapping
    public ResponseEntity<CustomResponse<PingDTO>> ping() {
        var response = pingService.ping();
        return ResponseEntity.ok(response);
    }


}
