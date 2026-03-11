package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.ResumeFileDTO;
import com.revhire.revhirep2.entity.ResumeFile;

public class ResumeFileMapper {

    public static ResumeFileDTO toDTO(ResumeFile file) {

        ResumeFileDTO dto = new ResumeFileDTO();
        dto.setId(file.getId());
        dto.setFileName(file.getFileName());
        dto.setFilePath(file.getFilePath());
        dto.setFileSize(file.getFileSize());
        dto.setUploadedAt(file.getUploadedAt());

        return dto;
    }

    public static ResumeFile toEntity(ResumeFileDTO dto) {

        ResumeFile file = new ResumeFile();
        file.setFileName(dto.getFileName());
        file.setFilePath(dto.getFilePath());
        file.setFileSize(dto.getFileSize());
        file.setUploadedAt(dto.getUploadedAt());

        return file;
    }
}