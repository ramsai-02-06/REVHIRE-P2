package com.revhire.revhirep2.service.impl;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.entity.Certification;
import com.revhire.revhirep2.entity.Education;
import com.revhire.revhirep2.entity.Experience;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.Project;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.Skill;
import com.revhire.revhirep2.exception.ResourceNotFoundException;
import com.revhire.revhirep2.mapper.ResumeMapper;
import com.revhire.revhirep2.repository.CertificationRepository;
import com.revhire.revhirep2.repository.EducationRepository;
import com.revhire.revhirep2.repository.ExperienceRepository;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.ProjectRepository;
import com.revhire.revhirep2.repository.ResumeRepository;
import com.revhire.revhirep2.repository.SkillRepository;
import com.revhire.revhirep2.service.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {

    private final ResumeRepository resumeRepository;
    private final EducationRepository educationRepository;
    private final ExperienceRepository experienceRepository;
    private final SkillRepository skillRepository;
    private final CertificationRepository certificationRepository;
    private final ProjectRepository projectRepository;
    private final JobSeekerRepository jobSeekerRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository,
                             EducationRepository educationRepository,
                             ExperienceRepository experienceRepository,
                             SkillRepository skillRepository,
                             CertificationRepository certificationRepository,
                             ProjectRepository projectRepository,
                             JobSeekerRepository jobSeekerRepository) {

        this.resumeRepository = resumeRepository;
        this.educationRepository = educationRepository;
        this.experienceRepository = experienceRepository;
        this.skillRepository = skillRepository;
        this.certificationRepository = certificationRepository;
        this.projectRepository = projectRepository;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    // ======================================
    // CREATE RESUME
    // ======================================

    @Override
    public ResumeDTO createResumeFromDTO(Long jobSeekerId, ResumeDTO dto) {

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        Resume resume = new Resume();
        resume.setObjective(dto.getObjective());
        resume.setJobSeeker(jobSeeker);
        resume.setCreatedAt(LocalDateTime.now());
        resume.setUpdatedAt(LocalDateTime.now());

        // EDUCATION
        if (dto.getEducationList() != null) {

            List<Education> educationEntities = dto.getEducationList()
                    .stream()
                    .filter(e -> e.getDegree() != null && !e.getDegree().isBlank())
                    .map(eDto -> {

                        Education e = new Education();
                        e.setDegree(eDto.getDegree());
                        e.setInstitution(eDto.getInstitution());

                        if (eDto.getStartDate() != null && !eDto.getStartDate().isBlank()) {
                            e.setStartDate(LocalDate.parse(eDto.getStartDate()));
                        }

                        if (eDto.getEndDate() != null && !eDto.getEndDate().isBlank()) {
                            e.setEndDate(LocalDate.parse(eDto.getEndDate()));
                        }

                        e.setGrade(eDto.getGrade());
                        e.setResume(resume);

                        return e;
                    })
                    .toList();

            resume.setEducationList(educationEntities);
        }

        // EXPERIENCE
        if (dto.getExperienceList() != null) {

            List<Experience> experienceEntities = dto.getExperienceList()
                    .stream()
                    .filter(e -> e.getCompanyName() != null && !e.getCompanyName().isBlank())
                    .map(eDto -> {

                        Experience e = new Experience();
                        e.setCompanyName(eDto.getCompanyName());
                        e.setJobTitle(eDto.getJobTitle());

                        if (eDto.getStartDate() != null && !eDto.getStartDate().isBlank()) {
                            e.setStartDate(LocalDate.parse(eDto.getStartDate()));
                        }

                        if (eDto.getEndDate() != null && !eDto.getEndDate().isBlank()) {
                            e.setEndDate(LocalDate.parse(eDto.getEndDate()));
                        }

                        e.setDescription(eDto.getDescription());
                        e.setResume(resume);

                        return e;
                    })
                    .toList();

            resume.setExperienceList(experienceEntities);
        }

        // SKILLS
        if (dto.getSkillsList() != null) {

            List<Skill> skillEntities = dto.getSkillsList()
                    .stream()
                    .filter(s -> s.getSkillName() != null && !s.getSkillName().isBlank())
                    .map(sDto -> {

                        Skill s = new Skill();
                        s.setSkillName(sDto.getSkillName());
                        s.setProficiencyLevel(sDto.getProficiencyLevel());
                        s.setResume(resume);

                        return s;
                    })
                    .toList();

            resume.setSkills(skillEntities);
        }

        // CERTIFICATIONS
        if (dto.getCertificationsList() != null) {

            List<Certification> certificationEntities = dto.getCertificationsList()
                    .stream()
                    .filter(c -> c.getName() != null && !c.getName().isBlank())
                    .map(cDto -> {

                        Certification c = new Certification();
                        c.setName(cDto.getName());
                        c.setIssuedBy(cDto.getIssuedBy());

                        if (cDto.getIssueDate() != null && !cDto.getIssueDate().isBlank()) {
                            c.setIssueDate(LocalDate.parse(cDto.getIssueDate()));
                        }

                        c.setResume(resume);

                        return c;
                    })
                    .toList();

            resume.setCertifications(certificationEntities);
        }

        // PROJECTS
        if (dto.getProjectsList() != null) {

            List<Project> projectEntities = dto.getProjectsList()
                    .stream()
                    .filter(p -> p.getName() != null && !p.getName().isBlank())
                    .map(pDto -> {

                        Project p = new Project();
                        p.setName(pDto.getName());
                        p.setTechnologiesUsed(pDto.getTechnologiesUsed());
                        p.setDescription(pDto.getDescription());
                        p.setResume(resume);

                        return p;
                    })
                    .toList();

            resume.setProjects(projectEntities);
        }

        Resume saved = resumeRepository.save(resume);

        return ResumeMapper.toDTO(saved);
    }

    // ======================================
    // UPDATE RESUME
    // ======================================

    @Override
    public Resume updateResume(Long resumeId, Resume updatedResume) {

        Resume existing = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        existing.setObjective(updatedResume.getObjective());
        existing.setUpdatedAt(LocalDateTime.now());

        existing.getEducationList().clear();
        existing.getExperienceList().clear();
        existing.getSkills().clear();
        existing.getCertifications().clear();
        existing.getProjects().clear();

        if (updatedResume.getEducationList() != null) {
            updatedResume.getEducationList().forEach(e -> {

                if (e.getDegree() != null && !e.getDegree().isBlank()) {

                    e.setResume(existing);
                    existing.getEducationList().add(e);

                }
            });
        }

        if (updatedResume.getExperienceList() != null) {
            updatedResume.getExperienceList().forEach(e -> {

                if (e.getCompanyName() != null && !e.getCompanyName().isBlank()) {

                    e.setResume(existing);
                    existing.getExperienceList().add(e);

                }
            });
        }

        if (updatedResume.getSkills() != null) {
            updatedResume.getSkills().forEach(s -> {

                if (s.getSkillName() != null && !s.getSkillName().isBlank()) {

                    s.setResume(existing);
                    existing.getSkills().add(s);

                }
            });
        }

        if (updatedResume.getCertifications() != null) {
            updatedResume.getCertifications().forEach(c -> {

                if (c.getName() != null && !c.getName().isBlank()) {

                    c.setResume(existing);
                    existing.getCertifications().add(c);

                }
            });
        }

        if (updatedResume.getProjects() != null) {
            updatedResume.getProjects().forEach(p -> {

                if (p.getName() != null && !p.getName().isBlank()) {

                    p.setResume(existing);
                    existing.getProjects().add(p);

                }
            });
        }

        return resumeRepository.save(existing);
    }

    @Override
    public ResumeDTO getResumeById(Long id) {

        Resume resume = resumeRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        // Use mapper to convert entity → DTO
        return ResumeMapper.toDTO(resume);
    }
    
    // ======================================
    // GET RESUME BY JOBSEEKER
    // ======================================

    @Override
    public Optional<Resume> getByJobSeekerId(Long jobSeekerId) {
        return resumeRepository.findByJobSeekerId(jobSeekerId);
    }

    @Override
    public Resume updateObjective(Long resumeId, String objective) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        resume.setObjective(objective);
        resume.setUpdatedAt(LocalDateTime.now());

        return resumeRepository.save(resume);
    }

    @Override
    public Education addEducation(Long resumeId, Education education) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        education.setResume(resume);

        return educationRepository.save(education);
    }

    @Override
    public Experience addExperience(Long resumeId, Experience experience) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        experience.setResume(resume);

        return experienceRepository.save(experience);
    }

    @Override
    public Skill addSkill(Long resumeId, Skill skill) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        skill.setResume(resume);

        return skillRepository.save(skill);
    }

    @Override
    public Certification addCertification(Long resumeId,
                                          Certification certification) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new ResourceNotFoundException("Resume not found"));

        certification.setResume(resume);

        return certificationRepository.save(certification);
    }

    @Override
    public List<Education> getEducation(Long resumeId) {
        return educationRepository.findByResumeId(resumeId);
    }

    @Override
    public List<Experience> getExperience(Long resumeId) {
        return experienceRepository.findByResumeId(resumeId);
    }

    @Override
    public List<Skill> getSkills(Long resumeId) {
        return skillRepository.findByResumeId(resumeId);
    }

    @Override
    public void deleteResume(Long resumeId) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        resumeRepository.delete(resume);
    }
    
    @Override
    public List<Certification> getCertifications(Long resumeId) {
        return certificationRepository.findByResumeId(resumeId);
    }
    
    @Override
    public Project addProject(Long resumeId, Project project) {

        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Resume not found"));

        project.setResume(resume);

        return projectRepository.save(project);
    }
    
    @Override
    public List<Project> getProjects(Long resumeId) {

        return projectRepository.findByResumeId(resumeId);
    }
    @Override
    public void deleteProject(Long projectId) {

        if (!projectRepository.existsById(projectId)) {
            throw new ResourceNotFoundException("Project not found");
        }

        projectRepository.deleteById(projectId);
    }
}