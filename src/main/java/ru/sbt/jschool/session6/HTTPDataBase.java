package ru.sbt.jschool.session6;

import ru.sbt.jschool.session6.controller.UserController;
import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.repository.FilesUserRepository;
import ru.sbt.jschool.session6.service.UserServiceImpl;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Arrays;
import java.util.List;

public class HTTPDataBase {
    private final static int PORT = 8080;

    public static void main(String[] args) throws IOException {
        FilesUserRepository repository = new FilesUserRepository();
        UserServiceImpl service = new UserServiceImpl(repository);
        UserController controller = new UserController(service);

        ServerSocket serverSocket = new ServerSocket(PORT);
        while(true) {
            Socket socket = serverSocket.accept();
            controller.run(socket.getInputStream(), socket.getOutputStream());
        }

    }



}
