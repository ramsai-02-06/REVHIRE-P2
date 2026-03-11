package com.revhire.revhirep2.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.revhire.revhirep2.dto.ResumeFileDTO;
import com.revhire.revhirep2.service.ResumeFileService;
@Controller
@RequestMapping("/api/resume-files")
public class ResumeFileController {

    private final ResumeFileService resumeFileService;

    public ResumeFileController(ResumeFileService resumeFileService) {
        this.resumeFileService = resumeFileService;
    }

    // =========================================
    // 1️⃣ Upload Resume (Create or Update)
    // =========================================

    @PostMapping("/{jobSeekerId}/upload")
    public ResponseEntity<ResumeFileDTO> uploadFile(
            @PathVariable Long jobSeekerId,
            @RequestParam("file") MultipartFile file) throws Exception {

        if (file.isEmpty()) {
            throw new RuntimeException("File is empty");
        }

        String fileName = file.getOriginalFilename();

        if (fileName == null ||
                !(fileName.toLowerCase().endsWith(".pdf")
                        || fileName.toLowerCase().endsWith(".docx"))) {
            throw new RuntimeException("Only PDF or DOCX files allowed");
        }

        if (file.getSize() > 2 * 1024 * 1024) {
            throw new RuntimeException("File size must be less than 2MB");
        }

        ResumeFileDTO saved =
                resumeFileService.uploadFile(jobSeekerId, file);

        return ResponseEntity.ok(saved);
    }

    // =========================================
    // 2️⃣ Get Resume Metadata
    // =========================================

    @GetMapping("/{jobSeekerId}")
    public ResponseEntity<ResumeFileDTO> getResume(
            @PathVariable Long jobSeekerId) {

        return ResponseEntity.ok(
                resumeFileService.getByJobSeekerId(jobSeekerId)
        );
    }

    // =========================================
    // 3️⃣ View Resume File (OPEN PDF/DOCX)
    // =========================================

    @GetMapping("/view/{fileId}")
    public ResponseEntity<Resource> viewResume(@PathVariable Long fileId) throws Exception {

        ResumeFileDTO file = resumeFileService.getById(fileId);

        Path path = Paths.get(file.getFilePath());

        if (!Files.exists(path)) {
            throw new RuntimeException("Resume not found");
        }

        Resource resource = new UrlResource(path.toUri());

        return ResponseEntity.ok()
                .contentType(org.springframework.http.MediaType.APPLICATION_PDF)
                .header(org.springframework.http.HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }
    
    @PostMapping("/update/{jobSeekerId}")
    public String updateResume(
            @PathVariable Long jobSeekerId,
            @RequestParam("file") MultipartFile file) throws Exception {

        resumeFileService.updateResume(jobSeekerId, file);

        return "redirect:/jobseeker/dashboard";
    }
    
    // =========================================
    // 4️⃣ Delete Resume
    // =========================================

    @DeleteMapping("/{fileId}")
    public ResponseEntity<String> deleteFile(
            @PathVariable Long fileId) {

        resumeFileService.deleteFile(fileId);

        return ResponseEntity.ok("Resume file deleted successfully");
    }

}