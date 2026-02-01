package com.vidyapp.services;

import com.vidyapp.dtos.SubjectDTO;

import java.time.LocalTime;
import java.util.List;

public interface SubjectService {
    SubjectDTO create(String name, String code, String description, short optional, LocalTime startTime, LocalTime endTime, int gradeId);
    SubjectDTO update(int subjectId, String name, String code, String description, short optional, LocalTime startTime, LocalTime endTime, boolean active, int gradeId);
    void softDelete(int subjectId, String name);
    void delete(int subjectId);
    List<SubjectDTO> getAll();
    List<SubjectDTO> getAll(int gradeId);
}