package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.SatisfactionLevel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SatisfactionLevelRepository extends JpaRepository<SatisfactionLevel, Integer> {
}
