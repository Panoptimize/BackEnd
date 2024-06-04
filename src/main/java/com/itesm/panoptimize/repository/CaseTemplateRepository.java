package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.CaseTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link CaseTemplate} instances.
 */
@Repository
public interface CaseTemplateRepository extends JpaRepository<CaseTemplate, Integer> {
}
