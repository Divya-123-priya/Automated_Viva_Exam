package com.aivivasystem.viva.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.aivivasystem.viva.model.StudyMaterial;
import com.aivivasystem.viva.model.Subject;
import com.aivivasystem.viva.repository.StudyMaterialRepository;
import com.aivivasystem.viva.repository.SubjectRepository;
import com.aivivasystem.viva.service.SubjectService;
import com.aivivasystem.viva.util.PDFTextExtractor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired private SubjectService          subjectService;
    @Autowired private SubjectRepository       subjectRepository;
    @Autowired private StudyMaterialRepository studyMaterialRepository;

    @Value("${app.upload.dir:uploads/}")
    private String uploadDir;

    @PostMapping("/subject")
    public ResponseEntity<?> addSubject(@RequestBody Subject subject) {
        try {
            Subject saved = subjectService.addSubject(subject);
            return ResponseEntity.ok(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/subjects")
    public ResponseEntity<List<Subject>> getSubjects() {
        return ResponseEntity.ok(subjectService.getAllSubjects());
    }

    @PostMapping("/uploadMaterial")
    public ResponseEntity<?> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam Long subjectId) {

        Subject subject = subjectRepository.findById(subjectId).orElse(null);
        if (subject == null)  return ResponseEntity.badRequest().body("Subject not found");
        if (file.isEmpty())   return ResponseEntity.badRequest().body("File is empty");

        String contentType = file.getContentType();
        if (contentType == null || !contentType.equals("application/pdf"))
            return ResponseEntity.badRequest().body("Only PDF files are allowed");

        if (file.getSize() > 10 * 1024 * 1024)
            return ResponseEntity.badRequest().body("File size must be under 10MB");

        try {
            String safeFileName = UUID.randomUUID() + "_"
                    + file.getOriginalFilename().replaceAll("[^a-zA-Z0-9._-]", "_");

            Path uploadPath = Paths.get(uploadDir, "documents").toAbsolutePath();
            Files.createDirectories(uploadPath);                
            Path filePath = uploadPath.resolve(safeFileName);

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            }

            String text = PDFTextExtractor.extractText(filePath.toString());

            StudyMaterial material = new StudyMaterial();
            material.setFileName(file.getOriginalFilename());
            material.setFilePath(filePath.toString());
            material.setExtractedText(text);
            material.setSubject(subject);
            studyMaterialRepository.save(material);

            return ResponseEntity.ok("Material uploaded successfully. Extracted "
                    + text.length() + " characters.");

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Upload failed: " + e.getMessage());
        }
    }

    @GetMapping("/materials/{subjectId}")
    public ResponseEntity<?> getMaterials(@PathVariable Long subjectId) {
        return ResponseEntity.ok(studyMaterialRepository.findBySubjectId(subjectId));
    }
}