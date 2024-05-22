package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.ContactMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Interface that extends JpaRepository for {@link ContactMetric} entity.
 */
public interface ContactMetricRepository extends JpaRepository<ContactMetric, Integer>{
}