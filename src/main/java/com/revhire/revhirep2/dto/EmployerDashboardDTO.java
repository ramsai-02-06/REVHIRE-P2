package com.revhire.revhirep2.dto;

public class EmployerDashboardDTO {

    private long totalJobs;
    private long activeJobs;
    private long totalApplications;
    private long pendingReviews;

    public EmployerDashboardDTO() {}

    public EmployerDashboardDTO(long totalJobs,
                                long activeJobs,
                                long totalApplications,
                                long pendingReviews) {
        this.totalJobs = totalJobs;
        this.activeJobs = activeJobs;
        this.totalApplications = totalApplications;
        this.pendingReviews = pendingReviews;
    }

    public long getTotalJobs() { return totalJobs; }
    public long getActiveJobs() { return activeJobs; }
    public long getTotalApplications() { return totalApplications; }
    public long getPendingReviews() { return pendingReviews; }
}