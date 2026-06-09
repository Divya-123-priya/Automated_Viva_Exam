package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.Subject;

import java.util.Optional;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    Optional<Subject> findBySubjectName(String subjectName);
    boolean existsBySubjectName(String subjectName);
}
