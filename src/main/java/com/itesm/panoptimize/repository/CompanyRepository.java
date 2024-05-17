package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for {@link Company} instances.
 */
@Repository
public interface CompanyRepository extends JpaRepository<Company, Integer> {
}
