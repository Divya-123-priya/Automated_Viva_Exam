package com.aivivasystem.viva.service;

import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class CompletenessAnalyzerService {

    
    private static final Set<String> STOP_WORDS = new HashSet<>(Arrays.asList(
            "the", "a", "an", "is", "are", "was", "were", "be", "been",
            "to", "of", "and", "in", "that", "it", "for", "on", "with",
            "as", "at", "by", "from", "or", "but", "not", "this", "can"
    ));

    public double analyzeCompleteness(String studentAnswer, String modelAnswer) {
        if (studentAnswer == null || studentAnswer.trim().isEmpty()) return 0.0;
        if (modelAnswer   == null || modelAnswer.trim().isEmpty())   return 0.0;

        double score = 0.0;

        
        String[] modelWords = modelAnswer.toLowerCase().split("\\s+");
        List<String> keyConcepts = new ArrayList<>();
        for (String word : modelWords) {
            String clean = word.replaceAll("[^a-z0-9]", "");
            if (clean.length() > 4 && !STOP_WORDS.contains(clean)) {
                keyConcepts.add(clean);
            }
        }

        if (!keyConcepts.isEmpty()) {
            long covered = keyConcepts.stream()
                    .filter(c -> studentAnswer.toLowerCase().contains(c))
                    .count();
            double coverageRatio = (double) covered / keyConcepts.size();
            score += coverageRatio * 4.0;
        }

        
        int studentWords = studentAnswer.trim().split("\\s+").length;
        int modelWords2  = modelAnswer.trim().split("\\s+").length;
        double depthRatio = Math.min((double) studentWords / Math.max(modelWords2, 1), 2.0);
        score += Math.min(depthRatio * 2.5, 4.0);  // now rewards longer answers up to 4 points
        
        String lower = studentAnswer.toLowerCase();
        boolean hasExample = lower.contains("for example") || lower.contains("e.g")
                || lower.contains("such as")   || lower.contains("instance")
                || lower.contains("like when") || lower.contains("consider");
        if (hasExample) score += 2.0;

        
        long sentenceCount = Arrays.stream(studentAnswer.split("[.!?]"))
                .filter(s -> s.trim().length() > 5).count();
        if (sentenceCount >= 3) score += 2.0;
        else if (sentenceCount >= 2) score += 1.0;

        return Math.min(Math.max(score, 0.0), 10.0);
    }
}

