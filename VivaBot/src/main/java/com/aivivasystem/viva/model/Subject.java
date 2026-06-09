package com.aivivasystem.viva.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "subject")
public class Subject {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Subject name is required")
    @Column(name = "subject_name", nullable = false)
    private String subjectName;

    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "exam_difficulty", nullable = false)
    private String examDifficulty = "MEDIUM";

    @Column(name = "exam_total_questions", nullable = false)
    private int examTotalQuestions = 5;

    public Subject() {}

    public Subject(String subjectName, String description) {
        this.subjectName = subjectName;
        this.description = description;
    }

    public Long getId() { 
    	return id; }
    public void setId(Long id) { 
    	this.id = id; }

    public String getSubjectName() { 
    	return subjectName; }
    public void setSubjectName(String s) { 
    	this.subjectName = s; }

    public String getDescription() {
    	return description; }
    public void setDescription(String d) { 
    	this.description = d; }

    public String getExamDifficulty() { 
    	return examDifficulty; }
    public void setExamDifficulty(String d) { 
    	this.examDifficulty = d; }

    public int getExamTotalQuestions() { 
    	return examTotalQuestions; }
    public void setExamTotalQuestions(int n) { 
    	this.examTotalQuestions = n; }
    
}