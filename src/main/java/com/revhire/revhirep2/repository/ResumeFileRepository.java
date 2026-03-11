package com.revhire.revhirep2.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import com.revhire.revhirep2.entity.ResumeFile;

public interface ResumeFileRepository extends JpaRepository<ResumeFile, Long> {

	 boolean existsByJobSeekerId(Long jobSeekerId);
	
	 Optional<ResumeFile> findByJobSeekerId(Long jobSeekerId);

	 
	 //ResumeFileDTO updateResume(Long jobSeekerId, MultipartFile file) throws Exception;
	 
	// List<ResumeFile> findByJobSeekerId(Long jobSeekerId);
}