package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.Instance;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstanceRepository extends JpaRepository<Instance, Long>{
}
