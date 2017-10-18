package com.svlada.component.repository;

import com.svlada.entity.ShopCart;
import com.svlada.entity.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ShopCartRepository extends JpaRepository<ShopCart, Long> {
    @Query(value = "select p.* from shop_cart sc inner join product p on  sc.product_id = p.id where sc.user_id = ?1 order by sc.create_date",nativeQuery = true)
    void deleteOneByUserIdAndProductId(Long id, Long productId);
    @Query(value = "select p.* from shop_cart sc inner join product p on  sc.product_id = p.id where sc.user_id = ?1 order by sc.create_date",nativeQuery = true)
    ShopCart findOneByUserIdAndProduct(Long id, Long productId);

    void deleteAllByUserId(Long userId);

    @Query(value = "select p.* from shop_cart sc inner join product p on  sc.product_id = p.id where sc.user_id = ?1 order by sc.create_date",nativeQuery = true)
    List<Product> findAllProductByUserId(Long userId);
}
