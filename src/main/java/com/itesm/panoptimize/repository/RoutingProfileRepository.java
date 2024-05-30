package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.RoutingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoutingProfileRepository extends JpaRepository<RoutingProfile, String> {
}
