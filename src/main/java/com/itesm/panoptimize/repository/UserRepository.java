package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    /**
     * Get all agents using pagination
     * @return List of agents
     */
    @Query("SELECT u FROM User u INNER JOIN u.userType ut WHERE ut.typeName = :type")
    List<User> getUsersByType(String type, Pageable pageable);
}
