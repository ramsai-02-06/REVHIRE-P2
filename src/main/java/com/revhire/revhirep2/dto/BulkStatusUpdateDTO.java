package com.revhire.revhirep2.dto;

import java.util.List;

public class BulkStatusUpdateDTO {

    private List<Long> applicationIds;
    private String status;

    public List<Long> getApplicationIds() {
        return applicationIds;
    }

    public void setApplicationIds(List<Long> applicationIds) {
        this.applicationIds = applicationIds;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}