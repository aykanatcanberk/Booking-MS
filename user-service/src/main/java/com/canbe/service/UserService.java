package com.canbe.service;

import com.canbe.modal.User;

import java.util.List;

public interface UserService {
    List<User> findAll();

    User createUser(User user);

    User getUserById(Long id) throws Exception;

    User updateUser(Long id, User user) throws Exception;

    void deleteUserById(Long id) throws Exception;
}
