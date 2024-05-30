package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.note.UpdateNoteDTO;
import com.itesm.panoptimize.service.NoteService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/note")
public class NoteController {
    private final NoteService noteService;

    public NoteController(NoteService noteService){
        this.noteService = noteService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Integer id){
        NoteDTO noteDTO = noteService.getNote(id);
        if (noteDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(noteDTO);
    }

    @GetMapping("/")
    public ResponseEntity<Page<NoteDTO>> getNotes(Pageable pageable){
        return ResponseEntity.ok(noteService.getNotes(pageable));
    }

    @PostMapping("/")
    public ResponseEntity<NoteDTO> createNote(@RequestBody CreateNoteDTO noteDTO){
        return ResponseEntity.ok(noteService.createNote(noteDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Integer id, @RequestBody UpdateNoteDTO noteDTO){
        NoteDTO updatedNote = noteService.updateNote(id, noteDTO);
        if (updatedNote == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNote);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Integer id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}