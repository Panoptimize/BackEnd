package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Call;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CallRepository extends JpaRepository<Call, Long> {
    @Query("SELECT MONTH(c.startTime) as month, COUNT(c) FROM Call c GROUP BY MONTH(c.startTime)")
    List<Object[]> countMonthlyCalls();
}