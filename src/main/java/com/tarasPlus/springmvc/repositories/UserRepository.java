package com.tarasPlus.springmvc.repositories;

import com.tarasPlus.springmvc.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("select user from User user left join fetch user.roles where user.username=:username")
    User findByLogin(@Param("username") String username);

    @Query("select user from User user left join fetch user.roles where user.id=:id")
    User getById(@Param("id") Integer id);

    @Query("from User as user")
    List<User> findAll();
}