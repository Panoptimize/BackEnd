package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.sql.Date;

/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {
    @Query(value = "SELECT ap FROM AgentPerformance ap JOIN Note n ON ap.id = n.agentPerformance.id WHERE n.id = :noteId")
    AgentPerformance findAgentPerformanceByNoteId(@Param("noteId") Integer noteId);

    // Idealmente con info de este query: AVG(ap.avgAfterContactWorkTime) as avgAfterContactWorkTime, AVG(ap.avgHandleTime) as avgHandleTime,  AVG(ap.avgAbandonTime) as avgAbandonTime, AVG(ap.avgHoldTime) as avgHoldTime
    //ap.id, ap.createdAt,  AVG(ap.avgAfterContactWorkTime) as avgAfterContactWorkTime, AVG(ap.avgHandleTime) as avgHandleTime,   AVG(ap.avgAbandonTime) as avgAbandonTime, AVG(ap.avgHoldTime) as avgHoldTime, ap.agent.id
    @Query("SELECT ap FROM AgentPerformance ap WHERE DATE(ap.createdAt) = :dateToday AND ap.agent.id =:agentId and ap.avgHoldTime = 3")
    AgentPerformance findAgentMetricsByAgentId(@Param("dateToday") Date dateToday, @Param("agentId") Integer agentId);
}
