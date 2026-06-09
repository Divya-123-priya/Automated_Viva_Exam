package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.VivaSession;

import java.util.List;

public interface VivaSessionRepository extends JpaRepository<VivaSession, Long> {
    
    List<VivaSession> findByStudentId(Long studentId);

    
    List<VivaSession> findByStudentIdAndCompleted(Long studentId, boolean completed);
}
