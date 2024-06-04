package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Queue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QueueRepository extends JpaRepository<Queue, String> {
}
