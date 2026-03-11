package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.JobSeekerDTO;
import com.revhire.revhirep2.entity.JobSeeker;

public class JobSeekerMapper {

    public static JobSeeker toEntity(JobSeekerDTO dto) {

        JobSeeker jobSeeker = new JobSeeker();
        jobSeeker.setFullName(dto.getFullName());
        jobSeeker.setPhone(dto.getPhone());
        jobSeeker.setLocation(dto.getLocation());
        jobSeeker.setEmploymentStatus(dto.getEmploymentStatus());
        jobSeeker.setProfileSummary(dto.getProfileSummary());

        return jobSeeker;
    }

    public static JobSeekerDTO toDTO(JobSeeker entity) {

        JobSeekerDTO dto = new JobSeekerDTO();
        dto.setId(entity.getId());
        dto.setFullName(entity.getFullName());
        dto.setPhone(entity.getPhone());
        dto.setLocation(entity.getLocation());
        dto.setEmploymentStatus(entity.getEmploymentStatus());
        dto.setProfileSummary(entity.getProfileSummary());

        return dto;
    }
}