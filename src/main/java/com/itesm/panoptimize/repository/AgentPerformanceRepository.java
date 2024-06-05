package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.Instant;

/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {
    @Query(value = "SELECT ap FROM AgentPerformance ap JOIN Note n ON ap.id = n.agentPerformance.id WHERE n.id = :noteId")
    AgentPerformance findAgentPerformanceByNoteId(@Param("noteId") Integer noteId);

    @Query("SELECT AVG(ap.avgAbandonTime) as avg_abandon_time, AVG(ap.avgAfterContactWorkTime) as avg_after_contact_work_time, AVG(ap.avgHandleTime) as avg_handle_time, AVG(ap.avgHoldTime) as avg_hold_time FROM AgentPerformance ap WHERE DATE(ap.createdAt) = :dateToday AND ap.agent.id =:agentId")
    AgentPerformance findAgentMetricsByAgentId(String dateToday, Integer agentId);
}
