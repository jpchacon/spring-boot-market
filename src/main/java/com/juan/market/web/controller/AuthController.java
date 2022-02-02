package com.juan.market.web.controller;

import com.juan.market.domain.dto.AuthenticationRequest;
import com.juan.market.domain.dto.AuthenticationResponse;
import com.juan.market.domain.service.JuanUserDetailsService;
import com.juan.market.web.security.JWTUtil;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager manager;
    private JuanUserDetailsService userDetailsService;
    private JWTUtil jwtUtil;

    public AuthController(AuthenticationManager manager, JuanUserDetailsService userDetailsService, JWTUtil jwtUtil) {
        this.manager = manager;
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @ApiOperation("Autentificación de los usuarios")
    @PostMapping("/authenticate")
    @ApiResponses({
            @ApiResponse(code = 200, message = "OK"),
            @ApiResponse(code = 403, message = "Forbidden")
    })
    public ResponseEntity<AuthenticationResponse> createToken(@ApiParam(value = "Usuario y contraseña de los usuarios",
                                                                required = true)
                                                                  @RequestBody AuthenticationRequest request) {
        try {
            manager.authenticate(new UsernamePasswordAuthenticationToken(request.getUserName(), request.getPassword()));
            UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUserName());
            String jwt = jwtUtil.generateToken(userDetails);

            return new ResponseEntity<>(new AuthenticationResponse(jwt), HttpStatus.OK);
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}
