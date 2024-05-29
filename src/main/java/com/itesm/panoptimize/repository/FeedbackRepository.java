package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Notes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Notes, Integer> {
}
