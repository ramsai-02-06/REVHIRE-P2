package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.ExperienceDTO;
import com.revhire.revhirep2.entity.Experience;

import java.time.format.DateTimeFormatter;

public class ExperienceMapper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static ExperienceDTO toDTO(Experience experience) {

        ExperienceDTO dto = new ExperienceDTO();

        dto.setId(experience.getId());
        dto.setCompanyName(experience.getCompanyName());
        dto.setJobTitle(experience.getJobTitle());

        if (experience.getStartDate() != null) {
            dto.setStartDate(
                    experience.getStartDate().format(formatter)
            );
        }

        if (experience.getEndDate() != null) {
            dto.setEndDate(
                    experience.getEndDate().format(formatter)
            );
        }

        dto.setDescription(experience.getDescription());

        return dto;
    }
    
    public static Experience toEntity(ExperienceDTO dto) {

        Experience experience = new Experience();

        experience.setCompanyName(dto.getCompanyName());
        experience.setJobTitle(dto.getJobTitle());
        experience.setDescription(dto.getDescription());

        if (dto.getStartDate() != null) {
            experience.setStartDate(
                    java.time.LocalDate.parse(dto.getStartDate())
            );
        }

        if (dto.getEndDate() != null) {
            experience.setEndDate(
                    java.time.LocalDate.parse(dto.getEndDate())
            );
        }

        return experience;
    }
}