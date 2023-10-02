package ru.otus.spring.service;

import ru.otus.spring.dao.LoaderCsv;
import ru.otus.spring.domain.Question;

import java.util.Scanner;

public class TrainingImp implements Training {
    private final LoaderCsv loader;

    public TrainingImp(LoaderCsv loader) {
        this.loader = loader;
    }

    @Override
    public void openTerminal(){
        Scanner in = new Scanner(System.in);
        for (Question question: loader.getQuestions() ) {
            System.out.println("Question № " + question.getOrder() + ". " + question.getDescription() + ". Variants:");
            System.out.println(" 1. " + question.getQuestion1());
            System.out.println(" 2. " + question.getQuestion2());
            System.out.println(" 3. " + question.getQuestion3());
            System.out.println(" 4. " + question.getQuestion4());

            System.out.print(" enter the response number >> ");
            String s = in.nextLine();
            if ( s.contentEquals( question.getAnswer() ) ) {
                System.out.println(" Congratulations the answer is correct: [" + s + "] !");
            } else {
                System.out.println(" The answer is incorrect.");
            }
        }
    }
}
