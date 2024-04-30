package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Contact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ContactRepository extends JpaRepository<Contact, Long> {
    @Query(value = "SELECT MONTH(c.start_time) as month, COUNT(*) FROM contact c GROUP BY MONTH(c.start_time)", nativeQuery = true)
    List<Object[]> countMonthlyContacts();
}