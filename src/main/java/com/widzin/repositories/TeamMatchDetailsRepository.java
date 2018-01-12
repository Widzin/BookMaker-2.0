package com.widzin.repositories;

import com.widzin.models.TeamMatchDetails;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamMatchDetailsRepository extends CrudRepository<TeamMatchDetails, Integer> {
}
