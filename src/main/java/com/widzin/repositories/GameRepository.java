package com.widzin.repositories;

import com.widzin.domain.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer>{
}
