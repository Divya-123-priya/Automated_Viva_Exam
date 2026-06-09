package com.aivivasystem.viva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "viva_answer")
public class VivaAnswer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long questionId;

    private Long sessionId;

    @Column(length = 5000)
    private String answerText;

    private double correctnessScore;
    private double confidenceScore;
    private double completenessScore;
    private double finalScore;

    private String grade;

    @Column(length = 3000)
    private String feedback;

    public Long getId() { 
    	return id; }

    public Long getQuestionId() { 
    	return questionId; }
    public void setQuestionId(Long qId) { 
    	this.questionId = qId; }

    public Long getSessionId() { 
    	return sessionId; }
    public void setSessionId(Long sId) { 
    	this.sessionId = sId; }

    public String getAnswerText() { 
    	return answerText; }
    public void setAnswerText(String t) { 
    	this.answerText = t; }

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

    public double getFinalScore() { 
    	return finalScore; }
    public void setFinalScore(double s) { 
    	this.finalScore = s; }

    public String getGrade() { 
    	return grade; }
    public void setGrade(String g) { 
    	this.grade = g; }

    public String getFeedback() { 
    	return feedback; }
    public void setFeedback(String f) { 
    	this.feedback = f; }

}