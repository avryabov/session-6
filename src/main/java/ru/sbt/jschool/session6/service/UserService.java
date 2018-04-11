package ru.sbt.jschool.session6.service;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.util.exception.NotFoundException;

import java.util.List;

public interface UserService {
    User create(User user);

    boolean delete(int id) throws NotFoundException;

    User get(int id) throws NotFoundException;

    List<User> getAll();
}
