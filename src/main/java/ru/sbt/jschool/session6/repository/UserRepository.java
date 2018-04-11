package ru.sbt.jschool.session6.repository;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.util.exception.NotFoundException;

import java.util.List;

public interface UserRepository {
    User save(User user);

    boolean delete(int id);

    User get(int id) throws NotFoundException;

    List<User> getAll();
}
