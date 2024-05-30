package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FeedbackRepository extends JpaRepository<Note, Integer> {
}
