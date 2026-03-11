package com.revhire.revhirep2.service;

import java.util.Optional;

import com.revhire.revhirep2.dto.JobSeekerDTO;
import com.revhire.revhirep2.entity.JobSeeker;

public interface JobSeekerService {

    JobSeeker createProfile(JobSeeker jobSeeker);

    Optional<JobSeekerDTO> getProfile(Long id);

    JobSeekerDTO updateProfile(Long id, JobSeekerDTO dto);

    Optional<JobSeeker> getByUserId(Long userId);

    Optional<JobSeeker> getById(Long id);
    
    void createProfile(Long userId, JobSeekerDTO dto);
    
    
}