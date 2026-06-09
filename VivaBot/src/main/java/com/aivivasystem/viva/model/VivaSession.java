package com.aivivasystem.viva.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "viva_session")   
public class VivaSession {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long studentId;

    @Column(nullable = false)
    private String subject;

    private int totalQuestions;
    private int currentQuestion;
    private boolean completed;

    
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;

    public Long getId() { 
    	return id; }
    public void setId(Long id){ 
    	this.id = id; }

    public Long getStudentId() { 
    	return studentId; }
    public void setStudentId(Long sid) { 
    	this.studentId = sid; }

    public String getSubject() { 
    	return subject; }
    public void setSubject(String s) { 
    	this.subject = s; }

    public int getTotalQuestions() { 
    	return totalQuestions; }
    public void setTotalQuestions(int n){ 
    	this.totalQuestions = n; }

    public int getCurrentQuestion() { 
    	return currentQuestion; }
    public void setCurrentQuestion(int n){ 
    	this.currentQuestion = n; }

    public boolean isCompleted() { 
    	return completed; }
    public void setCompleted(boolean c){ 
    	this.completed = c; }

    public LocalDateTime getStartedAt() { 
    	return startedAt; }
    public void setStartedAt(LocalDateTime t){ 
    	this.startedAt = t; }

    public LocalDateTime getCompletedAt() { 
    	return completedAt; }
    public void setCompletedAt(LocalDateTime t){ 
    	this.completedAt = t; }

}
