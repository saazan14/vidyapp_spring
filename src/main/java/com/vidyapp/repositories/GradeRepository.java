package com.vidyapp.repositories;

import com.vidyapp.models.Batch;
import com.vidyapp.models.Grade;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Integer> {
    List<Grade> findAllByBatch(Batch batch);
}
