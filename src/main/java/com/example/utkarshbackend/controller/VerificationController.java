package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.jwt.JwtService;
import com.example.utkarshbackend.repository.StudentRepo;
import com.example.utkarshbackend.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
public class VerificationController {

    @Value("${frontend.url}")
    private String frontendUrl;

    private final StudentRepo studentRepo;
    private final JwtService jwtService;
    private final UserDetailsServiceImpl userDetailsService;

    public VerificationController(StudentRepo studentRepo, JwtService jwtService, UserDetailsServiceImpl userDetailsService) {
        this.studentRepo = studentRepo;
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @GetMapping("/verify-email")
    public ResponseEntity<String> verifyEmail(@RequestParam("token") String token) {
        String email = jwtService.extractEmail(token);
        Student student = studentRepo.findByEmail(email).orElse(null);
        if (student == null || student.getVerificationToken() == null) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired or Invalid");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(email);
        if(!jwtService.isTokenValid(token, userDetails) || !student.getVerificationToken().equals(token)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Token Expired or Invalid");
        }
        student.setEmailVerified(true);
        student.setVerificationToken(null);
        studentRepo.save(student);
        // redirect to the login page or frontend URL with a success message
        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(frontendUrl + "/login/student?verified=success"))
                .build();
    }
}
