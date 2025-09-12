package com.example.utkarshbackend.mapper;

import com.example.utkarshbackend.dto.AlumniDto;
import com.example.utkarshbackend.entity.Alumni;
import org.springframework.stereotype.Component;

@Component
public class AlumniMapper {

    public Alumni toEntity(AlumniDto dto) {
        if (dto == null) {
            return null;
        }
        Alumni entity = new Alumni();
        entity.setFirstName(dto.getFirstName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPhone(dto.getPhone());
        entity.setGraduationYear(dto.getGraduationYear());
        entity.setDegree(dto.getDegree());
        entity.setOccupation(dto.getOccupation());
        entity.setCompany(dto.getCompany());
        entity.setLinkedin(dto.getLinkedin());
        entity.setTwitter(dto.getTwitter());
        entity.setGithub(dto.getGithub());
        return entity;
    }

    public AlumniDto toDto(Alumni entity) {
        if (entity == null) {
            return null;
        }
        AlumniDto dto = new AlumniDto();
        dto.setFirstName(entity.getFirstName());
        dto.setLastName(entity.getLastName());
        dto.setEmail(entity.getEmail());
        dto.setPhone(entity.getPhone());
        dto.setGraduationYear(entity.getGraduationYear());
        dto.setDegree(entity.getDegree());
        dto.setOccupation(entity.getOccupation());
        dto.setCompany(entity.getCompany());
        dto.setLinkedin(entity.getLinkedin());
        dto.setTwitter(entity.getTwitter());
        dto.setGithub(entity.getGithub());
        return dto;
    }
}
