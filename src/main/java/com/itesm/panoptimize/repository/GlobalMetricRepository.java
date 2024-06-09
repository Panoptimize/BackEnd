package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.GlobalMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GlobalMetricRepository extends JpaRepository<GlobalMetric, Integer> {
}
