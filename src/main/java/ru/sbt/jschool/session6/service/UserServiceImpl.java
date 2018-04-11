package ru.sbt.jschool.session6.service;

import ru.sbt.jschool.session6.model.User;
import ru.sbt.jschool.session6.repository.UserRepository;
import ru.sbt.jschool.session6.util.exception.NotFoundException;

import java.util.List;

public class UserServiceImpl implements UserService {

    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public User create(User user) {
        return repository.save(user);
    }

    @Override
    public boolean delete(int id) throws NotFoundException {
        return repository.delete(id);
    }

    @Override
    public User get(int id) throws NotFoundException {
        return repository.get(id);
    }

    @Override
    public List<User> getAll() {
        return repository.getAll();
    }
}
