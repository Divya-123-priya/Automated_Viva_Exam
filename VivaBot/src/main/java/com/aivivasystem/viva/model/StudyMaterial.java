package com.aivivasystem.viva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "study_material")
public class StudyMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fileName;
    private String filePath;

    @Column(length = 100000)
    private String extractedText;

    @ManyToOne
    @JoinColumn(name = "subject_id", nullable = false)
    private Subject subject;

    public Long getId() { 
    	return id; }

    public String getFileName() { 
    	return fileName; }
    public void setFileName(String f){ 
    	this.fileName = f; }

    public String getFilePath() { 
    	return filePath; }
    public void setFilePath(String f){ 
    	this.filePath = f; }

    public String getExtractedText() { 
    	return extractedText; }
    public void setExtractedText(String text){ 
    	this.extractedText = text; }

    public Subject getSubject() { 
    	return subject; }
    public void setSubject(Subject subject){ 
    	this.subject = subject; }
    
}
