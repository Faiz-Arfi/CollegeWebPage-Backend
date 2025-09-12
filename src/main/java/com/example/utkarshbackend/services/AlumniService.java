package com.example.utkarshbackend.services;

import com.example.utkarshbackend.dto.AlumniDto;
import com.example.utkarshbackend.entity.Alumni;
import com.example.utkarshbackend.mapper.AlumniMapper;
import com.example.utkarshbackend.repository.AlumniRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AlumniService {

    private final AlumniRepository alumniRepository;
    private final AlumniMapper alumniMapper;

    @Autowired
    public AlumniService(AlumniRepository alumniRepository, AlumniMapper alumniMapper) {
        this.alumniRepository = alumniRepository;
        this.alumniMapper = alumniMapper;
    }

    public AlumniDto registerAlumni(AlumniDto alumniDto) {

        alumniRepository.findByEmail(alumniDto.getEmail()).ifPresent(alumni -> {
            throw new IllegalStateException("An alumnus with email " + alumniDto.getEmail() + " already exists.");
        });

        Alumni alumni = alumniMapper.toEntity(alumniDto);

        Alumni savedAlumni = alumniRepository.save(alumni);

        return alumniMapper.toDto(savedAlumni);
    }

    public List<AlumniDto> getAllAlumni() {
        return alumniRepository.findAll().stream().map(alumniMapper::toDto).toList();
    }
}
