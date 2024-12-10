package com.example.lab6.repositories;

import com.example.lab6.models.Task;
import com.example.lab6.models.UserModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByUser(UserModel user);
    Page<Task> findByUser(UserModel user, Pageable pageable);
    @Query("SELECT t FROM Task t WHERE t.title LIKE %:keyword% AND t.user = :user")
    List<Task> searchTasksByKeyword(@Param("keyword") String keyword, @Param("user") UserModel user);
    @Query("SELECT t FROM Task t WHERE t.category.id = :categoryId AND t.user = :user")
    List<Task> filterTasksByCategory(@Param("categoryId") Long categoryId, @Param("user") UserModel user);
}