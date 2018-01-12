package com.widzin.repositories;

import com.widzin.models.Match;
import org.springframework.data.repository.CrudRepository;

public interface MatchRepository extends CrudRepository<Match, Integer> {
}
