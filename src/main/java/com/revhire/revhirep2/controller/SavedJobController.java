package com.revhire.revhirep2.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.revhire.revhirep2.entity.SavedJob;
import com.revhire.revhirep2.service.SavedJobService;

@RestController
@RequestMapping("/api/saved-jobs")
public class SavedJobController {

    private final SavedJobService savedJobService;

    public SavedJobController(SavedJobService savedJobService) {
        this.savedJobService = savedJobService;
    }

    // SAVE A JOB
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @PostMapping
    public ResponseEntity<SavedJob> saveJob(
            @RequestParam Long jobId,
            @RequestParam Long jobSeekerId) {

        return ResponseEntity.ok(
                savedJobService.saveJob(jobId, jobSeekerId)
        );
    }

    // REMOVE SAVED JOB
    @PreAuthorize("hasRole('JOB_SEEKER')")
    @DeleteMapping
    public ResponseEntity<String> removeSavedJob(
            @RequestParam Long jobId,
            @RequestParam Long jobSeekerId) {

        savedJobService.removeSavedJob(jobId, jobSeekerId);
        return ResponseEntity.ok("Saved job removed successfully");
    }

    // GET ALL SAVED JOBS FOR JOB SEEKER
    @GetMapping("/{jobSeekerId}")
    public ResponseEntity<List<SavedJob>> getSavedJobs(
            @PathVariable Long jobSeekerId) {

        return ResponseEntity.ok(
                savedJobService.getSavedJobs(jobSeekerId)
        );
    }
}