package com.widzin.repositories;

import com.widzin.domain.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepositoryForPages extends PagingAndSortingRepository<User, Long>{
}
