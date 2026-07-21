package com.graspymar.ims.serviceImpl;

import com.graspymar.ims.dto.LoginRequest;
import com.graspymar.ims.dto.LoginResponse;
import com.graspymar.ims.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;
import com.graspymar.ims.security.CustomUserDetailsService;
import com.graspymar.ims.security.JwtService;
import org.springframework.security.core.userdetails.UserDetails;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    
    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
        )
    );

    UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
    String token = jwtService.generateToken(userDetails);
    return new LoginResponse(token);

}}