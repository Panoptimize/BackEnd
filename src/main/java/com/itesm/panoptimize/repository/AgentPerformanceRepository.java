package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.dto.agent_performance.AgentPerformanceQueryDTO;
import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.sql.Date;

import java.time.Instant;
import java.util.List;


/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {

    @Query(value = "SELECT ap FROM AgentPerformance ap JOIN Note n ON ap.id = n.agentPerformance.id WHERE n.id = :noteId")
    AgentPerformance findAgentPerformanceByNoteId(@Param("noteId") Integer noteId);


    /* Idealmente con info de este query: AVG(ap.avgAfterContactWorkTime) as avgAfterContactWorkTime, AVG(ap.avgHandleTime) as avgHandleTime,  AVG(ap.avgAbandonTime) as avgAbandonTime, AVG(ap.avgHoldTime) as avgHoldTime
    ap.id, ap.createdAt,  AVG(ap.avgAfterContactWorkTime) as avgAfterContactWorkTime, AVG(ap.avgHandleTime) as avgHandleTime,   AVG(ap.avgAbandonTime) as avgAbandonTime, AVG(ap.avgHoldTime) as avgHoldTime, ap.agent.id*/
    @Query("SELECT AVG(ap.avgAfterContactWorkTime) avgAfterContactWorkTime, \n" +
            "\tAVG(ap.avgHandleTime) avgHandleTime, \n" +
            "    AVG(ap.avgAbandonTime) avgAbandonTime, \n" +
            "    AVG(ap.avgHoldTime) avgHoldTime  " +
            "FROM AgentPerformance ap WHERE DATE(ap.createdAt) = DATE(NOW()) " +
            "AND ap.agent.id =:agentId")
    AgentPerformanceQueryDTO findAgentMetricsByAgentId(@Param("agentId") Integer agentId);

    @Query("SELECT ap FROM AgentPerformance ap JOIN ap.agent u WHERE ap.createdAt BETWEEN :startDate AND :endDate AND u.routingProfile.routingProfileId IN :routingProfileIds")
    List<AgentPerformance> findPerformancesBetweenDates(Instant startDate, Instant endDate, List<String> routingProfileIds);

}
