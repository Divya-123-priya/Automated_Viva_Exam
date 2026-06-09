package com.aivivasystem.viva.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viva_result")
public class VivaResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentEmail;
    private String studentName;
    private String registerNumber;  
    private String subject;         
    private Long   sessionId;

    private double totalScore;
    private double correctnessScore;
    private double confidenceScore;
    private double completenessScore;
    private String grade;

    @Column(columnDefinition = "TEXT")
    private String resultFeedback;

    private LocalDateTime createdAt;

    @PrePersist
    public void prePersist() { 
    	this.createdAt = LocalDateTime.now(); }

    public Long getId() { 
    	return id; }

    public String getStudentEmail() { 
    	return studentEmail; }
    public void setStudentEmail(String e) { 
    	this.studentEmail = e; }

    public String getStudentName() { 
    	return studentName; }
    public void setStudentName(String n) { 
    	this.studentName = n; }

    public String getRegisterNumber() { 
    	return registerNumber; }
    public void setRegisterNumber(String r) { 
    	this.registerNumber = r; }

    public String getSubject() { 
    	return subject; }
    public void setSubject(String s) { 
    	this.subject = s; }

    public Long getSessionId() { 
    	return sessionId; }
    public void setSessionId(Long s) { 
    	this.sessionId = s; }

    public double getTotalScore() { 
    	return totalScore; }
    public void setTotalScore(double s) { 
    	this.totalScore = s; }

    public double getCorrectnessScore() { 
    	return correctnessScore; }
    public void setCorrectnessScore(double s) { 
    	this.correctnessScore = s; }

    public double getConfidenceScore() { 
    	return confidenceScore; }
    public void setConfidenceScore(double s) { 
    	this.confidenceScore = s; }

    public double getCompletenessScore() { 
    	return completenessScore; }
    public void setCompletenessScore(double s){ 
    	this.completenessScore = s; }

    public String getGrade() { 
    	return grade; }
    public void setGrade(String g) { 
    	this.grade = g; }

    public String getResultFeedback() { 
    	return resultFeedback; }
    public void setResultFeedback(String f) { 
    	this.resultFeedback = f; }

    public LocalDateTime getCreatedAt() { 
    	return createdAt; }

}