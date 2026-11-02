package com.varshini.courseprogress.service;

import com.varshini.courseprogress.model.AppUser;
import com.varshini.courseprogress.model.Course;
import com.varshini.courseprogress.model.Topic;
import com.varshini.courseprogress.repository.CourseRepository;
import com.varshini.courseprogress.repository.TopicRepository;
import com.varshini.courseprogress.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TopicRepository topicRepository;
    private final UserRepository userRepository;

    public CourseService(CourseRepository courseRepository,
                         TopicRepository topicRepository,
                         UserRepository userRepository) {
        this.courseRepository = courseRepository;
        this.topicRepository = topicRepository;
        this.userRepository = userRepository;
    }

    private AppUser getUser(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found."));
    }

    @Transactional(readOnly = true)
    public List<Course> findAll(String email) {
        return courseRepository.findAllByUserEmail(email);
    }

    @Transactional(readOnly = true)
    public Course findById(Long id, String email) {
        return courseRepository.findByIdAndUserEmail(id, email)
                .orElseThrow(() -> new IllegalArgumentException("Course not found."));
    }

    public Course save(Course course, String email) {
        course.setUser(getUser(email));
        return courseRepository.save(course);
    }

    public void deleteCourse(Long id, String email) {
        Course course = findById(id, email);
        courseRepository.delete(course);
    }

    @Transactional
    public void addTopic(Long courseId, String topicName, String email) {
        Course course = findById(courseId, email);

        if (topicName == null || topicName.trim().isEmpty()) {
            throw new IllegalArgumentException("Topic name cannot be empty.");
        }

        course.addTopic(new Topic(topicName.trim()));
        courseRepository.save(course);
    }

    @Transactional
    public void toggleTopic(Long topicId, String email) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found."));

        findById(topic.getCourse().getId(), email);

        topic.setCompleted(!topic.isCompleted());
        topicRepository.save(topic);
    }

    @Transactional
    public void deleteTopic(Long topicId, String email) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found."));

        findById(topic.getCourse().getId(), email);

        topicRepository.delete(topic);
    }
}