package ru.otus.spring.domain;

public class QuestionImpl implements Question {
    private final String order;
    private final String description;
    private final String question1;
    private final String question2;
    private final String question3;
    private final String question4;
    private final String answer;

    public QuestionImpl(String[] row) {
        this.order       = row[0];
        this.description = row[1];
        this.question1   = row[2];
        this.question2   = row[3];
        this.question3   = row[4];
        this.question4   = row[5];
        this.answer      = row[6];
    }

    @Override
    public String getOrder() {
        return order;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public String getQuestion1() {
        return question1;
    }

    @Override
    public String getQuestion2() {
        return question2;
    }

    @Override
    public String getQuestion3() {
        return question3;
    }

    @Override
    public String getQuestion4() {
        return question4;
    }

    @Override
    public String getAnswer() {
        return answer;
    }
}
