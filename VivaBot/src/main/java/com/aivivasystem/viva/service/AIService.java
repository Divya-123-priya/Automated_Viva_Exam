package com.aivivasystem.viva.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    @Value("${openai.api.url}")
    private String apiUrl;

    private final ObjectMapper mapper = new ObjectMapper();

    public String generateQuestion(String subject, String difficulty, List<String> previousQuestions) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Generate exactly ONE short, direct viva exam question about ")
              .append(subject).append(" at difficulty level: ").append(difficulty).append(".\n\n");

        if (previousQuestions != null && !previousQuestions.isEmpty()) {
            prompt.append("STRICTLY DO NOT repeat or rephrase any of these already-asked questions:\n");
            for (int i = 0; i < previousQuestions.size(); i++) {
                prompt.append((i + 1)).append(". ").append(previousQuestions.get(i)).append("\n");
            }
            prompt.append("\nYou MUST ask about a completely DIFFERENT concept from the ones above.\n\n");
        }

        prompt.append("Rules for the question:\n");
        prompt.append("- Must be answerable in 3-5 sentences\n");
        prompt.append("- Ask about ONE concept only, not multiple\n");
        prompt.append("- Easy: define or explain a concept\n");
        prompt.append("- Medium: explain with one simple example\n");
        prompt.append("- Hard: compare two concepts or explain when to use something\n");
        prompt.append("- Do NOT ask for code implementation\n");
        prompt.append("- Do NOT ask multi-part questions\n");
        prompt.append("- Return ONLY the question text, nothing else.\n");
        return callOpenAI(prompt.toString());
    }

    public String generateQuestionFromMaterial(String materialText, String difficulty) {
        String excerpt = materialText.substring(0, Math.min(materialText.length(), 3000));
        String prompt = "Based on the following study material, generate exactly ONE viva question.\n"
                + "Difficulty: " + difficulty + "\n"
                + "Return only the question, nothing else.\n\n"
                + "Material:\n" + excerpt;
        return callOpenAI(prompt);
    }

    public String generateModelAnswer(String question) {
    	String prompt = "Provide a comprehensive model answer for this viva question:\n"
    	        + question
    	        + "\nInclude: definition, explanation, real-world example, and key points.\n"
    	        + "Write 6-8 sentences covering all important concepts thoroughly.";
        return callOpenAI(prompt);
    }

    public String evaluateAnswer(String question, String studentAnswer, String modelAnswer) {
    	String prompt = "You are a fair and encouraging viva examiner. Evaluate the student's answer.\n\n"
    	        + "Award full marks if the student demonstrates clear understanding even if "
    	        + "phrasing differs from model answer. Reward detailed answers with examples.\n\n"
                + "Question: " + question + "\n"
                + "Model Answer: " + modelAnswer + "\n"
                + "Student Answer: " + studentAnswer + "\n\n"
                + "Reply ONLY in this exact JSON format:\n"
                + "{\"score\": <0-10>, \"feedback\": \"<one sentence>\", "
                + "\"missing\": \"<key concepts missing>\", "
                + "\"correct\": \"<what student got right>\"}";
        return callOpenAI(prompt);
    }

    public String generatePersonalisedFeedback(String studentEmail,
                                                double correctness,
                                                double confidence,
                                                double completeness,
                                                double finalScore,
                                                String subject) {

        String correctnessLevel  = performanceLevel(correctness);
        String confidenceLevel   = performanceLevel(confidence);
        String completenessLevel = performanceLevel(completeness);

        String focusTopic = weakestArea(correctness, confidence, completeness);

        String prompt = "You are a supportive university viva examiner writing feedback to a student.\n"
                + "Subject examined: " + subject + "\n\n"
                + "Performance summary (DO NOT mention any numbers or grades in your response):\n"
                + "- Conceptual accuracy of answers: " + correctnessLevel + "\n"
                + "- Confidence and clarity of expression: " + confidenceLevel + "\n"
                + "- Completeness and depth of answers: " + completenessLevel + "\n"
                + "- Main area needing improvement: " + focusTopic + "\n\n"
                + "Write exactly 3 paragraphs of personalised feedback:\n"
                + "Do NOT label them. No 'Paragraph 1', no numbers, no headings. Just plain text paragraphs.\n\n"
                + "First: overall performance in " + subject + " and which topics were handled well.\n"
                + "Second: specific strengths in how concepts were explained.\n"
                + "Third: key topics in " + subject + " the student should focus on, with concrete study tips.\n\n"
                + "IMPORTANT RULES:\n"
                + "- Do NOT mention any scores, numbers, percentages, or letter grades.\n"
                + "- Do NOT say 'you scored' or 'your grade'.\n"
                + "- Focus only on topics, concepts, and study advice.\n"
                + "- Be encouraging, specific, and actionable.\n"
                + "- Address the student as 'you' directly.";
        return callOpenAI(prompt);
    }

    private String performanceLevel(double score) {
        if (score >= 8.5) return "excellent — thorough and accurate";
        if (score >= 7.0) return "good — mostly correct with minor gaps";
        if (score >= 5.0) return "satisfactory — basic understanding shown";
        if (score >= 4.0) return "needs improvement — some key concepts missing";
        return "weak — significant gaps in understanding";
    }

    private String weakestArea(double correctness, double confidence, double completeness) {
        if (correctness <= confidence && correctness <= completeness)
            return "conceptual accuracy — reviewing core definitions and theory";
        if (confidence <= correctness && confidence <= completeness)
            return "confidence and expression — practising spoken/written explanation";
        return "answer completeness — giving fuller explanations with examples";
    }

    public String callOpenAI(String prompt) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(apiKey);
            headers.add("HTTP-Referer", "http://localhost:8080");
            headers.add("X-Title", "AI Viva Examiner");

            Map<String, Object> request = new HashMap<>();
            request.put("model", "openai/gpt-3.5-turbo");

            List<Map<String, String>> messages = new ArrayList<>();
            messages.add(Map.of("role", "user", "content", prompt));
            request.put("messages", messages);
            request.put("temperature", 0.9);
            request.put("max_tokens", 1500);  

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(request, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(
                    apiUrl, entity, String.class);

            JsonNode root = mapper.readTree(response.getBody());
            return root.path("choices").get(0)
                       .path("message").path("content")
                       .asText().trim();

        } catch (Exception e) {
            e.printStackTrace();
            return "Error: Could not get AI response. " + e.getMessage();
        }
    }
}