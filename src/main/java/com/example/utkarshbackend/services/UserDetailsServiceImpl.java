package com.example.utkarshbackend.services;
import com.example.utkarshbackend.dto.AuthUser;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;

    private final TeacherRepo teacherRepo;

    public UserDetailsServiceImpl(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        if(email.equalsIgnoreCase(adminEmail)) {
            return User.withUsername(adminEmail)
                    .password("{noop}" + adminPassword)
                    .roles("ADMIN")
                    .build();
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

        throw new UsernameNotFoundException("User not found");

    }

    public UserDetails loadUserById(long id) throws UsernameNotFoundException {
//        User user = userRepo.findById(id).orElseThrow(() -> new UsernameNotFoundException("User not found"));
        Optional<Teacher> teacher = teacherRepo.findById(id);
        if(teacher.isPresent()) {
            AuthUser user = new AuthUser();
            user.setId(teacher.get().getId());
            user.setEmail(teacher.get().getEmail());
            user.setRole(teacher.get().getRole());
            user.setPassword(teacher.get().getPassword());
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
