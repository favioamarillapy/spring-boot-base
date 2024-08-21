package com.py.konecta.base.service.impl;

import com.py.konecta.base.core.Constantes;
import com.py.konecta.base.core.Mensajes;
import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PingDTO;

import com.py.konecta.base.service.PingService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PingServiceImpl implements PingService {

    private final EntityManager entityManager;


    @Override
    public CustomResponse<PingDTO> ping() {
        log.info("Request: {}", new Date());

        try {
            Query query = entityManager.createNativeQuery("select now() as fecha");
            List<String> results = query.getResultList();

            var ping = new PingDTO();
            ping.setVersion(Constantes.VERSION);
            ping.setFecha(results.getFirst());
            ping.setBd(!results.isEmpty());

            var response = new CustomResponse<>(Mensajes.SUCCESS_GET, Boolean.FALSE, ping);


            log.info("Response: {}", response);
            return response;
        } catch (Exception e) {
            log.error("Error: {}", e.getMessage(), e);
            return new CustomResponse<>(e.getMessage(), Boolean.TRUE, null);
        }
    }

}
