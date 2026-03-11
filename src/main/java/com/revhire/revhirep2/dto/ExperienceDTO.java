package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.NotBlank;

public class ExperienceDTO {

    private Long id;
    @NotBlank(message = "companyName required")
    private String companyName;
    @NotBlank(message = "Job Titlerequired")
    private String jobTitle;
    @NotBlank(message = "start date yyyy-mm-dd required")
    private String startDate;
    @NotBlank(message = "end date yyyy-mm-dd required")
    private String endDate;
    @NotBlank(message = "Descripition is  required")
    private String description;

    public ExperienceDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCompanyName() {
		return companyName;
	}

	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}

	public String getJobTitle() {
		return jobTitle;
	}

	public void setJobTitle(String jobTitle) {
		this.jobTitle = jobTitle;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

    // getters and setters
}