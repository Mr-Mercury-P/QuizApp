package com.example.demo.service;


import com.example.demo.dao.QuestionDao;
import com.example.demo.dao.QuizDao;
import com.example.demo.model.Question;
import com.example.demo.model.QuestionWrapper;
import com.example.demo.model.Quiz;
import com.example.demo.model.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuizService {
    @Autowired
    QuizDao quizDao;
    @Autowired
    QuestionDao questionDao;
    public ResponseEntity<String> createQuiz(String category, int numQ, String title) {
        List<Question> questions = questionDao.findRandomQuestionsByCategory(category, numQ);
        Quiz quiz = new Quiz();
        quiz.setTitle(title);
        quiz.setQuestions(questions);
        quizDao.save(quiz);
        return new ResponseEntity<>("Quiz created", HttpStatus.CREATED);
    }

    public ResponseEntity<List<QuestionWrapper>> getQuizQuestions(Integer id) {
        Optional<Quiz> quiz = quizDao.findById(id);
        List<Question> questionsFromDB = quiz.get().getQuestions();
        List<QuestionWrapper> questionsForUser = new ArrayList<>();
        for(Question q : questionsFromDB)
        {
            QuestionWrapper qw = new QuestionWrapper(q.getId(), q.getQuestion_title(), q.getOption1(), q.getOption2(), q.getOption3(), q.getOption4());
            questionsForUser.add(qw);
        }
        return new ResponseEntity<>(questionsForUser, HttpStatus.OK);
    }

    public ResponseEntity<Integer> calculateResult(Integer id, List<Response> responses) {
        Optional<Quiz> quizOptional = quizDao.findById(id);
        if (!quizOptional.isPresent()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Quiz quiz = quizOptional.get();
        Map<Integer, String> correctAnswers = quiz.getQuestions().stream()
                .collect(Collectors.toMap(Question::getId, Question::getRight_answer));

        int right = 0;
        for (Response response : responses) {
            String correctAnswer = correctAnswers.get(response.getId());
            if (correctAnswer != null && correctAnswer.trim().equalsIgnoreCase(response.getResponse().trim())) {
                right++;
            }
        }

        return new ResponseEntity<>(right, HttpStatus.OK);
    }

}
