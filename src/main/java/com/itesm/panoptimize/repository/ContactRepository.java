package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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

    /**
     * Custom query to count how many FCR a given agent has.
     * @param agentId the id of the agent
     * @return an int that denotes the total count of FCR contacts that agent has.
     */
    @Query(value = "select COUNT(c) from Contact c join c.contactMetrics cm where c.agent = :agentId " +
            "and cm.firstContactResolution = true")
    int countFCRByAgent(@Param("agentId") int agentId);

    /**
     * Custom query to get the contacts from an agent prior to the current contact
     * ordered in descending order by their id
     * @param agentId the id of the agent
     * @param contactId the id of the current contact
     * @return a list of all contacts
     */
    @Query(value = "select c from Contact c where c.agent = :agentId and c.id <= :contactId order by c.id DESC")
    List<Contact> findPreviousContactIdsFromAgent(@Param("agentId") int agentId, @Param("contactId") int contactId);

    /**
     * Custom query that returns the average after_call_work_time value for all of an agent's contacts
     * @param agentId the id of the agent
     * @return the average value of the agent's after_call_work_time
     */
    @Query(value = "select avg(cm.afterCallWorkTime) from Contact c join c.contactMetrics cm where c.agent = :agentId")
    Double avgAfterCallWorkTime(@Param("agentId") int agentId);
}