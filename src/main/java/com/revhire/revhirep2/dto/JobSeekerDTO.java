package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.*;

public class JobSeekerDTO {

    private Long id;

    @NotBlank(message = "Full name required")
    private String fullName;

    @NotBlank(message = "Phone number required")
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Phone must be 10 digits")
    private String phone;

    @NotBlank(message = "Location required")
    private String location;

    @NotBlank(message = "Employment status required")
    private String employmentStatus;

    @Size(max = 1000, message = "Profile summary too long")
    private String profileSummary;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getEmploymentStatus() {
		return employmentStatus;
	}

	public void setEmploymentStatus(String employmentStatus) {
		this.employmentStatus = employmentStatus;
	}

	public String getProfileSummary() {
		return profileSummary;
	}

	public void setProfileSummary(String profileSummary) {
		this.profileSummary = profileSummary;
	}

    // Getters and Setters
}