package ru.sbt.jschool.session6;

import ru.sbt.jschool.session6.controller.UserController;
import ru.sbt.jschool.session6.repository.FilesUserRepository;
import ru.sbt.jschool.session6.service.UserServiceImpl;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;

public class HTTPDataBase {
    private final static String DEFAULT_DIR = "src/main/resources/config.properties";

    public static void main(String[] args) throws IOException {
        String properties;
        if (args.length == 0)
            properties = DEFAULT_DIR;
        else
            properties = args[0];

        Properties property = new Properties();

        try (FileInputStream fis = new FileInputStream(properties)) {
            property.load(fis);
        } catch (IOException e) {
            System.err.println("Error: Property file not found");
        }
        int port = Integer.parseInt(property.getProperty("db.port"));
        String dir = property.getProperty("db.dir");
        FilesUserRepository repository = new FilesUserRepository(dir);
        UserServiceImpl service = new UserServiceImpl(repository);
        UserController controller = new UserController(service);

        ServerSocket serverSocket = new ServerSocket(port);
        while (true) {
            Socket socket = serverSocket.accept();
            controller.doTask(socket.getInputStream(), socket.getOutputStream());
        }

    }

}
