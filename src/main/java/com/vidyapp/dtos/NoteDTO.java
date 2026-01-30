package com.vidyapp.dtos;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private Long id;
    private String content;
    private String ownerUserName;
}
