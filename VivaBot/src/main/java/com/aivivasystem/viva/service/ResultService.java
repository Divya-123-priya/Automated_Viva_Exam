package com.aivivasystem.viva.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aivivasystem.viva.model.VivaResult;
import com.aivivasystem.viva.repository.VivaResultRepository;


@Service
public class ResultService {

    @Autowired private VivaResultRepository resultRepo;
    @Autowired private ScoreService         scoreService;

    
    public VivaResult saveResult(String email, double score) {
        VivaResult result = new VivaResult();
        result.setStudentEmail(email);
        result.setTotalScore(score);
        result.setGrade(scoreService.calculateGrade(score));

        
        String grade = result.getGrade();
        if (grade.equals("A"))      result.setResultFeedback("Excellent performance!");
        else if (grade.equals("B")) result.setResultFeedback("Good performance. Keep it up.");
        else if (grade.equals("C")) result.setResultFeedback("Average. Review weak topics.");
        else if (grade.equals("D")) result.setResultFeedback("Below average. More practice needed.");
        else                        result.setResultFeedback("Needs significant improvement.");

        return resultRepo.save(result);
    }
}

