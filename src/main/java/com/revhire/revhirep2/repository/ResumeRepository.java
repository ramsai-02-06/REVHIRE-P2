package com.revhire.revhirep2.repository;

import com.revhire.revhirep2.entity.Resume;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ResumeRepository extends JpaRepository<Resume, Long> {

    Optional<Resume> findByJobSeekerId(Long jobSeekerId);

    boolean existsByJobSeekerId(Long jobSeekerId);
}