package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.EmployerDTO;
import com.revhire.revhirep2.entity.Employer;

public class EmployerMapper {

    public static EmployerDTO toDTO(Employer employer) {

        EmployerDTO dto = new EmployerDTO();
        dto.setId(employer.getId());
        dto.setCompanyName(employer.getCompanyName());
        dto.setIndustry(employer.getIndustry());
        dto.setCompanySize(employer.getCompanySize());
        dto.setDescription(employer.getDescription());
        dto.setWebsite(employer.getWebsite());
        dto.setLocation(employer.getLocation());

        return dto;
    }

    public static Employer toEntity(EmployerDTO dto) {

        Employer employer = new Employer();
        employer.setCompanyName(dto.getCompanyName());
        employer.setIndustry(dto.getIndustry());
        employer.setCompanySize(dto.getCompanySize());
        employer.setDescription(dto.getDescription());
        employer.setWebsite(dto.getWebsite());
        employer.setLocation(dto.getLocation());

        return employer;
    }
}