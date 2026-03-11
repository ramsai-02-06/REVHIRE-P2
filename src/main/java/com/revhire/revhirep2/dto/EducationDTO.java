package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.NotBlank;

public class EducationDTO {

    private Long id;
    @NotBlank(message = "degree name required")
    private String degree;
    @NotBlank(message = "institution name required")
    private String institution;
    @NotBlank(message = "start date yyyy-mm-dd required")
    private String startDate;
    @NotBlank(message = "end date yyyy-mm-dd required")
    private String endDate;
    @NotBlank(message = "grade is required")
    private String grade;

    public EducationDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDegree() {
		return degree;
	}

	public void setDegree(String degree) {
		this.degree = degree;
	}

	public String getInstitution() {
		return institution;
	}

	public void setInstitution(String institution) {
		this.institution = institution;
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

	public String getGrade() {
		return grade;
	}

	public void setGrade(String grade) {
		this.grade = grade;
	}

    // getters and setters
}