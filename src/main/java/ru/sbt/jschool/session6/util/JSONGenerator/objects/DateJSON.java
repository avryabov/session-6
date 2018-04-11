package ru.sbt.jschool.session6.util.JSONGenerator.objects;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 */
public class DateJSON implements ObjectJSON {
    private SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");

    @Override
    public StringBuilder json(Object obj, int tabs) {
        return new StringBuilder("\"" + dateFormat.format((Date) obj) + "\"");
    }
}
