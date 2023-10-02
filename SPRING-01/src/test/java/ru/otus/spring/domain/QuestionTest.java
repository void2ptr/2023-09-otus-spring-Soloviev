package ru.otus.spring.domain;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Класс Question")
class QuestionTest {
    private static QuestionImpl question;

    @BeforeAll
    static void setUp(){
        String[] row = {
                "1", "Description",
                "answer 1", "answer 2", "answer 3", "answer 4",
                "2"
        };
        question = new QuestionImpl(row);
    }

    @Test
    void getOrder() {
        assertEquals(question.getOrder(), "1");
    }

    @Test
    void getDescription() {
        assertEquals(question.getDescription(), "Description");
    }

    @Test
    void getQuestion1() {
        assertEquals(question.getQuestion1(), "answer 1");
    }

    @Test
    void getQuestion2() {
        assertEquals(question.getQuestion2(), "answer 2");
    }

    @Test
    void getQuestion3() {
        assertEquals(question.getQuestion3(), "answer 3");
    }

    @Test
    void getQuestion4() {
        assertEquals(question.getQuestion4(), "answer 4");
    }

    @Test
    void getAnswer() {
        assertEquals(question.getAnswer(), "2");
    }
}