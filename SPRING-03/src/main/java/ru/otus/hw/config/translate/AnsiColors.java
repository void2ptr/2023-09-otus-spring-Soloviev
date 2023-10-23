package ru.otus.hw.config.translate;

public class AnsiColors {
    private static final String ANSI_RESET  = "\u001B[0m";
    private static final String ANSI_BLACK  = "\u001B[30m";
    private static final String ANSI_RED    = "\u001B[31m";
    private static final String ANSI_GREEN  = "\u001B[32m";
    private static final String ANSI_YELLOW = "\u001B[33m";
    private static final String ANSI_BLUE   = "\u001B[34m";
    private static final String ANSI_PURPLE = "\u001B[35m";
    private static final String ANSI_CYAN   = "\u001B[36m";
    private static final String ANSI_WHITE  = "\u001B[37m";

    public static String[] getColors(){
        return new String[]{
                ANSI_RESET,  // 0
                ANSI_BLACK,  // 1
                ANSI_RED,    // 2
                ANSI_GREEN,  // 3
                ANSI_YELLOW, // 4
                ANSI_BLUE,   // 5
                ANSI_PURPLE, // 6
                ANSI_CYAN,   // 7
                ANSI_WHITE   // 8
        };
    }
}
