package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {

    @Query("SELECT ap FROM AgentPerformance ap WHERE ap.createdAt BETWEEN :startDate AND :endDate")
    List<AgentPerformance> findPerformancesBetweenDates(Instant startDate, Instant endDate);
}
