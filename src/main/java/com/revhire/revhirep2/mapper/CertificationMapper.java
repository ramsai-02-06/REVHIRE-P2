package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.CertificationDTO;
import com.revhire.revhirep2.entity.Certification;

import java.time.format.DateTimeFormatter;

public class CertificationMapper {

    private static final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public static CertificationDTO toDTO(Certification certification) {

        CertificationDTO dto = new CertificationDTO();

        dto.setId(certification.getId());
        dto.setName(certification.getName());
        dto.setIssuedBy(certification.getIssuedBy());

        if (certification.getIssueDate() != null) {
            dto.setIssueDate(
                    certification.getIssueDate().format(formatter)
            );
        }

        return dto;
    }
    
    public static Certification toEntity(CertificationDTO dto) {

        Certification certification = new Certification();

        certification.setName(dto.getName());
        certification.setIssuedBy(dto.getIssuedBy());

        if (dto.getIssueDate() != null) {
            certification.setIssueDate(
                    java.time.LocalDate.parse(dto.getIssueDate())
            );
        }

        return certification;
    }
}