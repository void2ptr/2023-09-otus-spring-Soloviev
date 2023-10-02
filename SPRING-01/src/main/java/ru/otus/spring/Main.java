package ru.otus.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.otus.spring.service.TrainingImp;

public class Main {
    public static void main(String[] args) {
        ClassPathXmlApplicationContext content = new ClassPathXmlApplicationContext("/applicationContext.xml");

        TrainingImp service =  content.getBean(TrainingImp.class);
        service.openTerminal();
    }
}
