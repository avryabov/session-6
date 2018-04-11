package ru.sbt.jschool.session6.controller;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.service.UserServiceImpl;
import ru.sbt.jschool.session6.util.JSONGenerator.JSONGenerator;
import ru.sbt.jschool.session6.util.exception.NotFoundException;

import java.io.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserController {
    UserServiceImpl service;
    JSONGenerator jsonGenerator;

    Map<Integer, String> codeMap = new HashMap<>();

    {
        codeMap.put(200, "OK");
        codeMap.put(400, "Bad Request");
        codeMap.put(404, "Not Found");
    }

    public UserController(UserServiceImpl service) {
        this.service = service;
        this.jsonGenerator = new JSONGenerator();
    }

    public void run(InputStream inputStream, OutputStream outputStream) {
        Object obj = null;
        int code = 200;
        try {
            obj = doRequest(inputStream);
        } catch (NotFoundException e) {
            code = 404;
        } catch (IOException e) {
            code = 400;
        }
        try {
            doResponse(outputStream, code, obj);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Object doRequest(InputStream inputStream) throws NotFoundException, IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String message;
        if ((message = bufferedReader.readLine()) != null) {
            String[] request = message.split("\\s|/|\\?|=|&");
            if (request.length < 3 | !request[0].equals("GET") | !request[2].equals("user"))
                throw new IOException("Error request format");
            switch (request[3]) {
                case "create":
                    return create(Arrays.copyOfRange(request, 3, request.length));
                case "delete":
                    return delete(request[4]);
                case "list":
                    return list();
                default:
                    if (request[3].matches("\\d+"))
                        return getUser(request[3]);
            }
        }
        throw new IOException("Request is null");
    }

    private void doResponse(OutputStream outputStream, int code, Object obj) throws IOException {
        String message;
        if (code == 200)
            message = jsonGenerator.generate(obj);
        else
            message = codeMap.get(code);
        message = "<pre>" + message + "<pre>";
        String response = "HTTP/1.1 " + code + " " + codeMap.get(code) + "\r\n" +
                "Server: HTTPDataBase\r\n" +
                "Content-Type: text/html\r\n" +
                "Content-Length: " + message.length() + "\r\n" +
                "Connection: close\r\n\r\n";
        String result = response + message;
        outputStream.write(result.getBytes());
        outputStream.flush();
    }

    private int create(String[] args) {
        User user = new User();
        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "name":
                    user.setName(args[i + 1]);
                    break;
                case "age":
                    user.setAge(Integer.parseInt(args[i + 1]));
                    break;
                case "salary":
                    user.setSalary(Integer.parseInt(args[i + 1]));
                    break;
            }
        }
        return service.create(user).getId();
    }

    private boolean delete(String id) throws NotFoundException {
        return service.delete(Integer.parseInt(id));
    }

    private List<User> list() {
        return service.getAll();
    }

    private User getUser(String id) throws NotFoundException {
        return service.get(Integer.parseInt(id));
    }
}
