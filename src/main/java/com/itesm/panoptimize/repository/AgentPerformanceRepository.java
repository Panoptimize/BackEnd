package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentPerformance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AgentPerformanceRepository extends JpaRepository<AgentPerformance, Long> {

}
