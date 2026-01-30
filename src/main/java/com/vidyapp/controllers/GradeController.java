package com.vidyapp.controllers;

import com.vidyapp.dtos.GradeDTO;
import com.vidyapp.dtos.GradeListDTO;
import com.vidyapp.services.GradeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService gradeService;

    @PostMapping
    public ResponseEntity<GradeDTO> createGrade(@Valid @RequestBody GradeDTO gradeDTO) {
        GradeDTO createdGrade = gradeService.create(gradeDTO.getName(), gradeDTO.getDescription(), gradeDTO.getSection(), gradeDTO.getBatchId());
        return new ResponseEntity<>(createdGrade, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<GradeListDTO>> getGrades() {
        List<GradeListDTO> grades = gradeService.getAll();
        return ResponseEntity.ok(grades);
    }

    @GetMapping("/{batchId}")
    public ResponseEntity<List<GradeListDTO>> getGradesByBatch(@PathVariable int batchId) {
        List<GradeListDTO> grades = gradeService.getAll(batchId);
        return ResponseEntity.ok(grades);
    }

    @PutMapping("/{gradeId}")
    public ResponseEntity<GradeDTO> updateGrade(
            @PathVariable int gradeId,
            @Valid @RequestBody GradeDTO gradeDTO) {

        GradeDTO updatedGrade = gradeService.update(
                gradeId,
                gradeDTO.getName(),
                gradeDTO.getDescription(),
                gradeDTO.getSection(),
                gradeDTO.isActive(),
                gradeDTO.getBatchId()
        );
        return ResponseEntity.ok(updatedGrade);
    }

    @DeleteMapping("/{gradeId}/{name}")
    public ResponseEntity<Void> softDeleteGrade(
            @PathVariable int gradeId,
            @PathVariable String name) {

        gradeService.softDelete(gradeId, name);

        // Returns 204 No Content
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{gradeId}")
    public ResponseEntity<Void> deleteGrade(@PathVariable int gradeId) {
        gradeService.delete(gradeId);
        return ResponseEntity.noContent().build();
    }
}