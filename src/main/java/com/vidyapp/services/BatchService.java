package com.vidyapp.services;

import com.vidyapp.dtos.BatchDTO;
import com.vidyapp.dtos.BatchListDTO;

import java.util.List;

public interface BatchService {
    BatchDTO create(String name, String description);
    BatchDTO update(int batchId, String name, String description, boolean active);
    void softDelete(int batchId, String name);
    void delete(int batchId);
    List<BatchListDTO> getAll();
}
