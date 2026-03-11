//package com.revhire.revhirep2.service;
//
//import com.revhire.revhirep2.entity.*;
//
//import java.util.List;
//import java.util.Optional;
//
//public interface ResumeService {
//
//    Resume createResume(Resume resume);
//    
//    Resume updateResume(Long id, Resume resume);
//
//    Optional<Resume> getByJobSeekerId(Long jobSeekerId);
//
//    Resume updateObjective(Long resumeId, String objective);
//
//    Education addEducation(Long resumeId, Education education);
//
//    Experience addExperience(Long resumeId, Experience experience);
//
//    Skill addSkill(Long resumeId, Skill skill);
//
//    Certification addCertification(Long resumeId, Certification certification);
//
//    List<Education> getEducation(Long resumeId);
//
//    List<Experience> getExperience(Long resumeId);
//
//    List<Skill> getSkills(Long resumeId);
//
//    void deleteResume(Long resumeId);
//    
//    List<Certification> getCertifications(Long resumeId);
//
//    Project addProject(Long resumeId, Project project);
//
//    List<Project> getProjects(Long resumeId);
//
//    void deleteProject(Long projectId);
//    
//    
//}

package com.revhire.revhirep2.service;

import java.util.List;
import java.util.Optional;

import com.revhire.revhirep2.dto.ResumeDTO;
import com.revhire.revhirep2.entity.Certification;
import com.revhire.revhirep2.entity.Education;
import com.revhire.revhirep2.entity.Experience;
import com.revhire.revhirep2.entity.Project;
import com.revhire.revhirep2.entity.Resume;
import com.revhire.revhirep2.entity.Skill;

public interface ResumeService {



    Resume updateResume(Long id, Resume resume);

    Optional<Resume> getByJobSeekerId(Long jobSeekerId);

    Resume updateObjective(Long resumeId, String objective);

    Education addEducation(Long resumeId, Education education);

    Experience addExperience(Long resumeId, Experience experience);

    Skill addSkill(Long resumeId, Skill skill);

    Certification addCertification(Long resumeId, Certification certification);

    List<Education> getEducation(Long resumeId);

    List<Experience> getExperience(Long resumeId);

    List<Skill> getSkills(Long resumeId);

    List<Certification> getCertifications(Long resumeId);

    Project addProject(Long resumeId, Project project);

    List<Project> getProjects(Long resumeId);

    void deleteProject(Long projectId);

    void deleteResume(Long resumeId);
    
    ResumeDTO getResumeById(Long id);
   
    ResumeDTO createResumeFromDTO(Long jobSeekerId, ResumeDTO dto);
}
