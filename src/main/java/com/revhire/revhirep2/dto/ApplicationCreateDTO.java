package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class ApplicationCreateDTO {

    @NotNull(message = "Job ID is required")
    private Long jobId;

    @NotNull(message = "JobSeeker ID is required")
    private Long jobSeekerId;

    @NotNull(message = "Resume ID is required")
    private Long resumeId;

    @NotNull(message = "Resume File ID is required")
    private Long resumeFileId;

    @Size(max = 2000, message = "Cover letter too long")
    private String coverLetter;

	public Long getJobId() {
		return jobId;
	}

	public void setJobId(Long jobId) {
		this.jobId = jobId;
	}

	public Long getJobSeekerId() {
		return jobSeekerId;
	}

	public void setJobSeekerId(Long jobSeekerId) {
		this.jobSeekerId = jobSeekerId;
	}

	public Long getResumeId() {
		return resumeId;
	}

	public void setResumeId(Long resumeId) {
		this.resumeId = resumeId;
	}

	public Long getResumeFileId() {
		return resumeFileId;
	}

	public void setResumeFileId(Long resumeFileId) {
		this.resumeFileId = resumeFileId;
	}

	public String getCoverLetter() {
		return coverLetter;
	}

	public void setCoverLetter(String coverLetter) {
		this.coverLetter = coverLetter;
	}

    // Getters and Setters
}