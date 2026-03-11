package com.revhire.revhirep2.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.JobSeekerDTO;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.User;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.mapper.JobSeekerMapper;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.UserRepository;
import com.revhire.revhirep2.service.JobSeekerService;

@Service
public class JobSeekerServiceImpl implements JobSeekerService {

    private final JobSeekerRepository jobSeekerRepository;
    private final UserRepository userRepository;

    public JobSeekerServiceImpl(JobSeekerRepository jobSeekerRepository,
                                UserRepository userRepository) {
        this.jobSeekerRepository = jobSeekerRepository;
        this.userRepository = userRepository;
    }

    @Override
    public void createProfile(Long userId, JobSeekerDTO dto) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        JobSeeker jobSeeker = JobSeekerMapper.toEntity(dto);
        jobSeeker.setUser(user);

        jobSeekerRepository.save(jobSeeker);
    }
    @Override
    public JobSeeker createProfile(JobSeeker jobSeeker) {

        if (jobSeekerRepository.existsByUserId(jobSeeker.getUser().getId())) {
            throw new RuntimeException("Job Seeker profile already exists");
        }

        return jobSeekerRepository.save(jobSeeker);
    }

    @Override
    public Optional<JobSeekerDTO> getProfile(Long id) {

        return jobSeekerRepository.findById(id)
                .map(JobSeekerMapper::toDTO);
    }

    @Override
    public JobSeekerDTO updateProfile(Long id, JobSeekerDTO dto) {

        JobSeeker existing = jobSeekerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("JobSeeker not found"));

        // 🔥 Update only allowed fields
        existing.setFullName(dto.getFullName());
        existing.setPhone(dto.getPhone());
        existing.setLocation(dto.getLocation());
        existing.setEmploymentStatus(dto.getEmploymentStatus());
        existing.setProfileSummary(dto.getProfileSummary());

        JobSeeker saved = jobSeekerRepository.save(existing);

        return JobSeekerMapper.toDTO(saved);
    }

    @Override
    public Optional<JobSeeker> getByUserId(Long userId) {
        return jobSeekerRepository.findByUserId(userId);
    }

    @Override
    public Optional<JobSeeker> getById(Long id) {
        return jobSeekerRepository.findById(id);
    }
    
    
}

