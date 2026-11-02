package com.varshini.courseprogress.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(length = 40)
    private String category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private AppUser user;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<Topic> topics = new ArrayList<>();

    public Course() {}

    public Course(String title, String description, String category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }

    public void addTopic(Topic topic) {
        topics.add(topic);
        topic.setCourse(this);
    }

    public void removeTopic(Topic topic) {
        topics.remove(topic);
        topic.setCourse(null);
    }

    public long getCompletedCount() {
        return topics.stream().filter(Topic::isCompleted).count();
    }

    public int getProgress() {
        if (topics.isEmpty()) return 0;
        return (int) Math.round(getCompletedCount() * 100.0 / topics.size());
    }

    public long getPendingCount() {
        return topics.size() - getCompletedCount();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }

    public List<Topic> getTopics() { return topics; }
    public void setTopics(List<Topic> topics) { this.topics = topics; }
}