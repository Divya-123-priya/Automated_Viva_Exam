package com.aivivasystem.viva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aivivasystem.viva.model.User;
import com.aivivasystem.viva.model.VivaAnswer;
import com.aivivasystem.viva.model.VivaSession;
import com.aivivasystem.viva.repository.UserRepository;
import com.aivivasystem.viva.repository.VivaAnswerRepository;
import com.aivivasystem.viva.repository.VivaSessionRepository;
import com.aivivasystem.viva.service.FeedbackService;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/session")
public class VivaSessionController {

    @Autowired private VivaSessionRepository sessionRepo;
    @Autowired private VivaAnswerRepository  answerRepo;
    @Autowired private FeedbackService       feedbackService;
    @Autowired private UserRepository        userRepo;  

    @PostMapping("/start")
    public ResponseEntity<VivaSession> startSession(@RequestBody VivaSession session) {
        session.setCurrentQuestion(1);
        session.setCompleted(false);
        session.setStartedAt(LocalDateTime.now());
        return ResponseEntity.ok(sessionRepo.save(session));
    }

    @PutMapping("/{sessionId}/next")
    public ResponseEntity<?> nextQuestion(@PathVariable Long sessionId) {
        VivaSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        if (session.isCompleted()) {
            return ResponseEntity.badRequest().body("Session already completed");
        }

        int next = session.getCurrentQuestion() + 1;
        if (next > session.getTotalQuestions()) {
            return completeSession(sessionId);
        }

        session.setCurrentQuestion(next);
        return ResponseEntity.ok(sessionRepo.save(session));
    }

    @PutMapping("/{sessionId}/complete")
    public ResponseEntity<?> completeSession(@PathVariable Long sessionId) {
        VivaSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));

        session.setCompleted(true);
        session.setCompletedAt(LocalDateTime.now());
        sessionRepo.save(session);

        List<VivaAnswer> answers = answerRepo.findBySessionId(sessionId);

        if (answers.isEmpty()) {
            return ResponseEntity.ok(Map.of(
                    "message",   "Session completed with no answers submitted",
                    "sessionId", sessionId
            ));
        }

        double avgCorrectness  = answers.stream().mapToDouble(VivaAnswer::getCorrectnessScore).average().orElse(0);
        double avgConfidence   = answers.stream().mapToDouble(VivaAnswer::getConfidenceScore).average().orElse(0);
        double avgCompleteness = answers.stream().mapToDouble(VivaAnswer::getCompletenessScore).average().orElse(0);
        double avgFinal        = answers.stream().mapToDouble(VivaAnswer::getFinalScore).average().orElse(0);

        User student = userRepo.findById(session.getStudentId()).orElse(null);
        String studentEmail = (student != null)
                ? student.getEmail()
                : "student_" + session.getStudentId() + "@exam.local"; 

        var result = feedbackService.generateAndSaveResult(
                studentEmail,
                sessionId,
                session.getSubject(),
                avgCorrectness,
                avgConfidence,
                avgCompleteness,
                avgFinal,
                true    
        );

        return ResponseEntity.ok(Map.of(
                "message",   "Session completed. Feedback sent to your email.",
                "sessionId", sessionId,
                "resultId",  result.getId()
        ));
    }

    @GetMapping("/{sessionId}")
    public ResponseEntity<?> getSession(@PathVariable Long sessionId) {
        VivaSession session = sessionRepo.findById(sessionId)
                .orElseThrow(() -> new RuntimeException("Session not found"));
        return ResponseEntity.ok(session);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<VivaSession>> getStudentSessions(
            @PathVariable Long studentId) {
        return ResponseEntity.ok(sessionRepo.findByStudentId(studentId));
    }
}