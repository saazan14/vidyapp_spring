package com.vidyapp.controllers;

import com.vidyapp.dtos.NoteDTO;
import com.vidyapp.models.Note;
import com.vidyapp.services.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600, allowCredentials="true")
public class NoteController {
    private final NoteService noteService;

    // Constructor-based dependency injection (recommended)
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public NoteDTO createNote(@RequestBody NoteDTO noteDTO,
                              @AuthenticationPrincipal UserDetails userDetails) {
        return noteService.createNoteForUser(userDetails.getUsername(), noteDTO.getContent());
    }

    @GetMapping
    public List<NoteDTO> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        return noteService.getNotesForUser(userDetails.getUsername());
    }

    @PutMapping("/{noteId}")
    public NoteDTO updateNote(@PathVariable Long noteId,
                              @AuthenticationPrincipal UserDetails userDetails,
                              @RequestBody NoteDTO noteDTO) {
        return noteService.updateNoteForUser(noteId, userDetails.getUsername(), noteDTO.getContent());
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        noteService.deleteNoteForUser(noteId, userDetails.getUsername());
    }
}