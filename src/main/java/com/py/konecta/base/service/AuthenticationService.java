package com.py.konecta.base.service;

import com.py.konecta.base.dto.AuthenticationResponse;
import com.py.konecta.base.dto.CustomResponse;
import com.py.konecta.base.dto.LoginRequest;

public interface AuthenticationService {

    CustomResponse<AuthenticationResponse> login(LoginRequest request);

    CustomResponse<Void> logout();
}
