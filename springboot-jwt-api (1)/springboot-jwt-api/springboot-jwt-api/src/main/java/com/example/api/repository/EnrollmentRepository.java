package com.example.api.repository;

import com.example.api.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    List<Enrollment> findByCourseId(Long courseId);
    boolean existsByParentDui(String parentDui);
    List<Enrollment> findAllByOrderByEnrollmentDateDesc();

    boolean existsByParentDuiAndCourseId(String parentDui, Long courseId);
}