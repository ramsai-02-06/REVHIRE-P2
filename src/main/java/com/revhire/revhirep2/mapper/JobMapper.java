package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.JobCreateDTO;
import com.revhire.revhirep2.dto.JobResponseDTO;
import com.revhire.revhirep2.entity.Job;

public class JobMapper {

    public static Job toEntity(JobCreateDTO dto) {

        Job job = new Job();
        job.setTitle(dto.getTitle());
        job.setDescription(dto.getDescription());
        job.setCompanyName(dto.getCompanyName()); 
        job.setSkillsRequired(dto.getSkillsRequired());
        job.setLocation(dto.getLocation());
        job.setSalaryMin(dto.getSalaryMin());
        job.setSalaryMax(dto.getSalaryMax());
        job.setExperienceRequired(dto.getExperienceRequired());
        job.setEducationRequired(dto.getEducationRequired());
        job.setJobType(dto.getJobType());
        job.setDeadline(dto.getDeadline());
        job.setOpenings(dto.getOpenings());

        return job;
    }

    public static JobResponseDTO toDTO(Job job) {

        JobResponseDTO dto = new JobResponseDTO();
        dto.setId(job.getId());
        dto.setTitle(job.getTitle());
        dto.setDescription(job.getDescription());
        if (job.getEmployer() != null) {
            dto.setCompanyName(job.getEmployer().getCompanyName());
        }
        dto.setSkillsRequired(job.getSkillsRequired());
        dto.setLocation(job.getLocation());
        dto.setSalaryMin(job.getSalaryMin());
        dto.setSalaryMax(job.getSalaryMax()); 
        dto.setExperienceRequired(job.getExperienceRequired());
        dto.setEducationRequired(job.getEducationRequired());
        dto.setJobType(job.getJobType());
        dto.setDeadline(job.getDeadline());
        dto.setOpenings(job.getOpenings());
        dto.setStatus(job.getStatus());

        return dto;
    }
}