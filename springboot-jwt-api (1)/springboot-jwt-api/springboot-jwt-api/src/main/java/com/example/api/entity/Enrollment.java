// src/main/java/com/example/api/entity/Enrollment.java
package com.example.api.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "enrollments")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Enrollment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @Column(name = "student_name", nullable = false, length = 100)
    private String studentName;

    @Column(name = "student_last_name", nullable = false, length = 100)
    private String studentLastName;

    @Column(name = "student_age", nullable = false)
    private Integer studentAge;

    @Column(name = "parent_name", nullable = false, length = 100)
    private String parentName;

    @Column(name = "parent_dui", nullable = false, unique = true, length = 10)
    private String parentDui;

    @Column(nullable = false, length = 100)
    private String email;

    @Column(nullable = false, length = 15)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String message;

    // ✅ NUEVO CAMPO: sede
    @Column(name = "branch", nullable = false, length = 50)
    private String branch;

    @Column(name = "enrollment_date", nullable = false)
    private LocalDateTime enrollmentDate = LocalDateTime.now();

    // Constructores
    public Enrollment() {}

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Course getCourse() { return course; }
    public void setCourse(Course course) { this.course = course; }

    public String getStudentName() { return studentName; }
    public void setStudentName(String studentName) { this.studentName = studentName; }

    public String getStudentLastName() { return studentLastName; }
    public void setStudentLastName(String studentLastName) { this.studentLastName = studentLastName; }

    public Integer getStudentAge() { return studentAge; }
    public void setStudentAge(Integer studentAge) { this.studentAge = studentAge; }

    public String getParentName() { return parentName; }
    public void setParentName(String parentName) { this.parentName = parentName; }

    public String getParentDui() { return parentDui; }
    public void setParentDui(String parentDui) { this.parentDui = parentDui; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getBranch() { return branch; }
    public void setBranch(String branch) { this.branch = branch; }

    public LocalDateTime getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDateTime enrollmentDate) { this.enrollmentDate = enrollmentDate; }
}