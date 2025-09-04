package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.*;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.jwt.JwtService;
import com.example.utkarshbackend.repository.AdminRepo;
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
    private final AdminRepo adminRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, TeacherRepo teacherRepo, AdminRepo adminRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.teacherRepo = teacherRepo;
        this.adminRepo = adminRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public LoginResponseDTO login(LoginRequestDTO loginRequestDTO) {
        if(loginRequestDTO.getEmail() == null || loginRequestDTO.getPassword() == null || loginRequestDTO.getEmail().isBlank() || loginRequestDTO.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Password are Required");
        }

        try {
            Admin admin = adminRepo.findByEmail(loginRequestDTO.getEmail()).orElse(null);
            if(admin != null) {
                return loginAsAdmin(loginRequestDTO, admin);
            }
            Teacher teacher = teacherRepo.findByEmail(loginRequestDTO.getEmail()).orElse(null);
            if(teacher != null) {
                Authentication authentication = authenticationManager.authenticate(
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

    private LoginResponseDTO loginAsAdmin(LoginRequestDTO loginRequestDTO, Admin admin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            AuthUser authUser = AuthUser.builder()
                    .id(admin.getId())
                    .email(admin.getEmail())
                    .role(admin.getRole())
                    .build();
            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                    .user(new UserDTO().toDTO(admin))
                    .build();
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

    public ResponseEntity<String> registerInitialAdmin(AdminRequestDTO adminRequestDTO) {
        //check if admin already exists
        //check if email is valid
        if(!adminRequestDTO.getEmail().equalsIgnoreCase(adminEmail)) {
            return ResponseEntity.badRequest().body("Please use admin email");
        }
        Admin admin = adminRepo.findByEmail(adminEmail).orElse(null);
        //check if password is valid
        if(admin == null && !adminRequestDTO.getPassword().equals(adminPassword)) {
            return ResponseEntity.badRequest().body("Please use admin password");
        }
        if(admin != null) {
            return ResponseEntity.badRequest().body("Admin already exists");
        }
        else {
            admin = Admin.builder()
                    .email(adminEmail)
                    .password(encodePassword(adminRequestDTO.getNewPassword()))
                    .name("Admin")
                    .role("ADMIN")
                    .build();
        }
        Admin saved = adminRepo.save(admin);
        return ResponseEntity.ok().body("Admin Registered Successfully");
    }
}
