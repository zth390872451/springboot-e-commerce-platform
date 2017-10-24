package com.svlada.endpoint.dto.builder;

import com.svlada.common.utils.ApplicationSupport;
import com.svlada.component.repository.ProductRepository;
import com.svlada.endpoint.dto.ShopCartDto;
import com.svlada.entity.ShopCart;
import com.svlada.entity.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ShopCartBuilder {

    public static ShopCartDto buildShopCartDto(ShopCart shopCart){
        ShopCartDto shopCartDto = new ShopCartDto();
        shopCartDto.setNumber(shopCart.getNumber());
        shopCartDto.setProductId(shopCart.getProductId());
        return shopCartDto;
    }

    public static List<ShopCartDto> buildShopCartDtos(List<ShopCart> shopCarts){
        List<ShopCartDto> shopCartDtos = new ArrayList<>();
        ProductRepository productRepository = ApplicationSupport.getBean(ProductRepository.class);
        for (ShopCart shopCart:shopCarts){
            ShopCartDto shopCartDto = new ShopCartDto();
            shopCartDto.setNumber(shopCart.getNumber());
            shopCartDto.setProductId(shopCart.getProductId());
            Product product = productRepository.findOne(shopCart.getProductId());
            if (product!=null){
                shopCartDto.setName(product.getName());
                shopCartDto.setNowPrice(product.getNowPrice());
            }
            shopCartDtos.add(shopCartDto);
        }
        return shopCartDtos;
    }
}
