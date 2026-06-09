package com.aivivasystem.viva.service;

import org.springframework.stereotype.Service;
import java.util.*;


@Service
public class ConfidenceAnalyzerService {

   
    private static final List<String> ASSERTIVE_WORDS = Arrays.asList(
            "definitely", "certainly", "clearly", "obviously", "always",
            "because", "therefore", "thus", "hence", "this means",
            "the reason is", "in conclusion", "for example", "such as"
    );

    
    private static final List<String> HESITANT_WORDS = Arrays.asList(
            "maybe", "perhaps", "i think", "i guess", "i believe",
            "probably", "possibly", "not sure", "i'm not sure",
            "i don't know", "kind of", "sort of", "somewhat", "might be"
    );

   
    private static final List<String> FILLER_WORDS = Arrays.asList(
            " um ", " uh ", " like ", " you know ", " basically ",
            " actually ", " literally ", " so ", " well "
    );

    public double analyzeConfidence(String answer) {
        if (answer == null || answer.trim().isEmpty()) return 0.0;

        String lower = answer.toLowerCase();
        double score = 0.0;

        
        long assertiveCount = ASSERTIVE_WORDS.stream()
                .filter(lower::contains).count();
        score += Math.min(assertiveCount * 1.0, 4.0);

        
        long hesitantCount = HESITANT_WORDS.stream()
                .filter(lower::contains).count();
        score -= Math.min(hesitantCount * 0.75, 3.0);

        
        long fillerCount = FILLER_WORDS.stream()
                .filter(lower::contains).count();
        score -= Math.min(fillerCount * 0.5, 2.0);

        
        int wordCount = answer.trim().split("\\s+").length;
        if      (wordCount >= 150) score += 4.0;  
        else if (wordCount >= 80)  score += 3.0;  
        else if (wordCount >= 40)  score += 2.0;  
        else if (wordCount >= 15)  score += 1.0;
        else if (wordCount >= 5)   score += 0.5;
        
        long conjunctions = Arrays.stream(new String[]{"because", "however",
                "therefore", "although", "furthermore", "moreover", "which"})
                .filter(lower::contains).count();
        score += Math.min(conjunctions * 0.5, 2.0);

        
        return Math.min(Math.max(score, 0.0), 10.0);
    }
}
