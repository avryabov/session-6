package ru.sbt.jschool.session6.util.JSONGenerator.objects;

/**
 */
public class StringJSON implements ObjectJSON {
    @Override
    public StringBuilder json(Object obj, int tabs) {
        return new StringBuilder("\"" + obj + "\"");
    }
}
