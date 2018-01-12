package com.widzin.repositories;

import com.widzin.models.Game;
import org.springframework.data.repository.CrudRepository;

public interface GameRepository extends CrudRepository<Game, Integer>{
}
