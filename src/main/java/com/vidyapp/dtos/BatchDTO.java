package com.vidyapp.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BatchDTO {
    private int batchId;
    private String name;
    private String description;
    private boolean active;
    @JsonIgnore
    private LocalDateTime createdDate;
    @JsonIgnore
    private LocalDateTime updatedDate;
}
