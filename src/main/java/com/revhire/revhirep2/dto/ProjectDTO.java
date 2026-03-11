package com.revhire.revhirep2.dto;

public class ProjectDTO {
	private long id;
    private String name;
    private String technologiesUsed;
    private String description;

    public ProjectDTO() {}

    public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
        return name;
    }

    public String getTechnologiesUsed() {
        return technologiesUsed;
    }

    public String getDescription() {
        return description;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTechnologiesUsed(String technologiesUsed) {
        this.technologiesUsed = technologiesUsed;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}