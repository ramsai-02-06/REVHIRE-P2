//package com.revhire.revhirep2.controller;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.PutMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.revhire.revhirep2.dto.ApplicationResponseDTO;
//import com.revhire.revhirep2.mapper.ApplicationMapper;
//import com.revhire.revhirep2.service.ApplicationService;
//
//@RestController
//@RequestMapping("/api/applications")
//public class ApplicationController {
//
//    private final ApplicationService applicationService;
//
//    public ApplicationController(ApplicationService applicationService) {
//        this.applicationService = applicationService;
//    }
//
//    // APPLY TO JOB
//    @PreAuthorize("hasRole('JOB_SEEKER')")
//    @PostMapping
//    public ResponseEntity<ApplicationResponseDTO> applyToJob(
//            @RequestParam Long jobId,
//            @RequestParam Long jobSeekerId,
//            @RequestParam Long resumeId,
//            @RequestParam(required = false) String coverLetter) {
//
//        return ResponseEntity.ok(
//                ApplicationMapper.toDTO(
//                        applicationService.applyToJob(jobId, jobSeekerId, resumeId, coverLetter)
//                )
//        );
//    }
//
//    // WITHDRAW APPLICATION
//    @PutMapping("/{applicationId}/withdraw")
//    public ResponseEntity<ApplicationResponseDTO> withdrawApplication(
//            @PathVariable Long applicationId) {
//
//        return ResponseEntity.ok(
//                ApplicationMapper.toDTO(
//                        applicationService.withdrawApplication(applicationId)
//                )
//        );
//    }
//
//    // UPDATE STATUS
//    @PreAuthorize("hasRole('EMPLOYER')")
//    @PutMapping("/{applicationId}/status")
//    public ResponseEntity<ApplicationResponseDTO> updateStatus(
//            @PathVariable Long applicationId,
//            @RequestParam String status) {
//
//        return ResponseEntity.ok(
//                ApplicationMapper.toDTO(
//                        applicationService.updateStatus(applicationId, status)
//                )
//        );
//    }
//
//    // GET APPLICATIONS BY JOB
//    @GetMapping("/job/{jobId}")
//    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByJob(
//            @PathVariable Long jobId) {
//
//        return ResponseEntity.ok(
//                applicationService.getApplicationsByJob(jobId)
//                        .stream()
//                        .map(ApplicationMapper::toDTO)
//                        .collect(Collectors.toList())
//        );
//    }
//
//    // GET APPLICATIONS BY JOB SEEKER
//    @GetMapping("/jobseeker/{jobSeekerId}")
//    public ResponseEntity<List<ApplicationResponseDTO>> getApplicationsByJobSeeker(
//            @PathVariable Long jobSeekerId) {
//
//        return ResponseEntity.ok(
//                applicationService.getApplicationsByJobSeeker(jobSeekerId)
//                        .stream()
//                        .map(ApplicationMapper::toDTO)
//                        .collect(Collectors.toList())
//        );
//    }
//}


package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revhire.revhirep2.dto.ApplicationCreateDTO;
import com.revhire.revhirep2.dto.ApplicationResponseDTO;
import com.revhire.revhirep2.dto.BulkStatusUpdateDTO;
import com.revhire.revhirep2.dto.EmployerApplicationViewDTO;
import com.revhire.revhirep2.service.ApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/applications")
public class ApplicationController {

    private final ApplicationService service;

    public ApplicationController(ApplicationService service) {
        this.service = service;
    }

    // APPLY
    @PostMapping
    public ResponseEntity<?> apply(@Valid @RequestBody ApplicationCreateDTO dto) {
        service.apply(dto);
        return ResponseEntity.ok("Applied successfully");
    }

    // WITHDRAW
    @PutMapping("/{id}/withdraw/{jobSeekerId}")
    public ResponseEntity<?> withdraw(@PathVariable Long id,
                                      @PathVariable Long jobSeekerId) {
        service.withdraw(id, jobSeekerId);
        return ResponseEntity.ok("Withdrawn successfully");
    }

    // MY APPLICATIONS
    @GetMapping("/jobseeker/{jobSeekerId}")
    public ResponseEntity<List<ApplicationResponseDTO>> myApps(
            @PathVariable Long jobSeekerId) {

        return ResponseEntity.ok(
                service.getMyApplications(jobSeekerId));
    }

    @PutMapping("/{id}/status/{employerId}")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long id,
            @PathVariable Long employerId,
            @RequestParam String status) {

        service.updateStatus(id, employerId, status);
        return ResponseEntity.ok("Status updated successfully");
    }
    
    // EMPLOYER VIEW
    @GetMapping("/job/{jobId}/employer/{employerId}")
    public ResponseEntity<List<EmployerApplicationViewDTO>> viewApplicants(
            @PathVariable Long jobId,
            @PathVariable Long employerId) {

        return ResponseEntity.ok(
                service.getApplicantsForJob(jobId, employerId));
    }
    
    @PutMapping("/bulk-status")
    @PreAuthorize("hasRole('EMPLOYER')")
    public ResponseEntity<String> bulkUpdateStatus(
            @RequestBody BulkStatusUpdateDTO dto,
            Authentication authentication) {

        String employerEmail = authentication.getName();

        service.bulkUpdateStatus(
                employerEmail,
                dto.getApplicationIds(),
                dto.getStatus()
        );

        return ResponseEntity.ok("Bulk status updated successfully");
    }
}