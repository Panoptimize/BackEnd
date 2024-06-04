package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {
    @Query(value = "SELECT ap FROM AgentPerformance ap JOIN Note n ON ap.id = n.agentPerformance.id WHERE n.id = :noteId")
    AgentPerformance findAgentPerformanceByNoteId(@Param("noteId") Integer noteId);
}
