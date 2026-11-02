package com.varshini.courseprogress.controller;

import com.varshini.courseprogress.model.Course;
import com.varshini.courseprogress.service.CourseService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/")
    public String dashboard(Model model, Principal principal) {

        String email = principal.getName();
        var courses = courseService.findAll(email);

        long totalTopics = courses.stream()
                .mapToLong(c -> c.getTopics().size()).sum();

        long completedTopics = courses.stream()
                .mapToLong(Course::getCompletedCount).sum();

        long pendingTopics = totalTopics - completedTopics;

        int overallProgress = totalTopics == 0 ? 0 :
                (int) Math.round(completedTopics * 100.0 / totalTopics);

        model.addAttribute("courses", courses);
        model.addAttribute("totalCourses", courses.size());
        model.addAttribute("totalTopics", totalTopics);
        model.addAttribute("completedTopics", completedTopics);
        model.addAttribute("pendingTopics", pendingTopics);
        model.addAttribute("overallProgress", overallProgress);
        model.addAttribute("currentUser", email);

        return "dashboard";
    }

    @GetMapping("/courses/new")
    public String newCourse(Model model) {
        model.addAttribute("course", new Course());
        model.addAttribute("pageTitle", "Add a course");
        return "course-form";
    }

    @PostMapping("/courses")
    public String createCourse(
            @RequestParam String title,
            @RequestParam(required = false) String description,
            @RequestParam(required = false) String category,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        if (title == null || title.trim().isEmpty()) {
            redirectAttributes.addFlashAttribute(
                    "error", "Course name is required.");
            return "redirect:/courses/new";
        }

        courseService.save(
                new Course(title.trim(), description, category),
                principal.getName()
        );

        redirectAttributes.addFlashAttribute(
                "success", "Course added successfully.");

        return "redirect:/";
    }

    @GetMapping("/courses/{id}")
    public String courseDetails(
            @PathVariable Long id,
            Model model,
            Principal principal) {

        model.addAttribute(
                "course",
                courseService.findById(id, principal.getName())
        );

        return "course-details";
    }

    @PostMapping("/courses/{id}/topics")
    public String addTopic(
            @PathVariable Long id,
            @RequestParam String name,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        try {
            courseService.addTopic(id, name, principal.getName());
            redirectAttributes.addFlashAttribute("success", "Topic added.");
        } catch (IllegalArgumentException ex) {
            redirectAttributes.addFlashAttribute("error", ex.getMessage());
        }

        return "redirect:/courses/" + id;
    }

    @PostMapping("/topics/{id}/toggle")
    public String toggleTopic(
            @PathVariable Long id,
            @RequestParam Long courseId,
            Principal principal) {

        courseService.toggleTopic(id, principal.getName());

        return "redirect:/courses/" + courseId;
    }

    @PostMapping("/topics/{id}/delete")
    public String deleteTopic(
            @PathVariable Long id,
            @RequestParam Long courseId,
            Principal principal) {

        courseService.deleteTopic(id, principal.getName());

        return "redirect:/courses/" + courseId;
    }

    @PostMapping("/courses/{id}/delete")
    public String deleteCourse(
            @PathVariable Long id,
            Principal principal,
            RedirectAttributes redirectAttributes) {

        courseService.deleteCourse(id, principal.getName());

        redirectAttributes.addFlashAttribute("success", "Course deleted.");

        return "redirect:/";
    }
}