package com.aivivasystem.viva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.aivivasystem.viva.model.VivaResult;
import com.aivivasystem.viva.repository.VivaResultRepository;
import com.aivivasystem.viva.service.AnalyticsService;
import com.aivivasystem.viva.service.EmailService;
import com.aivivasystem.viva.service.FeedbackService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
public class AdminResultController {

    @Autowired private VivaResultRepository resultRepo;
    @Autowired private AnalyticsService     analyticsService;
    @Autowired private EmailService         emailService;
    @Autowired private FeedbackService      feedbackService;

    @GetMapping("/results")
    public ResponseEntity<Page<VivaResult>> getResults(
            @RequestParam(defaultValue = "0")  int page,
            @RequestParam(defaultValue = "20") int size) {
        return ResponseEntity.ok(resultRepo.findAll(
                PageRequest.of(page, size, Sort.by("createdAt").descending())));
    }

    @GetMapping("/results/student/{email}")
    public ResponseEntity<List<VivaResult>> getStudentResults(@PathVariable String email) {
        return ResponseEntity.ok(analyticsService.getStudentResults(email));
    }

    
    @GetMapping("/analytics/summary")
    public ResponseEntity<Map<String, Object>> getClassSummary() {
        return ResponseEntity.ok(analyticsService.getClassSummary());
    }

    @GetMapping("/analytics/top")
    public ResponseEntity<List<VivaResult>> getTop(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getTopPerformers(limit));
    }


    @GetMapping("/analytics/bottom")
    public ResponseEntity<List<VivaResult>> getBottom(
            @RequestParam(defaultValue = "10") int limit) {
        return ResponseEntity.ok(analyticsService.getBottomPerformers(limit));
    }

    @PostMapping("/results/{resultId}/email")
    public ResponseEntity<?> resendEmail(@PathVariable Long resultId) {
        VivaResult result = resultRepo.findById(resultId).orElse(null);
        if (result == null) return ResponseEntity.notFound().build();

        String name    = (result.getStudentName() != null && !result.getStudentName().isBlank())
                         ? result.getStudentName() : result.getStudentEmail();
        String subject = (result.getSubject() != null) ? result.getSubject() : "your subject";

        String body = feedbackService.buildStudentEmailBody(name, subject, result.getResultFeedback());

        try {
            emailService.sendResult(result.getStudentEmail(), body);
            return ResponseEntity.ok(Map.of("message", "Feedback email resent to " + result.getStudentEmail()));
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body(Map.of("error", "Email failed: " + e.getMessage()));
        }
    }
}