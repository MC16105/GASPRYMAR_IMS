package com.graspymar.ims.service;

import com.graspymar.ims.dto.LoginRequest;
import com.graspymar.ims.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}
