package com.practice.games.boardgame;

import java.rmi.NoSuchObjectException;

public enum Color {
    ANSI_RESET("Reset", "\u001B[0m"),
    ANSI_BLACK("Black", "\u001B[30m"),
    ANSI_RED("Red", "\u001B[31m"),
    ANSI_GREEN("Green", "\u001B[32m"),
    ANSI_YELLOW("Yellow", "\u001B[33m"),
    ANSI_BLUE("Blue", "\u001B[34m"),
    ANSI_PURPLE("Purple", "\u001B[35m"),
    ANSI_CYAN("Cyan", "\u001B[36m"),
    ANSI_WHITE("White", "\u001B[37m");

    private String name;
    private String code;

    Color(String name, String code){
        this.name = name;
        this.code = code;
    }

    @Override
    public String toString() {
        return format(this, name);
    }

    public static String format(Color color, String text){
        return color.code + text + Color.ANSI_RESET.code;
    }

    public static Color fromString(String s) throws NoSuchObjectException {
        s = s.trim().toLowerCase();
        for(Color c : Color.values()){
            if (s.equals(c.name.toLowerCase()))
                return c;
        }
        throw new NoSuchObjectException("No matching color found.");
    }

}