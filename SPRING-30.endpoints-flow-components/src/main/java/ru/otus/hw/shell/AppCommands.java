package ru.otus.hw.shell;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import ru.otus.hw.service.DesInspectionService;
import ru.otus.hw.service.InsectService;


@SuppressWarnings("unused")
@RequiredArgsConstructor
@ShellComponent
@Slf4j
public class AppCommands {

    private final InsectService insectService;

    private final DesInspectionService desInspectionService;

    @ShellMethod(value = "transformation", key = "t")
    public void startTransformation() {
        insectService.startMutation();
    }

    @ShellMethod(value = "desInspection", key = "d")
    public void startDesInspection() {
        desInspectionService.startDesInspection();
    }

}
