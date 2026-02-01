package com.vidyapp.services;

import com.vidyapp.dtos.SubjectDTO;
import com.vidyapp.models.Grade;
import com.vidyapp.models.Subject;
import com.vidyapp.repositories.GradeRepository;
import com.vidyapp.repositories.SubjectRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubjectServiceImpl implements SubjectService {
    private final SubjectRepository subjectRepository;
    private final GradeRepository gradeRepository;
    private final ModelMapper modelMapper;


    @Override
    public SubjectDTO create(String name, String code, String description, short optional, LocalTime startTime, LocalTime endTime, int gradeId) {
        Grade grade = findGradeById(gradeId);

        Subject subject = new Subject();
        subject.setName(name);
        subject.setCode(code);
        subject.setDescription(description);
        subject.setOptional(optional);
        subject.setStartTime(startTime);
        subject.setEndTime(endTime);
        subject.setGrade(grade);
        subject.setActive(true); // Default to true for new subjects

        return mapToDTO(subjectRepository.save(subject));
    }

    @Override
    public SubjectDTO update(int subjectId, String name, String code, String description, short optional, LocalTime startTime, LocalTime endTime, boolean active, int gradeId) {
        Subject subject = findSubjectById(subjectId);
        Grade grade = findGradeById(gradeId);

        subject.setName(name);
        subject.setCode(code);
        subject.setDescription(description);
        subject.setOptional(optional);
        subject.setStartTime(startTime);
        subject.setEndTime(endTime);
        subject.setActive(active);
        subject.setGrade(grade);

        return mapToDTO(subjectRepository.save(subject));
    }

    @Override
    public void softDelete(int subjectId, String name) {
        Subject subject = findSubjectById(subjectId);
        if (!subject.getName().equalsIgnoreCase(name)){
            throw new IllegalArgumentException("Delete failed: Provided name does not match the Subject ID.");
        }
        subject.setActive(false);
        subjectRepository.save(subject);
    }

    @Override
    public void delete(int subjectId) {
        Subject subject = findSubjectById(subjectId);
        subjectRepository.delete(subject);
    }

    @Override
    public List<SubjectDTO> getAll() {
        return subjectRepository.findAll().stream()
                .filter(Subject::isActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<SubjectDTO> getAll(int gradeId) {
        return subjectRepository.findAllByGrade_GradeIdAndActiveTrue(gradeId).stream()
                .filter(Subject::isActive)
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Consolidated Helper Methods
    private Grade findGradeById(int id) {
        return gradeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Grade with ID " + id + " not found"));
    }

    private Subject findSubjectById(int id) {
        return subjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subject with ID " + id + " not found"));
    }

    private SubjectDTO mapToDTO(Subject subject) {
        return modelMapper.map(subject, SubjectDTO.class);
    }
}
