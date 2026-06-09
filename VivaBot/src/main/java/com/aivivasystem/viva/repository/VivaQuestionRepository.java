package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.VivaQuestion;

import java.util.List;

public interface VivaQuestionRepository extends JpaRepository<VivaQuestion, Long> {
    List<VivaQuestion> findBySubjectAndDifficulty(String subject, String difficulty);
    List<VivaQuestion> findBySubject(String subject);
}

