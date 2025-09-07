package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.*;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.AdminRepo;
import com.example.utkarshbackend.services.AuthService;
import com.example.utkarshbackend.services.TeacherService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final TeacherService teacherService;
    private final AdminRepo adminRepo;

    public AuthController(AuthService authService, TeacherService teacherService, AdminRepo adminRepo) {
        this.authService = authService;
        this.teacherService = teacherService;
        this.adminRepo = adminRepo;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        LoginResponseDTO loginResponseDTO = authService.login(loginRequestDTO);
        ResponseCookie responseCookie = ResponseCookie.from("JWT", loginResponseDTO.getAccessToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(21 * 60 * 60) // 21 hrs
                .sameSite("strict")
                .build();
        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(loginResponseDTO.getUser());
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return authService.logout();
    }

    @GetMapping("/get-current-user")
    public ResponseEntity<?> getCurrentUser (Authentication authentication) {
        if(authentication == null) {
            return ResponseEntity.badRequest().body("No user logged in");
        }

        String email = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        if(role.equals("ROLE_TEACHER") || role.equals("ROLE_HOD")) {
            Teacher teacher = teacherService.getTeacherByEmail(email);
            return ResponseEntity.ok(new UserDTO().toDTO(teacher));
        }
        else if(role.equals("ROLE_ADMIN")) {
            Admin admin = adminRepo.findByEmail(email).orElse(null);
            if(admin != null) {
                return ResponseEntity.ok(new UserDTO().toDTO(admin));
            }
        }
        return ResponseEntity.badRequest().body("No user logged in");
    }

    @PostMapping("/register-as-admin")
    public ResponseEntity<String> registerAdmin(@RequestBody AdminRequestDTO adminRequestDTO) {
        return authService.registerInitialAdmin(adminRequestDTO);
    }

    @PostMapping("/reg-student")
    public ResponseEntity<UserDTO> studentRegistration(@RequestBody StudentRegRequestDTO studentRegRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        UserDTO student = authService.registerStudent(studentRegRequestDTO);
        var location = uriComponentsBuilder.path("/auth/get-current-user").build().toUri();
        return ResponseEntity.created(location).body(student);
    }
}
