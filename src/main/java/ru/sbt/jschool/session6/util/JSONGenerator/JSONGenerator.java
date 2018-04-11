package ru.sbt.jschool.session6.util.JSONGenerator;

import org.apache.commons.lang3.ClassUtils;
import ru.sbt.jschool.session6.util.JSONGenerator.objects.CalendarJSON;
import ru.sbt.jschool.session6.util.JSONGenerator.objects.DateJSON;
import ru.sbt.jschool.session6.util.JSONGenerator.objects.ObjectJSON;
import ru.sbt.jschool.session6.util.JSONGenerator.objects.StringJSON;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.*;

import static ru.sbt.jschool.session6.util.JSONGenerator.JSONUtil.*;

/**
 */
public class JSONGenerator {

    private Map<Class, ObjectJSON> typeMap = new HashMap<>();

    {
        typeMap.put(String.class, new StringJSON());
        typeMap.put(Date.class, new DateJSON());
        typeMap.put(Calendar.class, new CalendarJSON());
        typeMap.put(GregorianCalendar.class, new CalendarJSON());
    }

    public String generate(Object obj) {
        return generateObject(obj, "", 0).toString();
    }

    public void addType(Class clazz, ObjectJSON obj) {
        typeMap.put(clazz, obj);
    }

    private StringBuilder generateObject(Object obj, String name, int tabs) {
        StringBuilder sb = new StringBuilder();
        if (obj == null) {
            sb.append(appendString(tabs, name, new StringBuilder("null")));
        } else if (ClassUtils.isPrimitiveOrWrapper(obj.getClass())) {
            sb.append(appendString(tabs, name, new StringBuilder(obj.toString())));
        } else if (obj.getClass().isArray()) {
            sb.append(appendString(tabs, name, new StringBuilder("")));
            sb.append(START_ARR);
            tabs++;
            for (int i = 0; i < Array.getLength(obj); i++) {
                sb.append(generateObject(Array.get(obj, i), "", tabs));
                if (i < Array.getLength(obj) - 1)
                    sb.append(JSONUtil.DIV);
                sb.append(JSONUtil.RET);
            }
            tabs--;
            sb.append(String.format(END_ARR, shift(tabs)));
        } else if (typeMap.containsKey(obj.getClass())) {
            sb.append(appendString(tabs, name, typeMap.get(obj.getClass()).json(obj, tabs)));
        } else if (ClassUtils.getAllInterfaces(obj.getClass()).contains(Collection.class)) {
            Object[] arr = ((Collection) obj).toArray();
            sb.append(generateObject(arr, name, tabs));
        } else {
            sb.append(appendString(tabs, name, generateCustomObj(obj, tabs)));
        }
        return sb;
    }

    private StringBuilder appendString(int tabs, String name, StringBuilder object) {
        StringBuilder sb = new StringBuilder();
        if (name.equals("")) {
            sb.append(String.format(UNNAMED_OBJ, shift(tabs), object));
        } else {
            sb.append(String.format(NAMED_OBJ, shift(tabs), name, object));
        }
        return sb;
    }

    private StringBuilder generateCustomObj(Object obj, int tabs) {
        StringBuilder sb = new StringBuilder();
        sb.append(START);
        tabs++;
        Class clazz = obj.getClass();
        do {
            Field[] fields = clazz.getDeclaredFields();
            if (fields.length == 0)
                break;
            sb.append(generateFields(obj, fields, tabs));
            clazz = clazz.getSuperclass();
        } while (clazz != null);
        //sb.deleteCharAt(sb.length() - 2);
        tabs--;
        sb.append(String.format(END, JSONUtil.shift(tabs)));
        return sb;
    }


    private StringBuilder generateFields(Object obj, Field[] fields, int tabs) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            String fieldName = field.getName();
            if (fieldName.equals("this$0"))
                continue;
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            Object fieldObj = null;
            try {
                fieldObj = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            sb.append(generateObject(fieldObj, fieldName, tabs));
            if (i < fields.length - 1)
                sb.append(DIV);
            sb.append(RET);
        }
        return sb;
    }


}
