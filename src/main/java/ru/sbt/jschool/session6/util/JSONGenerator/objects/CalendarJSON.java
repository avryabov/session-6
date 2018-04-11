package ru.sbt.jschool.session6.util.JSONGenerator.objects;

import java.util.Calendar;

/**
 */
public class CalendarJSON extends DateJSON {
    @Override
    public StringBuilder json(Object obj, int tabs) {
        return super.json(((Calendar) obj).getTime(), tabs);
    }
}
