package com.vidyapp.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class SubjectDTO {
    private int subjectId;
    private String name;
    private String code;
    private String description;
    private int gradeId;
    private boolean active;
    private short optional = 0;
    private LocalTime startTime;
    private LocalTime endTime;
}