package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import jakarta.persistence.PrePersist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

/**
 * Repository interface for {@link AgentPerformance} instances.
 */
@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Integer> {
    List<AgentPerformance> findByPerformanceDateBetween(LocalDate startDate, LocalDate endDate);
}
