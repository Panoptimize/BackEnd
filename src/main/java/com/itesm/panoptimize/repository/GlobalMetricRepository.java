package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.GlobalMetric;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Interface that extends JpaRepository for {@link GlobalMetric} instances.
 */
@Repository
public interface GlobalMetricRepository extends JpaRepository<GlobalMetric, Integer> {

    /**
     * Find a global metric by its description.
     * @param description the description of the metric.
     * @return an Optional of the global metric.
     */
    Optional<GlobalMetric> findByMetricDescription(String description);

    /**
     * Find all global metrics.
     * @param pageable the pageable object.
     * @return a page of global metrics.
     */
    @NotNull Page<GlobalMetric> findAll(@NotNull Pageable pageable);
}
