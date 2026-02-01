package com.vidyapp.controllers;

import com.vidyapp.dtos.SubjectDTO;
import com.vidyapp.services.SubjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subjects")
@RequiredArgsConstructor
public class SubjectController {
    private final SubjectService subjectService;

    @PostMapping
    public ResponseEntity<SubjectDTO> createSubject(@Valid @RequestBody SubjectDTO subjectDTO) {
        SubjectDTO createdSubject = subjectService.create(
                subjectDTO.getName(),
                subjectDTO.getCode(),
                subjectDTO.getDescription(),
                subjectDTO.getOptional(),
                subjectDTO.getStartTime(),
                subjectDTO.getEndTime(),
                subjectDTO.getGradeId()
        );
        return new ResponseEntity<>(createdSubject, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<SubjectDTO>> getSubjects() {
        List<SubjectDTO> subjects = subjectService.getAll();
        return ResponseEntity.ok(subjects);
    }

    @GetMapping("/{subjectId}")
    public ResponseEntity<List<SubjectDTO>> getSubjectsByGrade(@PathVariable int subjectId) {
        List<SubjectDTO> subjects = subjectService.getAll(subjectId);
        return ResponseEntity.ok(subjects);
    }

    @PutMapping("/{subjectId}")
    public ResponseEntity<SubjectDTO> updateSubject(
            @PathVariable int subjectId,
            @Valid @RequestBody SubjectDTO subjectDTO) {

        SubjectDTO updatedSubject = subjectService.update(
                subjectId,
                subjectDTO.getName(),
                subjectDTO.getCode(),
                subjectDTO.getDescription(),
                subjectDTO.getOptional(),
                subjectDTO.getStartTime(),
                subjectDTO.getEndTime(),
                subjectDTO.isActive(),
                subjectDTO.getGradeId()
        );
        return ResponseEntity.ok(updatedSubject);
    }

    @DeleteMapping("/{subjectId}/{name}")
    public ResponseEntity<Void> softDeleteGrade(
            @PathVariable int subjectId,
            @PathVariable String name) {

        subjectService.softDelete(subjectId, name);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{subjectId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable int subjectId) {
        subjectService.delete(subjectId);
        return ResponseEntity.noContent().build();
    }
}
