package com.revhire.revhirep2.service.impl;

import java.io.File;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.revhire.revhirep2.dto.ResumeFileDTO;
import com.revhire.revhirep2.entity.JobSeeker;
import com.revhire.revhirep2.entity.ResumeFile;
import com.revhire.revhirep2.mapper.ResumeFileMapper;
import com.revhire.revhirep2.repository.JobSeekerRepository;
import com.revhire.revhirep2.repository.ResumeFileRepository;
import com.revhire.revhirep2.service.ResumeFileService;

@Service
public class ResumeFileServiceImpl implements ResumeFileService {

    private final ResumeFileRepository resumeFileRepository;
    private final JobSeekerRepository jobSeekerRepository;

    public ResumeFileServiceImpl(ResumeFileRepository resumeFileRepository,
                                 JobSeekerRepository jobSeekerRepository) {
        this.resumeFileRepository = resumeFileRepository;
        this.jobSeekerRepository = jobSeekerRepository;
    }

    @Override
    public ResumeFileDTO uploadFile(Long jobSeekerId, MultipartFile file) throws Exception {

        JobSeeker jobSeeker = jobSeekerRepository.findById(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("JobSeeker not found"));

        String uploadDir = System.getProperty("user.dir") + File.separator + "uploads";

        File directory = new File(uploadDir);
        if (!directory.exists()) directory.mkdirs();

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + File.separator + fileName;

        file.transferTo(new File(filePath));

        ResumeFile resumeFile = resumeFileRepository
                .findByJobSeekerId(jobSeekerId)
                .orElse(new ResumeFile());

        resumeFile.setJobSeeker(jobSeeker);
        resumeFile.setFileName(fileName);
        resumeFile.setFilePath(filePath);
        resumeFile.setFileSize(file.getSize());
        resumeFile.setUploadedAt(LocalDateTime.now());

        ResumeFile saved = resumeFileRepository.save(resumeFile);

        return ResumeFileMapper.toDTO(saved);
    }

    @Override
    public ResumeFileDTO getByJobSeekerId(Long jobSeekerId) {

        ResumeFile resume = resumeFileRepository
                .findByJobSeekerId(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return ResumeFileMapper.toDTO(resume);
    }

    @Override
    public ResumeFileDTO getById(Long id) {

        ResumeFile file = resumeFileRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        return ResumeFileMapper.toDTO(file);
    }
    
       
    @Override
    public ResumeFileDTO updateResume(Long jobSeekerId, MultipartFile file) throws Exception {

        ResumeFile resume = resumeFileRepository
                .findByJobSeekerId(jobSeekerId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        String uploadDir = System.getProperty("user.dir") + "/uploads";

        File dir = new File(uploadDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        String filePath = uploadDir + "/" + fileName;

        file.transferTo(new File(filePath));

        resume.setFileName(fileName);
        resume.setFilePath(filePath);
        resume.setFileSize(file.getSize());

        // 🔥 FIX
        resume.setUploadedAt(LocalDateTime.now());

        ResumeFile saved = resumeFileRepository.save(resume);

        ResumeFileDTO dto = new ResumeFileDTO();
        dto.setId(saved.getId());
        dto.setFileName(saved.getFileName());
        dto.setFilePath(saved.getFilePath());
        dto.setFileSize(saved.getFileSize());
        dto.setUploadedAt(saved.getUploadedAt());

        return dto;
    }
    
    @Override
    public void deleteFile(Long fileId) {

        if (!resumeFileRepository.existsById(fileId)) {
            throw new RuntimeException("Resume file not found");
        }

        resumeFileRepository.deleteById(fileId);
    }
}