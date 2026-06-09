package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.aivivasystem.viva.model.VivaResult;

import java.util.List;

public interface VivaResultRepository extends JpaRepository<VivaResult, Long> {
    List<VivaResult> findByStudentEmail(String email);

    
    @Query("SELECT AVG(r.totalScore) FROM VivaResult r")
    Double findAverageScore();

    
    @Query("SELECT r.grade, COUNT(r) FROM VivaResult r GROUP BY r.grade")
    List<Object[]> findScoreDistribution();
}
