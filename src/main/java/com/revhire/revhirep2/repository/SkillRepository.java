package com.revhire.revhirep2.repository;

import com.revhire.revhirep2.entity.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillRepository extends JpaRepository<Skill, Long> {

    List<Skill> findByResumeId(Long resumeId);
}