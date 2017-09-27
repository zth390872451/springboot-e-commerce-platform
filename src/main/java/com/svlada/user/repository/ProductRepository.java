package com.svlada.user.repository;

import com.svlada.entity.product.Product;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {


    @Query(value = "SELECT *from product p WHERE p.category_id = ?1 ORDER BY ?2 ?3 limit ?4,?5",nativeQuery = true)
    List<Product> findAllProductByCategoryIdOrderByCondition(Long id,String orderCondition,String ascOrDesc,Integer page,Integer size);

    List<Product> findAllProductByCategoryId(Long id,Pageable pageable);

    Product findOneByCode(String code);
}
