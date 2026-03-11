//package com.revhire.revhirep2.controller;
//
//import java.util.List;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.revhire.revhirep2.entity.Certification;
//import com.revhire.revhirep2.entity.Education;
//import com.revhire.revhirep2.entity.Experience;
//import com.revhire.revhirep2.entity.Resume;
//import com.revhire.revhirep2.entity.Skill;
//import com.revhire.revhirep2.service.ResumeService;
//
//@RestController
//@RequestMapping("/api/resumes")
//public class ResumeController {
//
//    private final ResumeService resumeService;
//
//    public ResumeController(ResumeService resumeService) {
//        this.resumeService = resumeService;
//    }
//
//    // CREATE RESUME
//    @PreAuthorize("hasRole('JOB_SEEKER')")
//    @PostMapping
//    public ResponseEntity<Resume> createResume(
//            @RequestBody Resume resume) {
//
//        return ResponseEntity.ok(
//                resumeService.createResume(resume)
//        );
//    }
//
//    // UPDATE OBJECTIVE
//    @PutMapping("/{resumeId}/objective")
//    public ResponseEntity<Resume> updateObjective(
//            @PathVariable Long resumeId,
//            @RequestParam String objective) {
//
//        return ResponseEntity.ok(
//                resumeService.updateObjective(resumeId, objective)
//        );
//    }
//
//    // ADD EDUCATION
//    @PostMapping("/{resumeId}/education")
//    public ResponseEntity<Education> addEducation(
//            @PathVariable Long resumeId,
//            @RequestBody Education education) {
//
//        return ResponseEntity.ok(
//                resumeService.addEducation(resumeId, education)
//        );
//    }
//
//    // ADD EXPERIENCE
//    @PostMapping("/{resumeId}/experience")
//    public ResponseEntity<Experience> addExperience(
//            @PathVariable Long resumeId,
//            @RequestBody Experience experience) {
//
//        return ResponseEntity.ok(
//                resumeService.addExperience(resumeId, experience)
//        );
//    }
//
//    // ADD SKILL
//    @PostMapping("/{resumeId}/skills")
//    public ResponseEntity<Skill> addSkill(
//            @PathVariable Long resumeId,
//            @RequestBody Skill skill) {
//
//        return ResponseEntity.ok(
//                resumeService.addSkill(resumeId, skill)
//        );
//    }
//
//    // ADD CERTIFICATION
//    @PostMapping("/{resumeId}/certifications")
//    public ResponseEntity<Certification> addCertification(
//            @PathVariable Long resumeId,
//            @RequestBody Certification certification) {
//
//        return ResponseEntity.ok(
//                resumeService.addCertification(resumeId, certification)
//        );
//    }
//
//    // GET FULL RESUME
//    @GetMapping("/jobseeker/{jobSeekerId}")
//    public ResponseEntity<Resume> getByJobSeeker(
//            @PathVariable Long jobSeekerId) {
//
//        return ResponseEntity.ok(
//                resumeService.getByJobSeekerId(jobSeekerId)
//                        .orElseThrow(() -> new RuntimeException("Resume not found"))
//        );
//    }
//
//    @GetMapping("/{resumeId}/education")
//    public ResponseEntity<List<Education>> getEducation(
//            @PathVariable Long resumeId) {
//
//        return ResponseEntity.ok(
//                resumeService.getEducation(resumeId)
//        );
//    }
//
//    @GetMapping("/{resumeId}/experience")
//    public ResponseEntity<List<Experience>> getExperience(
//            @PathVariable Long resumeId) {
//
//        return ResponseEntity.ok(
//                resumeService.getExperience(resumeId)
//        );
//    }
//
//    @GetMapping("/{resumeId}/skills")
//    public ResponseEntity<List<Skill>> getSkills(
//            @PathVariable Long resumeId) {
//
//        return ResponseEntity.ok(
//                resumeService.getSkills(resumeId)
//        );
//    }
//
//    @GetMapping("/{resumeId}/certifications")
//    public ResponseEntity<List<Certification>> getCertifications(
//            @PathVariable Long resumeId) {
//
//        return ResponseEntity.ok(
//                resumeService.getCertifications(resumeId)
//        );
//    }
//}

