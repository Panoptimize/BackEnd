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
public interface ContactRepository extends JpaRepository<Contact, Long> {
    /**
     * Custom query to count contacts grouped by the month of their start time.
     * @return a list of object arrays where each array contains the month number and the count of contacts for that month
     */
    @Query(value = "SELECT MONTH(c.start_time) as month, COUNT(*) FROM contact c GROUP BY MONTH(c.start_time)", nativeQuery = true)
    List<Object[]> countMonthlyContacts();
}