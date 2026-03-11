package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.ApplicationResponseDTO;
import com.revhire.revhirep2.entity.Application;

public class ApplicationMapper {

    public static ApplicationResponseDTO toResponseDTO(Application app) {

        ApplicationResponseDTO dto = new ApplicationResponseDTO();

        dto.setId(app.getId());
        dto.setJobId(app.getJob().getId());
        dto.setJobTitle(app.getJob().getTitle());
        dto.setCompanyName(app.getJob().getEmployer().getCompanyName());
        dto.setStatus(app.getStatus());
        dto.setAppliedAt(app.getAppliedAt());

        return dto;
    }
}