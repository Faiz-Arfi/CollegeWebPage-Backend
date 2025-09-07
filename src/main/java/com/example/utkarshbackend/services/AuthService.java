package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.*;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.jwt.JwtService;
import com.example.utkarshbackend.mapper.UserMapper;
import com.example.utkarshbackend.repository.AdminRepo;
import com.example.utkarshbackend.repository.DepartmentRepo;
import com.example.utkarshbackend.repository.StudentRepo;
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

@SuppressWarnings("DuplicatedCode")
@Service
public class AuthService {

    @Value("${admin.email}")
    private String adminEmail;
    @Value("${admin.password}")
    private String adminPassword;
    @Value("${jwt.access-token-validity-time}")
    private String accessTokenValidityTime;
    @Value("${email.verification.validity-time}")
    private String emailVerificationTokenValidityTime;

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final TeacherRepo teacherRepo;
    private final AdminRepo adminRepo;
    private final StudentRepo studentRepo;
    private final DepartmentRepo departmentRepo;
    private final PasswordEncoder passwordEncoder;

    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, TeacherRepo teacherRepo, AdminRepo adminRepo, StudentRepo studentRepo, DepartmentRepo departmentRepo, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.teacherRepo = teacherRepo;
        this.adminRepo = adminRepo;
        this.studentRepo = studentRepo;
        this.departmentRepo = departmentRepo;
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
                    AuthUser authUser = UserMapper.toAuthUser(teacher);
                    return LoginResponseDTO.builder()
                            .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                            .user(UserMapper.toDTO(teacher))
                            .build();
                }

            }
            Student student = studentRepo.findByEmail(loginRequestDTO.getEmail()).orElse(null);
            if(student != null) {
                return loginAsStudent(loginRequestDTO, student);
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
    }

    private LoginResponseDTO loginAsStudent(LoginRequestDTO loginRequestDTO, Student student) {
        if(!student.isEmailVerified()) {
            //To-DO: send an email verification link
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Please verify your email");
        }
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            AuthUser authUser = UserMapper.toAuthUser(student);
            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                    .user(UserMapper.toDTO(student))
                    .build();
        }
        return null;
    }

    private LoginResponseDTO loginAsAdmin(LoginRequestDTO loginRequestDTO, Admin admin) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequestDTO.getEmail(), loginRequestDTO.getPassword())
        );

        if (authentication.isAuthenticated()) {
            AuthUser authUser = UserMapper.toAuthUser(admin);
            return LoginResponseDTO.builder()
                    .accessToken(jwtService.generateToken(authUser, Integer.parseInt(accessTokenValidityTime)))
                    .user(UserMapper.toDTO(admin))
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
        //check if the password is valid
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
                    .isEmailVerified(true)
                    .build();
        }
        adminRepo.save(admin);
        return ResponseEntity.ok().body("Admin Registered Successfully");
    }

    public UserDTO registerStudent(StudentRegRequestDTO studentRegRequestDTO) {
        Student student = studentRepo.findByEmail(studentRegRequestDTO.getEmail()).orElse(null);
        //check if email is already register or not
        if (student != null) {
            if (student.isEmailVerified()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already registered");
            }
        }
        //check if the registration number already exists in db or not
        if(studentRepo.existsByRegNo(studentRegRequestDTO.getRegNo())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Registration number already registered");
        }
        //check if the password is valid
        if(studentRegRequestDTO.getPassword().isBlank() || studentRegRequestDTO.getPassword().length() < 8) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must be at least 8 characters long");
        }
        //check for strong password
        else if(!studentRegRequestDTO.getPassword().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Password must contain at least one lowercase letter, one uppercase letter, and one number");
        }
        else if(!studentRegRequestDTO.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email format");
        }
        else if(studentRegRequestDTO.getName().length() < 3) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at least 3 characters");
        }
        else if(studentRegRequestDTO.getName().length() > 15) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Username must be at most 15 characters");
        }

        if(student == null) {
            student = new Student();
            student.setEmail(studentRegRequestDTO.getEmail());
        }

        Department dept = departmentRepo.findById(studentRegRequestDTO.getDepartmentId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department not found"));
        student.setName(studentRegRequestDTO.getName());
        student.setPassword(encodePassword(studentRegRequestDTO.getPassword()));
        student.setRegNo(studentRegRequestDTO.getRegNo());
        student.setDepartment(dept);
        student.setEmailVerified(false);
        student.setRole("STUDENT");
        Student saved = studentRepo.save(student);

        AuthUser authUser = UserMapper.toAuthUser(saved);

        saved.setVerificationToken(jwtService.generateToken(authUser, Integer.parseInt(emailVerificationTokenValidityTime)));
        //TO-DO: send an email verification link
        studentRepo.save(saved);
        return UserMapper.toDTO(saved);
    }

    public Object getLoggedInUser(Authentication authentication) {
        String email = authentication.getName();
        String role = authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority();
        switch (role) {
            case "ROLE_TEACHER", "ROLE_HOD" -> {
                Teacher teacher = teacherRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
                return ResponseEntity.ok(UserMapper.toDTO(teacher));
            }
            case "ROLE_ADMIN" -> {
                Admin admin = adminRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "not found"));
                return ResponseEntity.ok(UserMapper.toDTO(admin));
            }
            case "ROLE_STUDENT" -> {
                Student student = studentRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Student not found"));
                return ResponseEntity.ok(UserMapper.toDTO(student));
            }
            default -> {
                return null;
            }
        }
    }
}
