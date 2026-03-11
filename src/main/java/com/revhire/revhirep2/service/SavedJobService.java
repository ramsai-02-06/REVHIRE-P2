package com.revhire.revhirep2.service;

import com.revhire.revhirep2.entity.SavedJob;

import java.util.List;

public interface SavedJobService {

    SavedJob saveJob(Long jobId, Long jobSeekerId);

    void removeSavedJob(Long jobId, Long jobSeekerId);

    List<SavedJob> getSavedJobs(Long jobSeekerId);
}