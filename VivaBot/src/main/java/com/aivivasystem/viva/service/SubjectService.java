package com.aivivasystem.viva.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.aivivasystem.viva.model.Subject;
import com.aivivasystem.viva.repository.SubjectRepository;

import java.util.List;

@Service
public class SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;

    public Subject addSubject(Subject subject) {
        if (subjectRepository.existsBySubjectName(subject.getSubjectName())) {
            throw new RuntimeException("Subject already exists: " + subject.getSubjectName());
        }
        return subjectRepository.save(subject);
    }

    public List<Subject> getAllSubjects() {
        return subjectRepository.findAll();
    }

    public Subject getById(Long id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject not found: " + id));
    }
}
