package com.svlada.component.repository;

import com.svlada.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 * 
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Activity findOneByName(String name);
}
