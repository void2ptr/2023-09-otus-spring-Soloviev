package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.AppConfig;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;
    private final TestFileNameProvider fileNameProvider;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below :%n");
        // Получить вопросы из дао и вывести их с вариантами ответов
        AppConfig appConfig = new AppConfig(fileNameProvider.getTestFileName());
        CsvQuestionDao csvQuestionDao = new CsvQuestionDao(appConfig);

        StringBuilder sb = new StringBuilder();
        for (Question question : csvQuestionDao.findAll() ) {
            sb.append(" ");
            sb.append(question.text());
            sb.append("\n");

            for (Answer answer : question.answers() ) {
                sb.append("  - ");
                sb.append(answer.text());
                sb.append("\n");
            }
            ioService.printLine(sb.toString());
            sb.setLength(0);
        }
    }
}
