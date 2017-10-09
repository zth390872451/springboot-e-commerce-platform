package com.svlada.component.repository;

import com.svlada.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    List<Partner> findAllByUserId(Long id);
}
