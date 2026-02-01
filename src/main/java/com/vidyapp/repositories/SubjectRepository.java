package com.vidyapp.repositories;

import com.vidyapp.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Integer> {
    // Find all active subjects belonging to a specific Grade ID
    List<Subject> findAllByGrade_GradeIdAndActiveTrue(int gradeId);
}
