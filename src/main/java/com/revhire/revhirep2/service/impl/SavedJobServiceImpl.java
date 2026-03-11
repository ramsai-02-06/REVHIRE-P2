package com.revhire.revhirep2.service.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.entity.Job;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.SavedJob;
import com.revhire.revhirep2.exception.BadRequestException;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.repository.JobRepository;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.SavedJobRepository;
import com.revhire.revhirep2.service.SavedJobService;

@Service
public class SavedJobServiceImpl implements SavedJobService {

    private final SavedJobRepository savedJobRepository;
    private final JobRepository jobRepository;
    private final JobSeekerRepository jobSeekerRepository;

    public SavedJobServiceImpl(SavedJobRepository savedJobRepository,
                               JobRepository jobRepository,
                               JobSeekerRepository jobSeekerRepository) {
        this.savedJobRepository = savedJobRepository;
        this.jobRepository = jobRepository;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @Override
    public SavedJob saveJob(Long jobId, Long jobSeekerId) {

        if (savedJobRepository.existsByJobIdAndJobSeekerId(jobId, jobSeekerId)) {
            throw new BadRequestException("Job already saved");
        }

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        SavedJob savedJob = new SavedJob();
        savedJob.setJob(job);
        savedJob.setJobSeeker(jobSeeker);
        savedJob.setSavedAt(LocalDateTime.now());

        return savedJobRepository.save(savedJob);
    }

    @Override
    public void removeSavedJob(Long jobId, Long jobSeekerId) {

        SavedJob savedJob = savedJobRepository
                .findByJobIdAndJobSeekerId(jobId, jobSeekerId)
                .orElseThrow(() -> new ResourceNotFoundException("Saved job not found"));

        savedJobRepository.delete(savedJob);
    }

    @Override
    public List<SavedJob> getSavedJobs(Long jobSeekerId) {
        return savedJobRepository.findByJobSeekerId(jobSeekerId);
    }
}