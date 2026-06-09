package com.aivivasystem.viva.model;

import jakarta.persistence.*;

@Entity
@Table(name = "viva_question")   
public class VivaQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String subject;
    private String difficulty;

    @Column(length = 2000)
    private String questionText;

    
    @Column(length = 5000)
    private String modelAnswer;

    public Long getId() { 
    	return id; }

    public String getSubject() { 
    	return subject; }
    public void setSubject(String s) { 
    	this.subject = s; }

    public String getDifficulty() { 
    	return difficulty; }
    public void setDifficulty(String d) { 
    	this.difficulty = d; }

    public String getQuestionText() { 
    	return questionText; }
    public void setQuestionText(String text) { 
    	this.questionText = text; }

    public String getModelAnswer() { 
    	return modelAnswer; }
    public void setModelAnswer(String ans) { 
    	this.modelAnswer = ans; }
    
}

