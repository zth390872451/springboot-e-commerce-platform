package com.svlada.user.repository;

import com.svlada.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * UserRepository
 * 
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findAllByUserId(Long id);
}
