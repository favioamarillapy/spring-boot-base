package com.py.konecta.base.service;

import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.PingDTO;

public interface PingService {

    CustomResponse<PingDTO> ping();
}
