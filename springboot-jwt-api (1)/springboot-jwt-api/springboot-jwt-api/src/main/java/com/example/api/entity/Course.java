// 📄 src/main/java/com/example/api/entity/Course.java
package com.example.api.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "courses")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String image;

    @Column(name = "short_description", length = 500)
    private String shortDescription;

    @Column(nullable = false)
    private Integer capacity = 20;

    @Column(nullable = false)
    private Integer enrolled = 0;

    @Column(columnDefinition = "TEXT")
    private String info;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Enrollment> enrollments = new ArrayList<>();

    public Course() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getImage() { return image; }
    public void setImage(String image) { this.image = image; }

    public String getShortDescription() { return shortDescription; }
    public void setShortDescription(String shortDescription) { this.shortDescription = shortDescription; }

    public Integer getCapacity() { return capacity; }
    public void setCapacity(Integer capacity) { this.capacity = capacity; }

    public Integer getEnrolled() { return enrolled; }
    public void setEnrolled(Integer enrolled) { this.enrolled = enrolled; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public List<Enrollment> getEnrollments() { return enrollments; }
    public void setEnrollments(List<Enrollment> enrollments) { this.enrollments = enrollments; }
}