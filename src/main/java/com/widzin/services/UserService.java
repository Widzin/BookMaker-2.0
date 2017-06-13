package com.widzin.services;

import com.widzin.domain.User;

public interface UserService extends CRUDService<User> {

    User findByUsername(String username);
}
