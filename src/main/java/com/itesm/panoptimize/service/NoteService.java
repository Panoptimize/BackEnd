package com.itesm.panoptimize.service;

import com.itesm.panoptimize.dto.agent_performance.Agent_PerformanceDTO;
import com.itesm.panoptimize.dto.agent_performance.CreateAgentPerformanceWithNote;
import com.itesm.panoptimize.dto.note.CreateNoteDTO;
import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.dto.note.UpdateNoteDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import com.itesm.panoptimize.model.Note;
import com.itesm.panoptimize.model.User;
import com.itesm.panoptimize.repository.AgentPerformanceRepository;
import com.itesm.panoptimize.repository.NoteRepository;
import com.itesm.panoptimize.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class NoteService {

    private final NoteRepository noteRepository;
    private final AgentPerformanceRepository agentPerformanceRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    private NoteDTO convertToNoteDTO(Note note) {
        return modelMapper.map(note, NoteDTO.class);
    }

    private Note convertToNoteEntity(CreateNoteDTO noteDTO) {
        return modelMapper.map(noteDTO, Note.class);
    }

    public NoteService(NoteRepository noteRepository, ModelMapper modelMapper, AgentPerformanceRepository agentPerformanceRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.modelMapper = modelMapper;
        this.agentPerformanceRepository = agentPerformanceRepository;
        this.userRepository = userRepository;
    }

    public NoteDTO getNote(Integer id) {
        return convertToNoteDTO(noteRepository.findById(id).orElse(null));
    }

    public Page<NoteDTO> getNotes(Pageable pageable) {
        return noteRepository.findAll(pageable).map(this::convertToNoteDTO);
    }

    public void deleteNote(Integer id) {
        noteRepository.deleteById(id);
    }

    public NoteDTO createNote(CreateNoteDTO noteDTO) {
        return convertToNoteDTO(noteRepository.save(convertToNoteEntity(noteDTO)));
    }

    public NoteDTO updateNote(Integer id, UpdateNoteDTO noteDTO) {
        Note note = noteRepository.findById(id).orElse(null);
        if (note == null) {
            return null;
        }
        if (noteDTO.getName() != null) {
            note.setName(noteDTO.getName());
        }
        if (noteDTO.getDescription() != null) {
            note.setDescription(noteDTO.getDescription());
        }
        if (noteDTO.getPriority() != null) {
            note.setPriority(noteDTO.getPriority());
        }
        if (noteDTO.getAgentPerformanceId() != null) {
            AgentPerformance agentPerformance = agentPerformanceRepository.findById(noteDTO.getAgentPerformanceId()).orElse(null);
            if (agentPerformance == null) {
                return null;
            }
            note.setAgentPerformance(agentPerformance);
        }

        return convertToNoteDTO(noteRepository.save(note));
    }

    public Page<NoteDTO> getAgentNotes(Pageable pageable, Integer agentId) {
        return noteRepository.getNotesByAgent(agentId, pageable);
    }

    @Transactional
    public NoteDTO createNoteWithAgentPerformance(CreateAgentPerformanceWithNote createAgentPerformanceWithNote) {
        AgentPerformance agentPerformance = modelMapper.map(createAgentPerformanceWithNote, AgentPerformance.class);
        User agent = userRepository.findById(createAgentPerformanceWithNote.getCreateAgent_PerformanceDTO().getAgentId())
                .orElse(null);
        agentPerformance.setAgent(agent);
        agentPerformance = agentPerformanceRepository.save(agentPerformance);

        Note note = convertToNoteEntity(createAgentPerformanceWithNote.getCreateNoteDTO());
        note.setAgentPerformance(agentPerformance);
        note = noteRepository.save(note);

        return convertToNoteDTO(note);
    }
}
