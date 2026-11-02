package com.varshini.courseprogress.repository;

import com.varshini.courseprogress.model.Course;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    @EntityGraph(attributePaths = "topics")
    List<Course> findAllByUserEmail(String email);

    @EntityGraph(attributePaths = "topics")
    Optional<Course> findByIdAndUserEmail(Long id, String email);
}