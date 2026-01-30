package com.vidyapp.dtos;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class BatchListDTO {
    private int batchId;
    private String name;
    private String description;
}