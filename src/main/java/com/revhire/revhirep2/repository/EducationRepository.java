package com.revhire.revhirep2.repository;

import com.revhire.revhirep2.entity.Education;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EducationRepository extends JpaRepository<Education, Long> {

    List<Education> findByResumeId(Long resumeId);
}