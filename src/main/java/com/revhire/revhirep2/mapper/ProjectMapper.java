//package com.revhire.revhirep2.mapper;
//
//import com.revhire.revhirep2.dto.ProjectDTO;
//import com.revhire.revhirep2.entity.Project;
//import com.revhire.revhirep2.entity.Resume;
//
//public class ProjectMapper {
//
//    public static Project toEntity(ProjectDTO dto, Resume resume) {
//
//        Project project = new Project();
//        project.setId(dto.getId());
//        project.setName(dto.getName());
//        project.setTechnologiesUsed(dto.getTechnologiesUsed());
//        project.setDescription(dto.getDescription());
//        project.setResume(resume);
//
//        return project;
//    }
//
//    public static ProjectDTO toDTO(Project project) {
//
//        ProjectDTO dto = new ProjectDTO();
//        dto.setId(project.getId());
//        dto.setName(project.getName());
//        dto.setTechnologiesUsed(project.getTechnologiesUsed());
//        dto.setDescription(project.getDescription());
//       // dto.setResumeId(project.getResume().getId());
//
//        return dto;
//    }
//}

package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.ProjectDTO;
import com.revhire.revhirep2.entity.Project;

public class ProjectMapper {

    public static Project toEntity(ProjectDTO dto) {

        Project project = new Project();

        // ❌ DO NOT SET ID HERE
        // project.setId(dto.getId());

        project.setName(dto.getName());
        project.setTechnologiesUsed(dto.getTechnologiesUsed());
        project.setDescription(dto.getDescription());

        return project;
    }

    public static ProjectDTO toDTO(Project project) {

        ProjectDTO dto = new ProjectDTO();
        dto.setId(project.getId());
        dto.setName(project.getName());
        dto.setTechnologiesUsed(project.getTechnologiesUsed());
        dto.setDescription(project.getDescription());

        return dto;
    }
}