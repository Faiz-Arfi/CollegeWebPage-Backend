package com.example.utkarshbackend.controller;

import com.example.utkarshbackend.dto.AlumniDto;
import com.example.utkarshbackend.services.AlumniService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/alumni")
public class AlumniController {

    private final AlumniService alumniService;

    public AlumniController(AlumniService alumniService) {
        this.alumniService = alumniService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerAlumni(@Valid @RequestBody AlumniDto alumniDto) {
        try {
            AlumniDto registeredAlumni = alumniService.registerAlumni(alumniDto);
            return new ResponseEntity<>(registeredAlumni, HttpStatus.CREATED);
        } catch (IllegalStateException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("message", "An unexpected error occurred."));
        }
    }

    @GetMapping
    public ResponseEntity<List<AlumniDto>> getAllAlumni() {
        List<AlumniDto> alumniList = alumniService.getAllAlumni();
        return ResponseEntity.ok(alumniList);
    }
}
