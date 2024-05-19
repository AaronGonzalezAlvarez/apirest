package com.aga.apirest.repository;

import com.aga.apirest.models.Activity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Integer> {

    @Query("SELECT a FROM Activity a WHERE a.name LIKE CONCAT('%', :x, '%') ORDER BY a.id DESC")
    List<Activity> filterActivityForNameConcat(@Param("x") String x);

    @Query("SELECT a FROM Activity a WHERE a.hourlyPrice = 0 and a.activityprice = 0 ORDER BY a.id DESC")
    List<Activity> filterActivityFree();

    @Query("SELECT a FROM Activity a WHERE a.province = :x ORDER BY a.id DESC")
    List<Activity> filterActivityForPronvince(@Param("x") String x);

    @Query("SELECT a FROM Activity a WHERE a.date BETWEEN :date1 AND :date2 ORDER BY a.id DESC")
    List<Activity> filterActivityForDate(@Param("date1") LocalDate date1, @Param("date2") LocalDate date2);

    @Query("SELECT a FROM Activity a ORDER BY a.id DESC")
    List<Activity> getActivityOrderDesc();
}
