package com.revhire.revhirep2.dto;

import java.util.List;

public class ResumeDTO {

    private Long id;
    private String objective;

    private List<EducationDTO> educationList;
    private List<ExperienceDTO> experienceList;
    private List<SkillDTO> skillsList;
    private List<CertificationDTO> certificationsList;
    private List<ProjectDTO> projectsList;

    public ResumeDTO() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObjective() {
        return objective;
    }

    public void setObjective(String objective) {
        this.objective = objective;
    }

    public List<EducationDTO> getEducationList() {
        return educationList;
    }

    public void setEducationList(List<EducationDTO> educationList) {
        this.educationList = educationList;
    }

    public List<ExperienceDTO> getExperienceList() {
        return experienceList;
    }

    public void setExperienceList(List<ExperienceDTO> experienceList) {
        this.experienceList = experienceList;
    }

    public List<SkillDTO> getSkillsList() {
        return skillsList;
    }

    public void setSkillsList(List<SkillDTO> skillsList) {
        this.skillsList = skillsList;
    }

    public List<CertificationDTO> getCertificationsList() {
        return certificationsList;
    }

    public void setCertificationsList(List<CertificationDTO> certificationsList) {
        this.certificationsList = certificationsList;
    }

    public List<ProjectDTO> getProjectsList() {
        return projectsList;
    }

    public void setProjectsList(List<ProjectDTO> projectsList) {
        this.projectsList = projectsList;
    }
}