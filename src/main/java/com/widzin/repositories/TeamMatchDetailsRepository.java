package com.widzin.repositories;

import com.widzin.model.TeamMatchDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMatchDetailsRepository extends CrudRepository<TeamMatchDetails, Integer> {
}
