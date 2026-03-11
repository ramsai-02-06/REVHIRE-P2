package com.revhire.revhirep2.service;

import java.util.List;

import com.revhire.revhirep2.dto.ApplicationCreateDTO;
import com.revhire.revhirep2.dto.ApplicationResponseDTO;
import com.revhire.revhirep2.dto.EmployerApplicationViewDTO;

public interface ApplicationService {

    void apply(ApplicationCreateDTO dto);

    void withdraw(Long applicationId, Long jobSeekerId);

    List<ApplicationResponseDTO> getMyApplications(Long jobSeekerId);

    List<EmployerApplicationViewDTO> getApplicantsForJob(Long jobId, Long employerId);

    List<EmployerApplicationViewDTO> getAllApplicationsForEmployer(Long employerId);
    
    // ✅ NEW
    void bulkUpdateStatus(String employerEmail, List<Long> applicationIds, String status);
    
    void updateStatus(Long applicationId, Long employerId, String status);
}