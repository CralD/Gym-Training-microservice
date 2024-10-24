package com.epam.gymappHibernate.controller;

import com.epam.gymappHibernate.dao.TraineeRepository;
import com.epam.gymappHibernate.dao.UserRepository;

import com.epam.gymappHibernate.dto.AuthRequest;
import com.epam.gymappHibernate.services.LoginAttemptService;
import com.epam.gymappHibernate.services.SecurityService;
import com.epam.gymappHibernate.services.TokenInvalidationService;
import com.epam.gymappHibernate.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private SecurityService userDetailsService;

    private UserRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private LoginAttemptService loginAttemptService;

    private TokenInvalidationService tokenInvalidationService;

    private JwtUtil jwtUtil;
    private static final Logger logger = LoggerFactory.getLogger(TraineeRepository.class);


    public AuthController(AuthenticationManager authenticationManager, SecurityService userDetailsService, UserRepository userRepository, PasswordEncoder passwordEncoder, LoginAttemptService loginAttemptService, TokenInvalidationService tokenInvalidationService, JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.loginAttemptService = loginAttemptService;
        this.tokenInvalidationService = tokenInvalidationService;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody AuthRequest authenticationRequest) throws Exception {
        String username = authenticationRequest.getUsername();

        if (loginAttemptService.isBlocked(username)) {
            return ResponseEntity.status(HttpStatus.LOCKED).body("Account is locked due to too many failed login attempts. Please try again later.");
        }

        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authenticationRequest.getUsername(), authenticationRequest.getPassword())
            );
            loginAttemptService.loginSucceeded(username);
        } catch (Exception e) {
            loginAttemptService.loginFailed(username);
            throw e;
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails);

        return ResponseEntity.ok(jwt);

    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(HttpServletRequest request, HttpServletResponse response) {
        String authorizationHeader = request.getHeader("Authorization");
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            String jwt = authorizationHeader.substring(7);
            tokenInvalidationService.addToBlacklist(jwt);
        }


        return ResponseEntity.ok().body("Logged out successfully");
    }

}

