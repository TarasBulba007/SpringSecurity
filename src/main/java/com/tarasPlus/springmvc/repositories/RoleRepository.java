package com.tarasPlus.springmvc.repositories;

import com.tarasPlus.springmvc.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {

    @Query("select role from Role role where role.role=:role")
    Role findByRole(@Param("role") String role);

    @Query("select b from Role b where b.id=:id")
    Role findById(@Param("id")int id);
}