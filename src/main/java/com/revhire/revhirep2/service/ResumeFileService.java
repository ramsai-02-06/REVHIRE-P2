package com.revhire.revhirep2.service;

import org.springframework.web.multipart.MultipartFile;
import com.revhire.revhirep2.dto.ResumeFileDTO;

public interface ResumeFileService {

    ResumeFileDTO uploadFile(Long jobSeekerId, MultipartFile file) throws Exception;

    ResumeFileDTO getByJobSeekerId(Long jobSeekerId);

    void deleteFile(Long fileId);

    ResumeFileDTO getById(Long id);

    // ⭐ NEW METHOD
    ResumeFileDTO updateResume(Long jobSeekerId, MultipartFile file) throws Exception;
}