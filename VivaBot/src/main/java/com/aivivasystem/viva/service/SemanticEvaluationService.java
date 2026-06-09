package com.aivivasystem.viva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


@Service
public class SemanticEvaluationService {

    @Autowired
    private AIService aiService;

    private final ObjectMapper mapper = new ObjectMapper();

    
    public double evaluateAnswer(String question, String studentAnswer, String modelAnswer) {
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) return 0.0;
        if (modelAnswer   == null || modelAnswer.trim().isEmpty())   return 0.0;

        try {
            
            String aiResponse = aiService.evaluateAnswer(question, studentAnswer, modelAnswer);

            
            JsonNode json = mapper.readTree(aiResponse);
            if (json.has("score")) {
                double score = json.get("score").asDouble();
                return Math.min(Math.max(score, 0), 10); // clamp 0-10
            }

        } catch (Exception e) {
            
            return keywordFallback(studentAnswer, modelAnswer);
        }

        return keywordFallback(studentAnswer, modelAnswer);
    }

    
    public String extractFeedback(String question, String studentAnswer, String modelAnswer) {
        try {
            String aiResponse = aiService.evaluateAnswer(question, studentAnswer, modelAnswer);
            JsonNode json = mapper.readTree(aiResponse);
            if (json.has("feedback")) {
                return json.get("feedback").asText();
            }
        } catch (Exception ignored) {}
        return "Answer evaluated.";
    }

    
    private double keywordFallback(String studentAnswer, String modelAnswer) {
        if (studentAnswer == null || modelAnswer == null) return 0;
        String[] words = modelAnswer.toLowerCase().split("\\s+");
        long matched = 0;
        for (String word : words) {
            if (word.length() > 3 && studentAnswer.toLowerCase().contains(word)) {
                matched++;
            }
        }
        if (words.length == 0) return 0;
        return Math.min(((double) matched / words.length) * 10, 10);
    }
}

