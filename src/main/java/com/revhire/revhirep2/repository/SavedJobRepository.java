package com.revhire.revhirep2.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.revhire.revhirep2.entity.SavedJob;

public interface SavedJobRepository extends JpaRepository<SavedJob, Long> {

    List<SavedJob> findByJobSeekerId(Long jobSeekerId);

    Optional<SavedJob> findByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);

    boolean existsByJobIdAndJobSeekerId(Long jobId, Long jobSeekerId);
}