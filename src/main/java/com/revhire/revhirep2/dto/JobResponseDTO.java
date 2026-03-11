package com.revhire.revhirep2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public class JobResponseDTO {

    private Long id;
    private String title;
    private String description;
    private String companyName;
    private String location;
    private String skillsRequired;
    private BigDecimal salaryMin;
    private BigDecimal salaryMax;
    private Integer experienceRequired;
    private String educationRequired;
    private String jobType;
    private LocalDate deadline;
    private Integer openings;
    private String status;

    public JobResponseDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getSkillsRequired() {
		return skillsRequired;
	}

	public void setSkillsRequired(String skillsRequired) {
		this.skillsRequired = skillsRequired;
	}

	public BigDecimal getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(BigDecimal bigDecimal) {
		this.salaryMin = bigDecimal;
	}

	public BigDecimal getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(BigDecimal bigDecimal) {
		this.salaryMax = bigDecimal;
	}
	

	public Integer getExperienceRequired() {
		return experienceRequired;
	}

	public void setExperienceRequired(Integer experienceRequired) {
		this.experienceRequired = experienceRequired;
	}

	public String getEducationRequired() {
		return educationRequired;
	}

	public void setEducationRequired(String educationRequired) {
		this.educationRequired = educationRequired;
	}

	public LocalDate getDeadline() {
		return deadline;
	}

	public void setDeadline(LocalDate deadline) {
		this.deadline = deadline;
	}

	public Integer getOpenings() {
		return openings;
	}

	public void setOpenings(Integer openings) {
		this.openings = openings;
	}

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

    // getters and setters
}