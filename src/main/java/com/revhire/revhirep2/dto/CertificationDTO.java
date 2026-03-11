package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.NotBlank;

public class CertificationDTO {

    private Long id;
    @NotBlank(message = "Full name required")
    private String name;
    @NotBlank(message = "Full name required")
    private String issuedBy;
    @NotBlank(message = "Full name required")
    private String issueDate;

    public CertificationDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getIssuedBy() {
		return issuedBy;
	}

	public void setIssuedBy(String issuedBy) {
		this.issuedBy = issuedBy;
	}

	public String getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(String issueDate) {
		this.issueDate = issueDate;
	}

    // getters and setters
}