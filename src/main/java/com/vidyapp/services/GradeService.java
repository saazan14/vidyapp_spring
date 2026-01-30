package com.vidyapp.services;

import com.vidyapp.dtos.GradeDTO;
import com.vidyapp.dtos.GradeListDTO;

import java.util.List;

public interface GradeService {
    GradeDTO create(String name, String description, String section, int batchId);
    GradeDTO update(int gradeId, String name, String description, String section, boolean active, int batchId);
    void softDelete(int gradeId, String name);
    void delete(int gradeId);
    List<GradeListDTO> getAll();
    List<GradeListDTO> getAll(int batchId);
}