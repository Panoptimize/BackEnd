package com.itesm.panoptimize.repository;


import com.itesm.panoptimize.dto.note.NoteDTO;
import com.itesm.panoptimize.model.Note;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface NoteRepository extends JpaRepository<Note, Integer> {
    @Query(value = "SELECT n FROM Note n JOIN n.agentPerformance ap WHERE ap.agent.id = :agentId")
    Page<NoteDTO> getNotesByAgent(@Param("agentId") Integer agentId, Pageable pageable);
}
