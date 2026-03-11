package com.revhire.revhirep2.service;

import java.util.Optional;

import com.revhire.revhirep2.dto.EmployerDTO;
import com.revhire.revhirep2.dto.EmployerDashboardDTO;
import com.revhire.revhirep2.entity.Employer;

public interface EmployerService {

    Employer createEmployer(Employer employer);

//    Employer updateEmployer(Long id, Employer employer);

    Optional<Employer> getByUserId(Long userId);

    Optional<Employer> getById(Long id);
    
    Optional<EmployerDTO> getProfile(Long id);

    Optional<Employer> getByUserEmail(String email);
    
    void updateByEmail(String email, EmployerDTO dto);
    
    EmployerDashboardDTO getDashboardByEmail(String email);
    
    EmployerDTO updateProfile(Long id, EmployerDTO dto);
}