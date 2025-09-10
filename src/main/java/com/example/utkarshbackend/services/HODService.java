package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.DepartmentDTO;
import com.example.utkarshbackend.dto.EmailMessageReqDTO;
import com.example.utkarshbackend.dto.TeacherDetailsDTO;
import com.example.utkarshbackend.dto.TeacherRegReqDTO;
import com.example.utkarshbackend.entity.ContactPageData;
import com.example.utkarshbackend.entity.Department;
import com.example.utkarshbackend.entity.NonTeaching;
import com.example.utkarshbackend.entity.Teacher;
import com.example.utkarshbackend.mapper.DepartmentMapper;
import com.example.utkarshbackend.mapper.TeacherMapper;
import com.example.utkarshbackend.repository.ContactPageDataRepo;
import com.example.utkarshbackend.repository.DepartmentRepo;
import com.example.utkarshbackend.repository.NonTeachingRepo;
import com.example.utkarshbackend.repository.TeacherRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class HODService {

    private final TeacherRepo teacherRepo;
    private final DepartmentRepo departmentRepo;
    private final NonTeachingRepo nonTeachingRepo;
    private final ContactPageDataRepo contactPageDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public HODService(TeacherRepo teacherRepo, DepartmentRepo departmentRepo, NonTeachingRepo nonTeachingRepo, ContactPageDataRepo contactPageDataRepo, PasswordEncoder passwordEncoder, EmailService emailService) {
        this.teacherRepo = teacherRepo;
        this.departmentRepo = departmentRepo;
        this.nonTeachingRepo = nonTeachingRepo;
        this.contactPageDataRepo = contactPageDataRepo;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public boolean doesTeacherExistByEmail (String email) {
        return teacherRepo.existsByEmail (email);
    }

    public ResponseEntity<TeacherDetailsDTO> registerNewTeacher(TeacherRegReqDTO teacherRegReqDTO) {
        if(teacherRegReqDTO.getEmail() == null || teacherRegReqDTO.getName() == null || teacherRegReqDTO.getPassword() == null || teacherRegReqDTO.getEmail().isBlank() || teacherRegReqDTO.getName().isBlank() || teacherRegReqDTO.getPassword().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email, Name or Password are Required");
        }

        if(doesTeacherExistByEmail(teacherRegReqDTO.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email already exists");
        }

        Department dept = departmentRepo.findById(teacherRegReqDTO.getDepartmentId()).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Department not found"));

        Teacher teacher = Teacher.builder()
                .name(teacherRegReqDTO.getName())
                .email(teacherRegReqDTO.getEmail())
                .password(passwordEncoder.encode(teacherRegReqDTO.getPassword()))
                .profilePic(teacherRegReqDTO.getProfilePic())
                .department(dept)
                .designation(teacherRegReqDTO.getDesignation())
                .education(teacherRegReqDTO.getEducation())
                .phone(teacherRegReqDTO.getPhone())
                .role("TEACHER")
                .build();
        Teacher savedTeacher = teacherRepo.save(teacher);

        TeacherDetailsDTO dto = TeacherMapper.toDTO(savedTeacher);

        return ResponseEntity.ok().body(dto);
    }

    public Page<TeacherDetailsDTO> getAllTeachersRoles(Pageable p) {
        Page<Teacher> teacherPage = teacherRepo.findAllByRole("TEACHER", p);

        return teacherPage.map(TeacherMapper::toDTO);
    }

    public DepartmentDTO editDepartment(Department dept, Long deptId, Authentication authentication) {
        Department existing = departmentRepo.findById(deptId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found"));

        //check if the user is not admin
        if(!authentication.getAuthorities().stream().findFirst().orElseThrow().getAuthority().equalsIgnoreCase("ROLE_ADMIN")) {
            //Find a teacher using authentication email
            String email = authentication.getName();
            Teacher teacher = teacherRepo.findByEmail(email).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

            //check if the department belongs to the hod
            if(!teacher.getDepartment().getId().equals(existing.getId())) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to edit this department");
            }
        }

        if(dept.getCode() != null && !dept.getCode().isBlank()) {
            existing.setCode(dept.getCode());
        }
        if(dept.getName() != null && !dept.getName().isBlank()) {
            existing.setName(dept.getName());
        }
        if(dept.getDescription() != null && !dept.getDescription().isBlank()) {
            existing.setDescription(dept.getDescription());
        }
        Department saved = departmentRepo.save(existing);
        return DepartmentMapper.toDTO(saved);
    }

    public ResponseEntity<TeacherDetailsDTO> deleteTeacher(Long id, Authentication authentication) {
        Teacher teacher = teacherRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));
        if (!authentication.getAuthorities().contains("ROLE_ADMIN")) {
            //throw error is hod is being delete by another hod
            if(teacher.getRole().equalsIgnoreCase("HOD")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "You are not authorized to delete this HOD");
            }
        }

        TeacherDetailsDTO dto = TeacherMapper.toDTO(teacher);
        teacherRepo.delete(teacher);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<TeacherDetailsDTO> updateTeacher(TeacherRegReqDTO teacherRegReqDTO, Long id) {
        Teacher teacher = teacherRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher not found"));

        if(teacherRegReqDTO.getName() != null && !teacherRegReqDTO.getName().isBlank()) {
            teacher.setName(teacherRegReqDTO.getName());
        }
        if(teacherRegReqDTO.getEmail() != null && !teacherRegReqDTO.getEmail().isBlank()) {
            teacher.setEmail(teacherRegReqDTO.getEmail());
        }
        if(teacherRegReqDTO.getPhone() != null && !teacherRegReqDTO.getPhone().isBlank()) {
            teacher.setPhone(teacherRegReqDTO.getPhone());
        }
        if(teacherRegReqDTO.getEducation() != null && !teacherRegReqDTO.getEducation().isBlank()) {
            teacher.setEducation(teacherRegReqDTO.getEducation());
        }
        if(teacherRegReqDTO.getDesignation() != null && !teacherRegReqDTO.getDesignation().isBlank()) {
            teacher.setDesignation(teacherRegReqDTO.getDesignation());
        }
        if(teacherRegReqDTO.getProfilePic() != null && !teacherRegReqDTO.getProfilePic().isBlank()) {
            teacher.setProfilePic(teacherRegReqDTO.getProfilePic());
        }
        Teacher savedTeacher = teacherRepo.save(teacher);
        TeacherDetailsDTO dto = TeacherMapper.toDTO(savedTeacher);
        return ResponseEntity.ok(dto);
    }

    public ResponseEntity<NonTeaching> registerNewNonTeacher(NonTeaching nonTeaching) {
        if (nonTeaching.getEmail() == null || nonTeaching.getName() == null || nonTeaching.getEmail().isBlank() || nonTeaching.getName().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email or Name is Required");
        }
        NonTeaching saved = nonTeachingRepo.save(nonTeaching);
        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<NonTeaching> updateNonTeacher(NonTeaching nonTeaching, Long id) {
        NonTeaching existing = nonTeachingRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non Teaching Staff not found"));
        if (nonTeaching.getEmail() != null && !nonTeaching.getEmail().isBlank()) {
            existing.setEmail(nonTeaching.getEmail());
        }
        if (nonTeaching.getName() != null && !nonTeaching.getName().isBlank()) {
            existing.setName(nonTeaching.getName());
        }
        if (nonTeaching.getPhone() != null && !nonTeaching.getPhone().isBlank()) {
            existing.setPhone(nonTeaching.getPhone());
        }
        if (nonTeaching.getDesignation() != null && !nonTeaching.getDesignation().isBlank()) {
            existing.setDesignation(nonTeaching.getDesignation());
        }
        if (nonTeaching.getProfilePic() != null && !nonTeaching.getProfilePic().isBlank()) {
            existing.setProfilePic(nonTeaching.getProfilePic());
        }
        if (nonTeaching.getEducation() != null && !nonTeaching.getEducation().isBlank()) {
            existing.setEducation(nonTeaching.getEducation());
        }
        if (nonTeaching.getDepartment() != null && !nonTeaching.getDepartment().isBlank()) {
            existing.setDepartment(nonTeaching.getDepartment());
        }
        NonTeaching saved = nonTeachingRepo.save(existing);
        return ResponseEntity.ok(saved);
    }

    public ResponseEntity<NonTeaching> deleteNonTeacher(Long id) {
        NonTeaching nonTeaching = nonTeachingRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Non Teaching Staff not found"));
        nonTeachingRepo.delete(nonTeaching);
        return ResponseEntity.ok(nonTeaching);
    }

    public Page<ContactPageData> getAllContactUsData(Pageable p) {
        return contactPageDataRepo.findAll(p);
    }

    public ContactPageData getContactUsDataById(Long id) {
        return contactPageDataRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Page Data not found"));
    }

    public ResponseEntity<ContactPageData> deleteContactUsDataById(Long id) {
        ContactPageData contactPageData = contactPageDataRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Page Data not found"));
        contactPageDataRepo.delete(contactPageData);
        return ResponseEntity.ok(contactPageData);
    }

    public ResponseEntity<ContactPageData> sendEmail(Long id, EmailMessageReqDTO emailMessageReqDTO) {
        ContactPageData contactPageData = contactPageDataRepo.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Contact Page Data not found"));
        if(emailMessageReqDTO.getSubject() == null || emailMessageReqDTO.getSubject().isBlank()) {
            emailMessageReqDTO.setSubject("Thanks for reaching out to UCET help and support");
        }
        emailService.sendContactUsEmail(contactPageData.getEmail(), emailMessageReqDTO.getSubject(), emailMessageReqDTO.getMessage());
        return ResponseEntity.ok(contactPageData);
    }
}
