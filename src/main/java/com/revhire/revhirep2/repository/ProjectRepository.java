package com.revhire.revhirep2.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.revhire.revhirep2.entity.Project;

public interface ProjectRepository extends JpaRepository<Project, Long> {

    List<Project> findByResumeId(Long resumeId);
}