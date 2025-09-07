package com.example.utkarshbackend.services;
import com.example.utkarshbackend.dto.AuthUser;
import com.example.utkarshbackend.entity.Admin;
import com.example.utkarshbackend.entity.Student;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.AdminRepo;
import com.example.utkarshbackend.repository.StudentRepo;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TeacherRepo teacherRepo;
    private final AdminRepo adminRepo;
    private final StudentRepo studentRepo;

    public UserDetailsServiceImpl(TeacherRepo teacherRepo, AdminRepo adminRepo, StudentRepo studentRepo) {
        this.teacherRepo = teacherRepo;
        this.adminRepo = adminRepo;
        this.studentRepo = studentRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        Optional<Admin> admin = adminRepo.findByEmail(email);
        if(admin.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(admin.get().getId());
            user.setEmail(admin.get().getEmail());
            user.setRole(admin.get().getRole());
            user.setPassword(admin.get().getPassword());
            return buildUserDetails(user);
        }

        Optional<Teacher> teacher = teacherRepo.findByEmail(email);
        if(teacher.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(teacher.get().getId());
            user.setEmail(teacher.get().getEmail());
            user.setRole(teacher.get().getRole());
            user.setPassword(teacher.get().getPassword());
            return buildUserDetails(user);
        }

        Optional<Student> student = studentRepo.findByEmail(email);
        if(student.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(student.get().getId());
            user.setEmail(student.get().getEmail());
            user.setRole(student.get().getRole());
            user.setPassword(student.get().getPassword());
            return buildUserDetails(user);
        }

        throw new UsernameNotFoundException("User not found");

    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
//        User user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<Admin> admin = adminRepo.findById(id);
        if(admin.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(admin.get().getId());
            user.setEmail(admin.get().getEmail());
            user.setRole(admin.get().getRole());
            user.setPassword(admin.get().getPassword());
            return buildUserDetails(user);
        }
        Optional<Teacher> teacher = teacherRepo.findById(id);
        if(teacher.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(teacher.get().getId());
            user.setEmail(teacher.get().getEmail());
            user.setRole(teacher.get().getRole());
            user.setPassword(teacher.get().getPassword());
            return buildUserDetails(user);
        }
        Optional<Student> student = studentRepo.findById(id);
        if(student.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(student.get().getId());
            user.setEmail(student.get().getEmail());
            user.setRole(student.get().getRole());
            user.setPassword(student.get().getPassword());
            return buildUserDetails(user);
        }

        throw new UsernameNotFoundException("User not found");
    }

    private UserDetails buildUserDetails(AuthUser user) {
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole())
                .accountExpired(false)
                .credentialsExpired(false)
                .accountLocked(false)
                .disabled(false)
                .build();
    }
}
