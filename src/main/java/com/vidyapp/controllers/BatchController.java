package com.vidyapp.controllers;

import com.vidyapp.dtos.BatchDTO;
import com.vidyapp.dtos.BatchListDTO;
import com.vidyapp.services.BatchService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/batches")
@RequiredArgsConstructor
public class BatchController {

    private final BatchService batchService;

    @PostMapping
    public ResponseEntity<BatchDTO> createBatch(@Valid @RequestBody BatchDTO batchDTO) {
        BatchDTO createdBatch = batchService.create(batchDTO.getName(), batchDTO.getDescription());
        return new ResponseEntity<>(createdBatch, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<BatchListDTO>> getBatches() {
        List<BatchListDTO> batches = batchService.getAll();
        return ResponseEntity.ok(batches);
    }

    @PutMapping("/{batchId}")
    public ResponseEntity<BatchDTO> updateBatch(
            @PathVariable int batchId,
            @Valid @RequestBody BatchDTO batchDTO) {

        BatchDTO updatedBatch = batchService.update(
                batchId,
                batchDTO.getName(),
                batchDTO.getDescription(),
                batchDTO.isActive()
        );
        return ResponseEntity.ok(updatedBatch);
    }

    @DeleteMapping("/{batchId}")
    public ResponseEntity<Void> deleteBatch(
            @PathVariable int batchId,
            @RequestParam String name) {

        batchService.delete(batchId, name);

        // Returns 204 No Content
        return ResponseEntity.noContent().build();
    }
}