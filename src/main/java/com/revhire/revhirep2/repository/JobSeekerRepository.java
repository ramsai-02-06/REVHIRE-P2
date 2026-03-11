package com.revhire.revhirep2.repository;

import com.revhire.revhirep2.entity.JobSeeker;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JobSeekerRepository extends JpaRepository<JobSeeker, Long> {

    Optional<JobSeeker> findByUserId(Long userId);

    boolean existsByUserId(Long userId);
}