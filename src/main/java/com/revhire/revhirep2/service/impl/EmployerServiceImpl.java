package com.revhire.revhirep2.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.EmployerDTO;
import com.revhire.revhirep2.dto.EmployerDashboardDTO;
import com.revhire.revhirep2.entity.Employer;
import com.revhire.revhirep2.exception.BadRequestException;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.mapper.EmployerMapper;
import com.revhire.revhirep2.repository.ApplicationRepository;
import com.revhire.revhirep2.repository.EmployerRepository;
import com.revhire.revhirep2.repository.JobRepository;
import com.revhire.revhirep2.service.EmployerService;

@Service
public class EmployerServiceImpl implements EmployerService {

	private final JobRepository jobRepository;
	private final ApplicationRepository applicationRepository;
	private final EmployerRepository employerRepository;

    
    
	public EmployerServiceImpl(EmployerRepository employerRepository,
            JobRepository jobRepository,
            ApplicationRepository applicationRepository) {

this.employerRepository = employerRepository;
this.jobRepository = jobRepository;
this.applicationRepository = applicationRepository;
}

    
    @Override
    public EmployerDashboardDTO getDashboardByEmail(String email) {

        Employer employer = employerRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        Long employerId = employer.getId();

        long totalJobs =
                jobRepository.countByEmployerId(employerId);

        long activeJobs =
                jobRepository.countByEmployerIdAndStatus(
                        employerId, "OPEN"
                );

        long totalApplications =
                applicationRepository.countByJobEmployerId(employerId);

        long pendingReviews =
                applicationRepository.countByJobEmployerIdAndStatus(
                        employerId, "APPLIED"
                );

        return new EmployerDashboardDTO(
                totalJobs,
                activeJobs,
                totalApplications,
                pendingReviews
        );
    }
    @Override
    public Employer createEmployer(Employer employer) {

        if (employerRepository.existsByUserId(employer.getUser().getId())) {
        	throw new BadRequestException("Employer profile already exists"); }

        return employerRepository.save(employer);
    }
    
    @Override
    public Optional<EmployerDTO> getProfile(Long id) {

        return employerRepository.findById(id)
                .map(EmployerMapper::toDTO);
    }

    @Override
    public Optional<Employer> getByUserEmail(String email) {
        return employerRepository.findByUserEmail(email);
    }
    
    @Override
    public void updateByEmail(String email, EmployerDTO dto) {

        Employer employer = employerRepository
                .findByUserEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("Employer not found"));

        employer.setCompanyName(dto.getCompanyName());
        employer.setDescription(dto.getDescription());
        employer.setLocation(dto.getLocation());
        employer.setWebsite(dto.getWebsite());

        employerRepository.save(employer);
    }
    
    @Override
    public EmployerDTO updateProfile(Long id, EmployerDTO dto) {

        Employer existing = employerRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Employer not found"));

        
        
        // Update only allowed fields
        existing.setCompanyName(dto.getCompanyName());
        existing.setIndustry(dto.getIndustry());
        existing.setCompanySize(dto.getCompanySize());
        existing.setDescription(dto.getDescription());
        existing.setWebsite(dto.getWebsite());
        existing.setLocation(dto.getLocation());

        Employer saved = employerRepository.save(existing);

        return EmployerMapper.toDTO(saved);
    }

//    @Override
//    public Employer updateEmployer(Long id, Employer employer) {
//
//        Employer existing = employerRepository.findById(id)
//        		.orElseThrow(() -> new ResourceNotFoundException("Employer not found"));
//
//        existing.setCompanyName(employer.getCompanyName());
//        existing.setIndustry(employer.getIndustry());
//        existing.setCompanySize(employer.getCompanySize());
//        existing.setDescription(employer.getDescription());
//        existing.setWebsite(employer.getWebsite());
//        existing.setLocation(employer.getLocation());
//
//        return employerRepository.save(existing);
//    }

    @Override
    public Optional<Employer> getByUserId(Long userId) {
        return employerRepository.findByUserId(userId);
    }

    @Override
    public Optional<Employer> getById(Long id) {
        return employerRepository.findById(id);
    }
}