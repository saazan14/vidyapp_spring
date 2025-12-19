package np.com.sajansubba.notes.controllers;

import np.com.sajansubba.notes.models.Note;
import np.com.sajansubba.notes.services.NoteService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notes")
public class NoteController {
    private final NoteService noteService;

    // Constructor-based dependency injection (recommended)
    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping
    public Note createNote(@RequestBody Note note,
                           @AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("username: " + username);
        return noteService.createNoteForUser(username, note.getContent());
    }

    @GetMapping
    public List<Note> getUserNotes(@AuthenticationPrincipal UserDetails userDetails) {
        String username = userDetails.getUsername();
        System.out.println("username: " + username);
        return noteService.getNotesForUser(username);
    }

    @PutMapping("/{noteId}")
    public Note updateNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails,
                           @RequestBody Note note) {
        return noteService.updateNoteForUser(noteId, userDetails.getUsername(), note.getContent());
    }

    @DeleteMapping("/{noteId}")
    public void deleteNote(@PathVariable Long noteId,
                           @AuthenticationPrincipal UserDetails userDetails) {
        noteService.deleteNoteForUser(noteId, userDetails.getUsername());
    }
}