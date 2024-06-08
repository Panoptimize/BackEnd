package com.itesm.panoptimize.controller;

import com.itesm.panoptimize.dto.download.DownloadDTO;
import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.note.UpdateNoteDTO;
import com.itesm.panoptimize.service.NoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Read a note", description = "This GET request call serves the purpose of returning a note with an id." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Note found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Note not found.",
                    content = @Content),
    })
    @GetMapping("/{id}")
    public ResponseEntity<NoteDTO> getNote(@PathVariable Integer id){
        NoteDTO noteDTO = noteService.getNote(id);
        if (noteDTO == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(noteDTO);
    }

    @Operation(summary = "Read notes", description = "This GET request call serves the purpose of returning all the notes storaged." )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Notes found.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Notes not found.",
                    content = @Content),
    })
    @GetMapping("/")
    public ResponseEntity<Page<NoteDTO>> getNotes(Pageable pageable){
        return ResponseEntity.ok(noteService.getNotes(pageable));
    }

    @Operation(summary = "Create a new note",
            description = "This POST request call creates a new note.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201",
                    description = "Item created successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "400",
                    description = "Bad request.",
                    content = @Content)
    })
    @PostMapping("/")
    public ResponseEntity<NoteDTO> createNote(@RequestBody CreateNoteDTO noteDTO){
        return ResponseEntity.ok(noteService.createNote(noteDTO));
    }

    @Operation(summary = "Update an existing note",
            description = "This PUT request call updates an existing note.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Note updated successfully.",
                    content = {
                            @Content(mediaType = "application/json",
                                    schema = @Schema(implementation = NoteDTO.class))
                    }),
            @ApiResponse(responseCode = "404",
                    description = "Note not found.",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<NoteDTO> updateNote(@PathVariable Integer id, @RequestBody UpdateNoteDTO noteDTO){
        NoteDTO updatedNote = noteService.updateNote(id, noteDTO);
        if (updatedNote == null){
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updatedNote);
    }

    @Operation(summary = "Delete a note by ID",
            description = "This DELETE request call deletes a note by its ID.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204",
                    description = "Note deleted successfully."),
            @ApiResponse(responseCode = "404",
                    description = "Note not found.",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteNote(@PathVariable Integer id) {
        noteService.deleteNote(id);
        return ResponseEntity.noContent().build();
    }
}
