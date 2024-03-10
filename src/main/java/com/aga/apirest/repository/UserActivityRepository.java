package com.aga.apirest.repository;

import com.aga.apirest.models.UserActivity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActivityRepository extends JpaRepository<UserActivity, Integer> {
}
