package com.widzin.services;

import com.widzin.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface UserService extends CRUDService<User> {

    Optional<User> findByUsername(String username);
    Page<User> findAllPageable(Pageable pageable);
}
