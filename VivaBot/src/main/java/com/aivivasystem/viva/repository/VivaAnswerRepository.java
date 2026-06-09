package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.VivaAnswer;

import java.util.List;

public interface VivaAnswerRepository extends JpaRepository<VivaAnswer, Long> {
    
    List<VivaAnswer> findBySessionId(Long sessionId);

    List<VivaAnswer> findByQuestionId(Long questionId);
}

