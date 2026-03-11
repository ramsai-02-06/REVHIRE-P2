package com.revhire.revhirep2.dto;

import jakarta.validation.constraints.NotBlank;

public class SkillDTO {

    private Long id;
    @NotBlank(message = "SkillName required")
    private String skillName;
    @NotBlank(message = "level required")
    private String proficiencyLevel;

    public SkillDTO() {}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getSkillName() {
		return skillName;
	}

	public void setSkillName(String skillName) {
		this.skillName = skillName;
	}

	public String getProficiencyLevel() {
		return proficiencyLevel;
	}

	public void setProficiencyLevel(String proficiencyLevel) {
		this.proficiencyLevel = proficiencyLevel;
	}

    // getters and setters
}