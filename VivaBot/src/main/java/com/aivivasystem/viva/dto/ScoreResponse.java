package com.aivivasystem.viva.dto;

public class ScoreResponse {
    private double correctnessScore;
    private double confidenceScore;
    private double completenessScore;
    private double finalScore;
    private String grade;
    private String feedback;

    public ScoreResponse() {}

    public ScoreResponse(double correctness, double confidence,
                         double completeness, double finalScore,
                         String grade, String feedback) {
        this.correctnessScore  = correctness;
        this.confidenceScore   = confidence;
        this.completenessScore = completeness;
        this.finalScore        = finalScore;
        this.grade             = grade;
        this.feedback          = feedback;
    }

    public double getCorrectnessScore() { 
    	return correctnessScore; }
    public double getConfidenceScore() { 
    	return confidenceScore; }
    public double getCompletenessScore() { 
    	return completenessScore; }
    public double getFinalScore() { 
    	return finalScore; }
    public String getGrade(){ 
    	return grade; }
    public String getFeedback() { 
    	return feedback; }
    
}
