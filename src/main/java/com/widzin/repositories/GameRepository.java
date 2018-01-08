package com.widzin.repositories;

import com.widzin.model.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer>{
}
