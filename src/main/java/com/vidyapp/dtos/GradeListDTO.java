package com.vidyapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GradeListDTO {
    private int gradeId;
    private String name;
    private String description;
    private String section;
    @JsonIgnore
    private int batchId;
    @JsonIgnore
    private boolean active=true;
    @JsonIgnore
    private LocalDateTime createdDate;
    @JsonIgnore
    private LocalDateTime updatedDate;
}
