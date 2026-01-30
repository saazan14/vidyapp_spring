package com.vidyapp.services;

import com.vidyapp.dtos.NoteDTO;
import com.vidyapp.models.Note;

import java.util.List;

public interface NoteService {
    NoteDTO createNoteForUser(String username, String content);

    NoteDTO updateNoteForUser(Long noteId, String username, String content);

    void deleteNoteForUser(Long noteId, String username);

    List<NoteDTO> getNotesForUser(String username);
}