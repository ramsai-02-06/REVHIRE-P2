package com.revhire.revhirep2.mapper;

import com.revhire.revhirep2.dto.SkillDTO;
import com.revhire.revhirep2.entity.Skill;

public class SkillMapper {

    public static SkillDTO toDTO(Skill skill) {

        SkillDTO dto = new SkillDTO();

        dto.setId(skill.getId());
        dto.setSkillName(skill.getSkillName());
        dto.setProficiencyLevel(skill.getProficiencyLevel());

        return dto;
    }
    public static Skill toEntity(SkillDTO dto) {

        Skill skill = new Skill();

        skill.setSkillName(dto.getSkillName());
        skill.setProficiencyLevel(dto.getProficiencyLevel());

        return skill;
    }
}