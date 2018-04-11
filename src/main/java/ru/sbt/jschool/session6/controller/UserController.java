package ru.sbt.jschool.session6.controller;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.service.UserServiceImpl;
import ru.sbt.jschool.session6.util.JSONGenerator.JSONGenerator;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class UserController {
    UserServiceImpl service;
    JSONGenerator jsonGenerator;

    public UserController(UserServiceImpl service) {
        this.service = service;
        this.jsonGenerator = new JSONGenerator();
    }

    public void run(InputStream inputStream, OutputStream outputStream) throws IOException {
        Object obj = doRequest(inputStream);
        doResponse(outputStream, obj);
    }

    private Object doRequest(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String message;
        while ((message = bufferedReader.readLine()) != null) {
            String[] request = message.split("\\s|/|\\?|=|&");

            for(int i = 0; i < request.length; i++) {
                System.out.println(i + "=" + request[i]);
            }

            if(!request[0].equals("GET") | !request[2].equals("user")); //TODO
            switch (request[3]) {
                case "create":
                    return create(Arrays.copyOfRange(request, 3, request.length));
                case "delete":
                    return delete(request[4]);
                case "list":
                    return list();
                default:
                    return getUser(request[3]);
            }
        }
        return null;
    }

    private void doResponse(OutputStream outputStream, Object obj) throws IOException {
        PrintWriter out = new PrintWriter(outputStream);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html");
        out.println("\r\n");
        String messege = jsonGenerator.generate(obj);
        System.out.println(messege);
        out.println("<pre>");
        out.println(messege);
        out.println("<pre>");
        out.flush();
    }

    private int create(String[] args) {
        User user = new User();
        for(int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "name":
                    user.setName(args[i+1]);
                    break;
                case "age":
                    user.setAge(Integer.parseInt(args[i+1]));
                    break;
                case "salary":
                    user.setSalary(Integer.parseInt(args[i+1]));
                    break;
            }
        }
        return service.create(user).getId();
    }

    private boolean delete(String id) {
        return service.delete(Integer.parseInt(id));
    }

    private List<User> list() {
        return service.getAll();
    }

    private User getUser(String id) {
        return service.get(Integer.parseInt(id));
    }
}
