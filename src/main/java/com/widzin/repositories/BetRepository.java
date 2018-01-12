package com.widzin.repositories;

import com.widzin.models.BetGame;
import org.springframework.data.repository.CrudRepository;

public interface BetRepository extends CrudRepository<BetGame, Integer> {
}
