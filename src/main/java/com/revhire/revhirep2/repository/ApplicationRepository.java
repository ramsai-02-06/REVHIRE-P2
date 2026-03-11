package com.revhire.revhirep2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revhire.revhirep2.entity.Application;

import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

    boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    List<Application> findByJobSeekerId(Long jobSeekerId);

    List<Application> findByJobId(Long jobId);

    long countByJobEmployerId(Long employerId);

    long countByJobEmployerIdAndStatus(Long employerId, String status);
    
    Optional<Application> findByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);
    
   
    
   
    Optional<Application> findByIdAndJobSeekerId(Long id, Long jobSeekerId);
}