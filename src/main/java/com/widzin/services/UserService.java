package com.widzin.services;

import com.widzin.domain.User;

import java.util.Optional;

public interface UserService extends CRUDService<User> {

    Optional<User> findByUsername(String username);
}
