package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.EducationDTO;
import com.revhire.revhirep2.entity.Education;

import java.time.format.DateTimeFormatter;

public class EducationMapper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static EducationDTO toDTO(Education education) {

        EducationDTO dto = new EducationDTO();

        dto.setId(education.getId());
        dto.setDegree(education.getDegree());
        dto.setInstitution(education.getInstitution());

        if (education.getStartDate() != null) {
            dto.setStartDate(
                    education.getStartDate().format(formatter)
            );
        }

        if (education.getEndDate() != null) {
            dto.setEndDate(
                    education.getEndDate().format(formatter)
            );
        }

        dto.setGrade(education.getGrade());

        return dto;
    }
    
    public static Education toEntity(EducationDTO dto) {

        Education education = new Education();

        education.setDegree(dto.getDegree());
        education.setInstitution(dto.getInstitution());
        education.setGrade(dto.getGrade());

        if (dto.getStartDate() != null) {
            education.setStartDate(
                    java.time.LocalDate.parse(dto.getStartDate())
            );
        }

        if (dto.getEndDate() != null) {
            education.setEndDate(
                    java.time.LocalDate.parse(dto.getEndDate())
            );
        }

        return education;
    }
}