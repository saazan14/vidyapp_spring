package com.vidyapp.repositories;

import com.vidyapp.models.Batch;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BatchRepository extends JpaRepository<Batch,Integer> {
}
