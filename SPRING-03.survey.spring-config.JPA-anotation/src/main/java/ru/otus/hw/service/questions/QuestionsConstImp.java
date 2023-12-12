package ru.otus.hw.service.questions;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QuestionsConstImp implements QuestionsConst {
    private static final String ANSWER_PROMPT =  "Please input the correct answer number:";

    public String getAnswerPrompt() {
        return ANSWER_PROMPT;
    }
}
