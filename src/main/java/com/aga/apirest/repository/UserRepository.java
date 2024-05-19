package com.aga.apirest.repository;

import com.aga.apirest.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u FROM User u WHERE u.email = :email")
    User findByEmail(@Param("email") String email);

    @Query("SELECT u FROM User u WHERE u.nick = :nick")
    User findByNick(@Param("nick") String nick);

    @Query("SELECT u FROM User u WHERE u.nick = :x or u.email = :x")
    User findByNickOrEmail(@Param("x") String x);

    @Query("SELECT u FROM User u WHERE u.nick LIKE CONCAT('%', :x, '%') OR u.email LIKE CONCAT('%', :x, '%')")
    List<User> findByNickOrEmailInclude(@Param("x") String x);
}