package com.revhire.revhirep2.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.revhire.revhirep2.dto.CertificationDTO;
import com.revhire.revhirep2.dto.EducationDTO;
import com.revhire.revhirep2.dto.ExperienceDTO;
import com.revhire.revhirep2.dto.ProjectDTO;
import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.dto.SkillDTO;
import com.revhire.revhirep2.entity.Certification;
import com.revhire.revhirep2.entity.Education;
import com.revhire.revhirep2.entity.Experience;
import com.revhire.revhirep2.entity.Project;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.Skill;
import com.revhire.revhirep2.mapper.CertificationMapper;
import com.revhire.revhirep2.mapper.EducationMapper;
import com.revhire.revhirep2.mapper.ExperienceMapper;
import com.revhire.revhirep2.mapper.ProjectMapper;
import com.revhire.revhirep2.mapper.ResumeMapper;
import com.revhire.revhirep2.mapper.SkillMapper;
import com.revhire.revhirep2.service.JobSeekerService;
import com.revhire.revhirep2.service.ResumeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;
    public ResumeController(ResumeService resumeService,
                            JobSeekerService jobSeekerService) {
        this.resumeService = resumeService;
    }

    // CREATE RESUME
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{jobSeekerId}")
    public ResponseEntity<ResumeDTO> createResume(
            @PathVariable Long jobSeekerId,
            @Valid @RequestBody ResumeDTO dto) {

        ResumeDTO saved = resumeService.createResumeFromDTO(jobSeekerId, dto);

        return ResponseEntity.ok(saved);
    }

    // UPDATE OBJECTIVE
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PutMapping("/{resumeId}/objective")
    public ResponseEntity<ResumeDTO> updateObjective(
            @PathVariable Long resumeId,
            @Valid @RequestBody String objective) {

        Resume updated = resumeService.updateObjective(resumeId, objective);

        return ResponseEntity.ok(ResumeMapper.toDTO(updated));
    }

    // GET FULL RESUME
    @PreAuthorize("hasAnyRole('JOB_SEEKER','EMPLOYER')")
    @GetMapping("/jobseeker/{jobSeekerId}")
    public ResponseEntity<ResumeDTO> getByJobSeeker(
            @PathVariable Long jobSeekerId) {

        Resume resume = resumeService.getByJobSeekerId(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return ResponseEntity.ok(ResumeMapper.toDTO(resume));
    }

    // ADD EDUCATION
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/education")
    public ResponseEntity<EducationDTO> addEducation(
            @PathVariable Long resumeId,
            @Valid @RequestBody EducationDTO dto) {

        Education education = EducationMapper.toEntity(dto);

        Education saved = resumeService.addEducation(resumeId, education);

        return ResponseEntity.ok(
                EducationMapper.toDTO(saved)
        );
    }

    // ADD EXPERIENCE
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/experience")
    public ResponseEntity<ExperienceDTO> addExperience(
            @PathVariable Long resumeId,
            @Valid @RequestBody ExperienceDTO dto) {

        Experience experience = ExperienceMapper.toEntity(dto);

        Experience saved = resumeService.addExperience(resumeId, experience);

        return ResponseEntity.ok(
                ExperienceMapper.toDTO(saved)
        );
    }

    // ADD SKILL
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/skills")
    public ResponseEntity<SkillDTO> addSkill(
            @PathVariable Long resumeId,
            @Valid @RequestBody SkillDTO dto) {

        Skill skill = SkillMapper.toEntity(dto);

        Skill saved = resumeService.addSkill(resumeId, skill);

        return ResponseEntity.ok(
                SkillMapper.toDTO(saved)
        );
    }
    
    

    // ADD CERTIFICATION
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/certifications")
    public ResponseEntity<CertificationDTO> addCertification(
            @PathVariable Long resumeId,
            @Valid @RequestBody CertificationDTO dto) {

        Certification certification =
                CertificationMapper.toEntity(dto);

        Certification saved =
                resumeService.addCertification(resumeId, certification);

        return ResponseEntity.ok(
                CertificationMapper.toDTO(saved)
        );
    }
    
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping("/{resumeId}/projects")
    public ResponseEntity<ProjectDTO> addProject(
            @PathVariable Long resumeId,
            @Valid @RequestBody ProjectDTO dto) {

        Project project = ProjectMapper.toEntity(dto);

        Project saved = resumeService.addProject(resumeId, project);

        return ResponseEntity.ok(
                ProjectMapper.toDTO(saved)
        );
    }
   
    
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PutMapping("/{resumeId}")
    public ResponseEntity<ResumeDTO> updateResume(
            @PathVariable Long resumeId,
            @RequestBody ResumeDTO dto) {

        Resume resume = ResumeMapper.toEntity(dto);

        Resume updated = resumeService.updateResume(resumeId, resume);

        return ResponseEntity.ok(
                ResumeMapper.toDTO(updated)
        );
    }
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @DeleteMapping("/{resumeId}")
    public ResponseEntity<String> deleteResume(
            @PathVariable Long resumeId) {

        resumeService.deleteResume(resumeId);

        return ResponseEntity.ok("Resume deleted successfully");
    }
}