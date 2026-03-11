package com.revhire.revhirep2.dto;

public class SavedJobDTO {

    private Long id;
    private Long jobId;
    private Long jobSeekerId;

    public SavedJobDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

    // getters and setters
}
