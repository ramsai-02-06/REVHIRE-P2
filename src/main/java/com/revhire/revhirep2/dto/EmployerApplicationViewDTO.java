package com.revhire.revhirep2.dto;

import java.time.LocalDateTime;

public class EmployerApplicationViewDTO {

    private Long applicationId;
    private String status;
    private LocalDateTime appliedAt;

    // Job information
    private String jobTitle;

    // JobSeeker profile
    private String fullName;
    private String email;
    private String phone;
    private String location;
    private String employmentStatus;
    private String profileSummary;

    // Resume
    private ResumeDTO resume;

    // Resume file
    private ResumeFileDTO resumeFile;

    private String coverLetter;

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getAppliedAt() {
        return appliedAt;
    }

    public void setAppliedAt(LocalDateTime appliedAt) {
        this.appliedAt = appliedAt;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public ResumeDTO getResume() {
        return resume;
    }

    public void setResume(ResumeDTO resume) {
        this.resume = resume;
    }

    public ResumeFileDTO getResumeFile() {
        return resumeFile;
    }

    public void setResumeFile(ResumeFileDTO resumeFile) {
        this.resumeFile = resumeFile;
    }

    public String getCoverLetter() {
        return coverLetter;
    }

    public void setCoverLetter(String coverLetter) {
        this.coverLetter = coverLetter;
    }
}