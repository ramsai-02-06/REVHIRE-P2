package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.SavedJobDTO;
import com.revhire.revhirep2.entity.SavedJob;

public class SavedJobMapper {

    public static SavedJobDTO toDTO(SavedJob entity) {
        SavedJobDTO dto = new SavedJobDTO();
        dto.setId(entity.getId());
        dto.setJobId(entity.getJob().getId());
        dto.setJobSeekerId(entity.getJobSeeker().getId());
        return dto;
    }
}