package com.example.utkarshbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String regNo;
    private String rollNo;
    private String password;
    private String gender;
    private String profilePic;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String dob;
    private String admissionYear;
    private String admissionStatus;
    private String role;
    private boolean isEmailVerified;
    private String verificationToken;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "student_course",
            joinColumns = @JoinColumn(name = "student_regNo"),
            inverseJoinColumns = @JoinColumn(name = "course_code")
    )
    private List<Course> courses;
}
