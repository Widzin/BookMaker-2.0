package com.widzin.services;

import com.widzin.domain.User;

import com.widzin.repositories.UserRepository;
import com.widzin.repositories.UserRepositoryForPages;
import com.widzin.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Profile("springdatajpa")
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;
    private UserRepositoryForPages userRepositoryForPages;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
	public void setUserRepositoryForPages (UserRepositoryForPages userRepositoryForPages) {
		this.userRepositoryForPages = userRepositoryForPages;
	}

	private EncryptionService encryptionService;

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public List<?> listAll() {
        List<User> users = new ArrayList<>();
        userRepository.findAll().forEach(users::add); //fun with Java 8
        return users;
    }

    @Override
    public User getById(Integer id) {
        return userRepository.findOne(id);
    }

    @Override
    public User saveOrUpdate(User domainObject) {
        if(domainObject.getPassword() != null){
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return userRepository.save(domainObject);
    }
    @Override
      @Transactional
       public void delete(Integer id) {
        userRepository.delete(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Transactional
	@Override
	public Page<User> findAllPageable (Pageable pageable) {
		return userRepositoryForPages.findAll(pageable);
	}
}
