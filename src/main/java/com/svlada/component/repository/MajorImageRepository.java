package com.svlada.component.repository;

import com.svlada.entity.product.MajorImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface MajorImageRepository extends JpaRepository<MajorImage, Long> {

    @Query(value = "select mi.image_url from major_image mi  where mi.product_id = ?1 order by mi.show_order asc limt 1",nativeQuery = true)
    String findFirstImageUrlByProductIdOrderByProductId(Long product_id);

}
