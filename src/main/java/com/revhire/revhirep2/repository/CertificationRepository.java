package com.revhire.revhirep2.repository;

import com.revhire.revhirep2.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CertificationRepository extends JpaRepository<Certification, Long> {

    List<Certification> findByResumeId(Long resumeId);
}