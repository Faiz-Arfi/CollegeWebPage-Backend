package com.example.utkarshbackend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AlumniDto {

    @NotEmpty(message = "First name cannot be empty.")
    @Size(min = 2, message = "First name must have at least 2 characters.")
    private String firstName;

    @NotEmpty(message = "Last name cannot be empty.")
    @Size(min = 2, message = "Last name must have at least 2 characters.")
    private String lastName;

    @NotEmpty(message = "Email cannot be empty.")
    @Email(message = "Please provide a valid email address.")
    private String email;

    private String phone;

    @NotNull(message = "Graduation year cannot be null.")
    private Integer graduationYear;

    @NotEmpty(message = "Degree and major cannot be empty.")
    private String degree;

    private String occupation;
    private String company;
    private String linkedin;
    private String twitter;
    private String github;
}
