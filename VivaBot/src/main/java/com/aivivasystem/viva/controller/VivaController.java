package com.aivivasystem.viva.controller;


import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aivivasystem.viva.dto.ScoreResponse;
import com.aivivasystem.viva.model.StudyMaterial;
import com.aivivasystem.viva.model.VivaAnswer;
import com.aivivasystem.viva.model.VivaQuestion;
import com.aivivasystem.viva.repository.StudyMaterialRepository;
import com.aivivasystem.viva.repository.VivaAnswerRepository;
import com.aivivasystem.viva.repository.VivaQuestionRepository;
import com.aivivasystem.viva.service.AIService;
import com.aivivasystem.viva.service.CompletenessAnalyzerService;
import com.aivivasystem.viva.service.ConfidenceAnalyzerService;
import com.aivivasystem.viva.service.ScoreService;
import com.aivivasystem.viva.service.SemanticEvaluationService;

import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/viva")
public class VivaController {

    @Autowired private AIService                   aiService;
    @Autowired private ScoreService                scoreService;
    @Autowired private SemanticEvaluationService   semanticService;
    @Autowired private ConfidenceAnalyzerService   confidenceService;
    @Autowired private CompletenessAnalyzerService completenessService;
    @Autowired private VivaAnswerRepository        answerRepository;
    @Autowired private VivaQuestionRepository      questionRepository;
    @Autowired private StudyMaterialRepository     materialRepository;

    @GetMapping("/question")
    public ResponseEntity<?> generateQuestion(
            @RequestParam String subject,
            @RequestParam String difficulty,
            @RequestParam(required = false) String previousQuestions) {

        List<String> prevList = new ArrayList<>();
        if (previousQuestions != null && !previousQuestions.isBlank()) {
            prevList = Arrays.asList(previousQuestions.split("\\|\\|"));
        }

        String questionText = aiService.generateQuestion(subject, difficulty, prevList);
        String modelAnswer  = aiService.generateModelAnswer(questionText);

        VivaQuestion question = new VivaQuestion();
        question.setSubject(subject);
        question.setDifficulty(difficulty);
        question.setQuestionText(questionText);
        question.setModelAnswer(modelAnswer);
        VivaQuestion saved = questionRepository.save(question);

        return ResponseEntity.ok(Map.of(
                "questionId",   saved.getId(),
                "questionText", saved.getQuestionText(),
                "subject",      saved.getSubject(),
                "difficulty",   saved.getDifficulty()
        ));
    }

    @GetMapping("/questionFromMaterial")
    public ResponseEntity<?> generateQuestionFromMaterial(
            @RequestParam Long materialId,
            @RequestParam String difficulty) {

        StudyMaterial material = materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found: " + materialId));

        String questionText = aiService.generateQuestionFromMaterial(
                material.getExtractedText(), difficulty);

        String modelAnswer = aiService.generateModelAnswer(questionText);

        VivaQuestion question = new VivaQuestion();
        question.setSubject(material.getSubject().getSubjectName());
        question.setDifficulty(difficulty);
        question.setQuestionText(questionText);
        question.setModelAnswer(modelAnswer);
        VivaQuestion saved = questionRepository.save(question);

        return ResponseEntity.ok(Map.of(
                "questionId",   saved.getId(),
                "questionText", saved.getQuestionText(),
                "materialName", material.getFileName()
        ));
    }

    @PostMapping("/answer")
    public ResponseEntity<?> submitAnswer(@RequestBody Map<String, Object> request) {

        Long   questionId  = Long.valueOf(request.get("questionId").toString());
        Long   sessionId   = request.containsKey("sessionId")
                             ? Long.valueOf(request.get("sessionId").toString()) : null;
        String answerText  = request.get("answerText").toString();

        VivaQuestion question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("Question not found: " + questionId));

        String modelAnswer  = question.getModelAnswer();
        String questionText = question.getQuestionText();

        double correctness = semanticService.evaluateAnswer(
                questionText, answerText, modelAnswer);

        double confidence = confidenceService.analyzeConfidence(answerText);

      
        double completeness = completenessService.analyzeCompleteness(
                answerText, modelAnswer);

       
        double finalScore = (correctness  * 0.60)
                          + (confidence   * 0.25)
                          + (completeness * 0.15);

        String grade    = scoreService.calculateGrade(finalScore);

        
        String feedback = semanticService.extractFeedback(
                questionText, answerText, modelAnswer);

        
        VivaAnswer answer = new VivaAnswer();
        answer.setQuestionId(questionId);
        answer.setSessionId(sessionId);
        answer.setAnswerText(answerText);
        answer.setCorrectnessScore(correctness);
        answer.setConfidenceScore(confidence);
        answer.setCompletenessScore(completeness);
        answer.setFinalScore(finalScore);

        answer.setGrade(grade);
        answer.setFeedback(feedback);
        answerRepository.save(answer);

        return ResponseEntity.ok(new ScoreResponse(
                correctness, confidence, completeness, finalScore, grade, feedback));
    }

   
    @GetMapping("/question/{id}")
    public ResponseEntity<?> getQuestion(@PathVariable Long id) {
        VivaQuestion q = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Question not found"));
        
        return ResponseEntity.ok(Map.of(
                "questionId",   q.getId(),
                "questionText", q.getQuestionText(),
                "subject",      q.getSubject(),
                "difficulty",   q.getDifficulty()
        ));
    }
}