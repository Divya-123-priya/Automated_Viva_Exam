package com.aivivasystem.viva.repository;


import org.springframework.data.jpa.repository.JpaRepository;

import com.aivivasystem.viva.model.StudyMaterial;

import java.util.List;

public interface StudyMaterialRepository extends JpaRepository<StudyMaterial, Long> {
   
    List<StudyMaterial> findBySubjectId(Long subjectId);
}
