package com.example.demo.model;
import jakarta.persistence.*;

import java.util.List;


@Entity
public class Quiz {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String title;

    @ManyToMany
    @JoinTable(
            name = "quiz_questions",
            joinColumns = @JoinColumn(name = "quiz_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "questions_id", referencedColumnName = "id")
    )
    private List<Question> questions;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(List<Question> questions) {
        this.questions = questions;
    }
}
