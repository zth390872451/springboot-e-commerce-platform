package com.svlada.component.repository;

import com.svlada.entity.Address;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface AddressRepository extends JpaRepository<Address, Long> {

    void deleteOneByIdAndUserId(Long id, Long userId);

    Address findOneByUserIdAndIsDefault(Long userId,Boolean isDefault);

    Address findOneByIdAndUserId(Long id, Long userId);

    Address findAllByUserId(Long userId);
}
