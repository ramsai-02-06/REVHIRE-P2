package com.revhire.revhirep2.mapper;

import java.util.stream.Collectors;

import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.entity.Resume;

public class ResumeMapper {

    // ===============================
    // ENTITY → DTO
    // ===============================
    public static ResumeDTO toDTO(Resume resume) {

        ResumeDTO dto = new ResumeDTO();

        dto.setId(resume.getId());
        dto.setObjective(resume.getObjective());

        if (resume.getEducationList() != null) {
            dto.setEducationList(
                    resume.getEducationList()
                            .stream()
                            .map(EducationMapper::toDTO)
                            .collect(Collectors.toList())
            );
        }

        if (resume.getExperienceList() != null) {
            dto.setExperienceList(
                    resume.getExperienceList()
                            .stream()
                            .map(ExperienceMapper::toDTO)
                            .collect(Collectors.toList())
            );
        }

        if (resume.getSkills() != null) {
            dto.setSkillsList(
                    resume.getSkills()
                            .stream()
                            .map(SkillMapper::toDTO)
                            .collect(Collectors.toList())
            );
        }

        if (resume.getCertifications() != null) {
            dto.setCertificationsList(
                    resume.getCertifications()
                            .stream()
                            .map(CertificationMapper::toDTO)
                            .collect(Collectors.toList())
            );
        }

        if (resume.getProjects() != null) {
            dto.setProjectsList(
                    resume.getProjects()
                            .stream()
                            .map(ProjectMapper::toDTO)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // ===============================
    // DTO → ENTITY
    // ===============================
    public static Resume toEntity(ResumeDTO dto) {

        Resume resume = new Resume();
        resume.setObjective(dto.getObjective());

        if (dto.getEducationList() != null) {
            resume.setEducationList(
                    dto.getEducationList()
                            .stream()
                            .map(EducationMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getExperienceList() != null) {
            resume.setExperienceList(
                    dto.getExperienceList()
                            .stream()
                            .map(ExperienceMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getSkillsList() != null) {
            resume.setSkills(
                    dto.getSkillsList()
                            .stream()
                            .map(SkillMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getCertificationsList() != null) {
            resume.setCertifications(
                    dto.getCertificationsList()
                            .stream()
                            .map(CertificationMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        if (dto.getProjectsList() != null) {
            resume.setProjects(
                    dto.getProjectsList()
                            .stream()
                            .map(ProjectMapper::toEntity)
                            .collect(Collectors.toList())
            );
        }

        return resume;
    }
}