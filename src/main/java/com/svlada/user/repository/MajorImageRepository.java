package com.svlada.user.repository;

import com.svlada.entity.product.MajorImage;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * UserRepository
 *
 * @author vladimir.stankovic
 *
 * Aug 16, 2016
 */
public interface MajorImageRepository extends JpaRepository<MajorImage, Long> {

}
