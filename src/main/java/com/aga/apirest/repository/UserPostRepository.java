package com.aga.apirest.repository;

import com.aga.apirest.models.Post;
import com.aga.apirest.models.User;
import com.aga.apirest.models.UserPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface UserPostRepository  extends JpaRepository<UserPost, Integer> {

    @Query("DELETE FROM UserPost up WHERE up.creator = :user and up.post = :post")
    @Modifying
    @Transactional
    void deleteByUserPost(@Param("user") User user,@Param("post") Post post);
}
