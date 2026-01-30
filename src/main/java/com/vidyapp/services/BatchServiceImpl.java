package com.vidyapp.services;

import com.vidyapp.dtos.BatchDTO;
import com.vidyapp.dtos.BatchListDTO;
import com.vidyapp.models.Batch;
import com.vidyapp.repositories.BatchRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private final BatchRepository batchRepository;
    private final ModelMapper modelMapper;

    @Override
    public BatchDTO create(String name, String description) {
        Batch batch = new Batch();
        batch.setName(name);
        batch.setDescription(description);
        return mapToDTO(batchRepository.save(batch));
    }

    @Override
    public BatchDTO update(int batchId, String name, String description, boolean active) {
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch Not Found"));

        batch.setName(name);
        batch.setDescription(description);
        batch.setActive(active);
        Batch savedBatch = batchRepository.save(batch);
        return modelMapper.map(savedBatch, BatchDTO.class);
    }

    @Override
    public void softDelete(int batchId, String name) {
        // 1. Find the batch or throw exception
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch with ID " + batchId + " not found"));

        // 2. Validate that the name matches (Case-insensitive check is usually safer)
        if (!batch.getName().equalsIgnoreCase(name)) {
            throw new IllegalArgumentException("Delete failed: Provided name does not match the Batch ID.");
        }

        // 3. Perform Soft Delete
        batch.setActive(false);
        batchRepository.save(batch);
    }

    @Override
    public void delete(int batchId) {
        // 1. Find the batch or throw exception
        Batch batch = batchRepository.findById(batchId)
                .orElseThrow(() -> new RuntimeException("Batch with ID " + batchId + " not found"));
        batchRepository.delete(batch);
    }

    @Override
    public List<BatchListDTO> getAll() {
        return batchRepository.findAll().stream()
                .filter(Batch::isActive)
                .map(batch -> modelMapper.map(batch, BatchListDTO.class)) // Explicitly map to the List DTO
                .collect(Collectors.toList());
    }

    private Batch findBatchById(int id) {
        return batchRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Batch with ID " + id + " not found"));
    }

    private BatchDTO mapToDTO(Batch batch) {
        return modelMapper.map(batch, BatchDTO.class);
    }
}
