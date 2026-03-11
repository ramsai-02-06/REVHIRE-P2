package com.revhire.revhirep2.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "projects")
public class Project {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(name = "technologies_used")
    private String technologiesUsed;

    @Column(columnDefinition = "TEXT")
    private String description;

    // 🔥 THIS IS WHERE resume_id IS MAPPED
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id", nullable = false)
    private Resume resume;

    public Project() {}

    public Long getId() {
        return id;
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

    public Resume getResume() {
        return resume;
    }

    public void setId(Long id) {
        this.id = id;
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

    public void setResume(Resume resume) {
        this.resume = resume;
    }
}