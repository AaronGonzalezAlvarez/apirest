package com.aga.apirest.repository;

import com.aga.apirest.models.Message;
import com.aga.apirest.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Integer> {

    @Query("SELECT m FROM Message m WHERE m.emitter.email = :email or m.addressee.email = :email order by m.id DESC")
    List<Message> messageForUser(@Param("email") String email);
}
