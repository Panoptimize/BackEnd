package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.AgentsHaveUsers;
import com.itesm.panoptimize.model.AgentsHaveUsersId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgentsHaveUsersRepository extends JpaRepository<AgentsHaveUsers, AgentsHaveUsersId> {
}
