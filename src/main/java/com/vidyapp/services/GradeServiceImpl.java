package com.vidyapp.services;

import com.vidyapp.dtos.GradeDTO;
import com.vidyapp.dtos.GradeListDTO;
import com.vidyapp.models.Batch;
import com.vidyapp.models.Grade;
import com.vidyapp.repositories.BatchRepository;
import com.vidyapp.repositories.GradeRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GradeServiceImpl implements GradeService {

    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;
    private final BatchRepository batchRepository;

    @Override
    public GradeDTO create(String name, String description, String section, int batchId) {
        Batch batch = findBatchById(batchId);

        Grade grade = new Grade();
        grade.setName(name);
        grade.setDescription(description);
        grade.setSection(section);
        grade.setBatch(batch);
        return mapToDTO(gradeRepository.save(grade));
    }

    @Override
    public GradeDTO update(int gradeId, String name, String description, String section, boolean active, int batchId) {
        Grade grade = findGradeById(gradeId); // Using the helper method
        Batch batch = findBatchById(batchId);

        grade.setName(name);
        grade.setDescription(description);
        grade.setActive(active);
        grade.setSection(section);
        grade.setBatch(batch);

        return mapToDTO(gradeRepository.save(grade));
    }

    @Override
    public void softDelete(int gradeId, String name) {
        Grade grade = findGradeById(gradeId);

        if (!grade.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("Delete failed: Provided name does not match the Grade ID.");
        }

        grade.setActive(false);
        gradeRepository.save(grade);
    }

    @Override
    public void delete(int gradeId) {
        Grade grade = findGradeById(gradeId);
        gradeRepository.delete(grade);
    }

    @Override
    public List<GradeListDTO> getAll() {
        return gradeRepository.findAll().stream()
                // Change Grade::isActive to grade -> grade.isActive()
                .filter(Grade::isActive)
                .map(grade -> modelMapper.map(grade, GradeListDTO.class)) // Explicitly map to the List DTO
                .collect(Collectors.toList());
    }

    @Override
    public List<GradeListDTO> getAll(int batchId) {
        Batch batch = findBatchById(batchId);

        return gradeRepository.findAllByBatch(batch).stream() // Note: changed method name to match JPA standard
                .filter(Grade::isActive)
                .map(grade -> modelMapper.map(grade, GradeListDTO.class)) // Explicitly map to the List DTO
                .collect(Collectors.toList());
    }

    // Consolidated Helper Methods
    private Grade findGradeById(int id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade with ID " + id + " not found"));
    }

    private Batch findBatchById(int id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch with ID " + id + " not found"));
    }

    private GradeDTO mapToDTO(Grade grade) {
        return modelMapper.map(grade, GradeDTO.class);
    }
}
