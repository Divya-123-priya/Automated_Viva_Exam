package com.aivivasystem.viva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class ScoreService {

    @Autowired private SemanticEvaluationService   semanticService;
    @Autowired private ConfidenceAnalyzerService   confidenceService;
    @Autowired private CompletenessAnalyzerService completenessService;

    
    private static final double CORRECTNESS_WEIGHT  = 0.60;
    private static final double CONFIDENCE_WEIGHT   = 0.25;
    private static final double COMPLETENESS_WEIGHT = 0.15;

    public double calculateFinalScore(String question,
                                      String studentAnswer,
                                      String modelAnswer) {
        double correctness  = semanticService.evaluateAnswer(question, studentAnswer, modelAnswer);
        double confidence   = confidenceService.analyzeConfidence(studentAnswer);
        double completeness = completenessService.analyzeCompleteness(studentAnswer, modelAnswer);

        return (correctness  * CORRECTNESS_WEIGHT)
             + (confidence   * CONFIDENCE_WEIGHT)
             + (completeness * COMPLETENESS_WEIGHT);
    }

    
    public double[] getComponentScores(String question,
                                       String studentAnswer,
                                       String modelAnswer) {
        double correctness  = semanticService.evaluateAnswer(question, studentAnswer, modelAnswer);
        double confidence   = confidenceService.analyzeConfidence(studentAnswer);
        double completeness = completenessService.analyzeCompleteness(studentAnswer, modelAnswer);
        double finalScore   = (correctness  * CORRECTNESS_WEIGHT)
                            + (confidence   * CONFIDENCE_WEIGHT)
                            + (completeness * COMPLETENESS_WEIGHT);
        return new double[]{correctness, confidence, completeness, finalScore};
    }

    
    public String calculateGrade(double score) {
        if (score >= 7.0) return "A";
        if (score >= 5.5) return "B";
        if (score >= 4.0) return "C";
        if (score >= 2.5) return "D";
        return "F";
    }

    public boolean isPassed(double score) {
        return score >= 6.0;
    }
}
