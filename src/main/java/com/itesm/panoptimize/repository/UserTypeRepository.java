package com.itesm.panoptimize.repository;

import com.itesm.panoptimize.model.UserType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserTypeRepository extends JpaRepository<UserType, Integer> {

    /**
     * Get the user type by name
     * @param typeName the user type with the associated name
     * @return UserType object
     */
    UserType typeName(String typeName);
}
