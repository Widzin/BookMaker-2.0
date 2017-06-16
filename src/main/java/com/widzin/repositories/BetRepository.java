package com.widzin.repositories;

import com.widzin.domain.BetGame;
import org.springframework.data.repository.CrudRepository;

public interface BetRepository extends CrudRepository<BetGame, Integer> {
}
