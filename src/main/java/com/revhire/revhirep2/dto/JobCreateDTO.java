package com.revhire.revhirep2.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class JobCreateDTO {

    @NotBlank(message = "Title is required")
    private String title;

    @NotBlank(message = "Description is required")
    private String description;
    
    @NotBlank(message = "CompanyName is required")
    private String companyName;
    
    @NotBlank(message = "skills is required")
    private String skillsRequired;
  

	@NotBlank(message = "Location is required")
    private String location;

    @NotNull(message = "Minimum salary required")
    @Positive(message = "Salary must be positive")
    private BigDecimal salaryMin;

    @NotNull(message = "Maximum salary required")
    @Positive(message = "Salary must be positive")
    private BigDecimal salaryMax;

    @NotNull(message = "Experience required")
    @Min(value = 0, message = "Experience cannot be negative")
    private Integer experienceRequired;

    @NotBlank(message = "Education required")
    private String educationRequired;

    @NotBlank(message = "Job type required")
    private String jobType;
    
    @NotNull(message = "Deadline is required")
    @Future(message = "Deadline must be future date")
    private LocalDate deadline;

    @NotNull(message = "Openings required")
    @Min(value = 1, message = "At least one opening required")
    private Integer openings;

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

	public String getSkillsRequired() {
		return skillsRequired;
	}

	public void setSkillsRequired(String skillsRequired) {
		this.skillsRequired = skillsRequired;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public BigDecimal getSalaryMin() {
		return salaryMin;
	}

	public void setSalaryMin(BigDecimal salaryMin) {
		this.salaryMin = salaryMin;
	}

	public BigDecimal getSalaryMax() {
		return salaryMax;
	}

	public void setSalaryMax(BigDecimal salaryMax) {
		this.salaryMax = salaryMax;
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

	public String getJobType() {
		return jobType;
	}

	public void setJobType(String jobType) {
		this.jobType = jobType;
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

    // Getters and Setters
}