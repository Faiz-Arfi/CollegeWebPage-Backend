package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.AuthUser;
import com.example.utkarshbackend.dto.LoginRequestDTO;
import com.example.utkarshbackend.dto.LoginResponseDTO;
import com.example.utkarshbackend.dto.UserDTO;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.jwt.JwtService;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${jwt.access-token-validity-time}")
    private String accessTokenValidityTime;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TeacherRepo teacherRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, TeacherRepo teacherRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.teacherRepo = teacherRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        if(loginRequestDTO.getEmail() == null || loginRequestDTO.getPassword() == null || loginRequestDTO.getEmail().isBlank() || loginRequestDTO.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password are Required");
        }

        Authentication authentication;
        if(loginRequestDTO.getEmail().equals(adminEmail)) {
            System.out.println("Admin login");
            System.out.println(adminPassword);
            System.out.println(encodePassword(adminPassword));
            authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
            );

            if(authentication.isAuthenticated()) {
                AuthUser authUser = AuthUser.builder()
                        .id(0L)
                        .role("ADMIN")
                        .email(adminEmail)
                        .build();
                return LoginResponseDTO.builder()
                        .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                        .user(UserDTO.builder()
                                .id(0L)
                                .email(adminEmail)
                                .name("Admin")
                                .role("ADMIN")
                                .isEmailVerified(true)
                                .build())
                        .build();
            }
        }

        try {
            Teacher teacher = teacherRepo.findByEmail(loginRequestDTO.getEmail()).orElse(null);
            if(teacher != null) {
                authentication = authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
                );

                if (authentication.isAuthenticated()) {
                    AuthUser authUser = AuthUser.builder()
                            .id(teacher.getId())
                            .email(teacher.getEmail())
                            .role(teacher.getRole())
                            .build();
                    return LoginResponseDTO.builder()
                            .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                            .user(new UserDTO().toDTO(teacher))
                            .build();
                }

            }
            else {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
            }
        } catch (ResponseStatusException e) {
            throw e;
        } catch (AuthenticationException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "An error occurred while logging in");
        }
        return null;
    }

    public ResponseEntity<String> logout() {
        ResponseCookie responseCookie = ResponseCookie.from("JWT", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .sameSite("strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body("Logout Successful");
    }

    public String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
