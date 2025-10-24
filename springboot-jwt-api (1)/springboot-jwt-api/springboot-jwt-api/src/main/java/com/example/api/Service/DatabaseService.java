package com.example.api.Service;

import com.example.api.entity.User;
import com.example.api.entity.Course;
import com.example.api.repository.UserRepository;
import com.example.api.repository.CourseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class DatabaseService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    private CourseRepository courseRepo;

    @Transactional
    public User createUser(User user) {
        System.out.println("Guardando usuario en servicio: " + user.getUsername());
        User savedUser = userRepo.save(user);
        System.out.println("Usuario guardado con ID: " + savedUser.getId());
        return savedUser;
    }

    @Transactional
    public Course createCourse(Course course) {
        System.out.println("Guardando curso en servicio: " + course.getTitle());
        Course savedCourse = courseRepo.save(course);
        System.out.println("Curso guardado con ID: " + savedCourse.getId());
        return savedCourse;
    }

    public long getUserCount() {
        return userRepo.count();
    }

    public long getCourseCount() {
        return courseRepo.count();
    }
}