package com.widzin.repositories;

import com.widzin.model.BetGame;
import org.springframework.data.repository.CrudRepository;

public interface BetRepository extends CrudRepository<BetGame, Integer> {
}
