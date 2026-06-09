package com.aivivasystem.viva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aivivasystem.viva.model.User;
import com.aivivasystem.viva.model.VivaResult;
import com.aivivasystem.viva.repository.UserRepository;
import com.aivivasystem.viva.repository.VivaResultRepository;

@Service
public class FeedbackService {

    @Autowired private VivaResultRepository resultRepo;
    @Autowired private UserRepository       userRepo;
    @Autowired private AIService            aiService;
    @Autowired private EmailService         emailService;
    @Autowired private ScoreService         scoreService;

    public VivaResult generateAndSaveResult(String studentEmail,
                                             Long sessionId,
                                             String subject,
                                             double correctness,
                                             double confidence,
                                             double completeness,
                                             double finalScore,
                                             boolean sendEmail) {

        String grade = scoreService.calculateGrade(finalScore);

        User student       = userRepo.findByEmail(studentEmail);
        String studentName = (student != null
                              && student.getFullName() != null
                              && !student.getFullName().isBlank())
                             ? student.getFullName() : studentEmail;
        String regNumber   = (student != null) ? student.getRegisterNumber() : null;

        String feedback;
        try {
            feedback = aiService.generatePersonalisedFeedback(
                    studentEmail, correctness, confidence, completeness, finalScore, subject);
        } catch (Exception e) {
            feedback = "Thank you for completing your viva examination in " + subject + ". "
                    + "Your responses have been recorded and your faculty will review them. "
                    + "Keep practising and reviewing your course material.";
        }

        VivaResult result = new VivaResult();
        result.setStudentEmail(studentEmail);
        result.setStudentName(studentName);
        result.setRegisterNumber(regNumber);
        result.setSubject(subject);
        result.setSessionId(sessionId);
        result.setTotalScore(finalScore);
        result.setCorrectnessScore(correctness);
        result.setConfidenceScore(confidence);
        result.setCompletenessScore(completeness);
        result.setGrade(grade);
        result.setResultFeedback(feedback);
        VivaResult saved = resultRepo.save(result); 

        if (sendEmail) {
            try {
                String body = buildStudentEmailBody(studentName, subject, feedback);
                emailService.sendResult(studentEmail, body);
            } catch (Exception e) {
                System.err.println("[VivaBot] Email failed for " + studentEmail
                        + " — result saved. Error: " + e.getMessage());
            }
        }

        return saved;
    }

    public String buildStudentEmailBody(String studentName, String subject, String feedback) {
        return "Dear " + studentName + ",\n\n"
                + "Thank you for completing your Viva Examination.\n"
                + "Subject: " + subject + "\n\n"
                + "YOUR PERSONALISED FEEDBACK\n\n"
                + feedback + "\n\n"
                + "Your responses have been recorded. Your faculty will\n"
                + "review your performance and may discuss it with you.\n\n"
                + "Keep studying and best of luck!\n\n"
                + "Regards,\n"
                + "VivaBot Examination System\n"
                + "Meenakshi College of Engineering";
    }
}