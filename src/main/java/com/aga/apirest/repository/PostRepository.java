package com.aga.apirest.repository;

import com.aga.apirest.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {

    @Query("SELECT p FROM Post p WHERE p.user.email = :email order by p.date,p.id DESC")
    List<Post> findPostsByUserEmail(@Param("email") String email);

    @Query("SELECT p FROM Post p order by p.date,p.id DESC")
    List<Post> orderDatePosts();
}
