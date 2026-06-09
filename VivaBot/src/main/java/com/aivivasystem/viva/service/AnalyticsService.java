package com.aivivasystem.viva.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aivivasystem.viva.model.VivaResult;
import com.aivivasystem.viva.repository.VivaAnswerRepository;
import com.aivivasystem.viva.repository.VivaResultRepository;
import com.aivivasystem.viva.repository.VivaSessionRepository;

import java.util.*;


@Service
public class AnalyticsService {

    @Autowired private VivaResultRepository  resultRepo;
    @Autowired private VivaSessionRepository sessionRepo;
    @Autowired private VivaAnswerRepository  answerRepo;

   
    public List<VivaResult> getStudentResults(String email) {
        return resultRepo.findByStudentEmail(email);
    }

    
    public Map<String, Object> getClassSummary() {
        Map<String, Object> summary = new LinkedHashMap<>();

        long totalStudents = resultRepo.count();
        summary.put("totalExams", totalStudents);

        Double avg = resultRepo.findAverageScore();
        summary.put("classAverage", avg != null ? String.format("%.2f", avg) : "0.00");

        
        List<Object[]> distribution = resultRepo.findScoreDistribution();
        Map<String, Long> gradeMap = new LinkedHashMap<>();
        for (Object[] row : distribution) {
            gradeMap.put(String.valueOf(row[0]), ((Number) row[1]).longValue());
        }
        summary.put("gradeDistribution", gradeMap);

       
        long passed = gradeMap.entrySet().stream()
                .filter(e -> !e.getKey().equals("F"))
                .mapToLong(Map.Entry::getValue).sum();
        double passRate = totalStudents > 0 ? (double) passed / totalStudents * 100 : 0;
        summary.put("passRate", String.format("%.1f%%", passRate));

        return summary;
    }

    
    public List<VivaResult> getTopPerformers(int limit) {
        return resultRepo.findAll().stream()
                .sorted(Comparator.comparingDouble(VivaResult::getTotalScore).reversed())
                .limit(limit)
                .toList();
    }

    
    public List<VivaResult> getBottomPerformers(int limit) {
        return resultRepo.findAll().stream()
                .sorted(Comparator.comparingDouble(VivaResult::getTotalScore))
                .limit(limit)
                .toList();
    }
}
