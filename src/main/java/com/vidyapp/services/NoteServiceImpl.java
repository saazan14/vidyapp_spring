package com.vidyapp.services;

import com.vidyapp.dtos.NoteDTO;
import com.vidyapp.models.Note;
import com.vidyapp.repositories.NoteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final ModelMapper modelMapper;

    public NoteServiceImpl(NoteRepository noteRepository, ModelMapper modelMapper) {
        this.noteRepository = noteRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public NoteDTO createNoteForUser(String username, String content) { // Changed return type to NoteDTO
        Note note = new Note();
        note.setContent(content);
        note.setOwnerUserName(username);
        Note savedNote = noteRepository.save(note);
        return modelMapper.map(savedNote, NoteDTO.class);
    }

    @Override
    public NoteDTO updateNoteForUser(Long noteId, String username, String content) { // Changed return type to NoteDTO
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getOwnerUserName().equals(username)) {
            throw new RuntimeException("Unauthorized to update this note");
        }

        note.setContent(content);
        Note updatedNote = noteRepository.save(note);
        return modelMapper.map(updatedNote, NoteDTO.class);
    }

    @Override
    public void deleteNoteForUser(Long noteId, String username) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getOwnerUserName().equals(username)) {
            throw new RuntimeException("Unauthorized to delete this note");
        }

        noteRepository.delete(note);
    }

    @Override
    public List<NoteDTO> getNotesForUser(String username) {
        List<Note> notes = noteRepository.findByOwnerUserName(username);
        return notes.stream()
                .map(note -> modelMapper.map(note, NoteDTO.class))
                .collect(Collectors.toList());
    }
}