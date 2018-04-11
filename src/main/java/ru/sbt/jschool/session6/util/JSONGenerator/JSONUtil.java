package ru.sbt.jschool.session6.util.JSONGenerator;

/**
 */
class JSONUtil {
    static final String START = "{\n";
    static final String END = "%s}";
    static final String NAMED_OBJ = "%s\"%s\":%s";
    static final String UNNAMED_OBJ = "%s%s";
    static final String DIV = ",";
    static final String RET = "\n";
    static final String TAB = "\t";
    static final String START_ARR = "[\n";
    static final String END_ARR = "%s]";

    static String shift(int tabs) {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < tabs; i++) {
            sb.append(TAB);
        }
        return sb.toString();
    }
}
