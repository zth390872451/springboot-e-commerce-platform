package com.svlada.component.repository;

import com.svlada.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PartnerRepository extends JpaRepository<Partner, Long> {

    Partner findOneByUserId(Long userId);
}
