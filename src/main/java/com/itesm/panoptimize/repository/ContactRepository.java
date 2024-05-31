package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Contact} instances.
 * Provides management operations for contacts including CRUD operations and custom queries.
 */
@Repository
public interface ContactRepository extends JpaRepository<Contact, String> {
    @Query("SELECT c.satisfaction, COUNT(c) FROM Contact c GROUP BY c.satisfaction ORDER BY c.satisfaction")
    List<Object[]> countBySatisfaction();
}