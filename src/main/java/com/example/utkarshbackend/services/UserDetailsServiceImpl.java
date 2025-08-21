package com.example.utkarshbackend.services;
import com.example.utkarshbackend.dto.AuthUser;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final TeacherRepo teacherRepo;

    public UserDetailsServiceImpl(TeacherRepo teacherRepo) {
        this.teacherRepo = teacherRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

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
