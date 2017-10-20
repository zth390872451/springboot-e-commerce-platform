package com.svlada.endpoint.dto.builder;

import com.svlada.endpoint.dto.MarkDto;
import com.svlada.endpoint.dto.ProductInfoDescDto;
import com.svlada.endpoint.dto.resp.ProductInfo;
import com.svlada.entity.product.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductInfoBuilder {

    /**
     * 获取产品的基本信息
     *
     * @param source
     * @return
     */
    public static ProductInfoDescDto builderProductInfoDescDto(Product source) {
        ProductInfoDescDto basicInfo = new ProductInfoDescDto();
        basicInfo.setCode(source.getCode());
        basicInfo.setDescription(source.getDescription());
        basicInfo.setIntroduce(source.getIntroduce());
        basicInfo.setName(source.getName());
        basicInfo.setTitle(source.getTitle());
        basicInfo.setNowPrice(source.getNowPrice());
        basicInfo.setPrice(source.getPrice());
        basicInfo.setSearchKey(source.getSearchKey());
        basicInfo.setStatus(source.getStatus());
        basicInfo.setStock(source.getStock());
        return basicInfo;
    }

    public static List<ProductInfoDescDto> builderPids(List<Product> products){
        List<ProductInfoDescDto> productInfoDescDtos = new ArrayList<>();
        for (Product product:products){
            productInfoDescDtos.add(builderProductInfoDescDto(product));
        }
        return productInfoDescDtos;
    }

    /**
     * 获取产品的营销策略信息
     * @param source
     * @return
     */
    public static MarkDto builderMarkDto(Product source) {
        MarkDto markDto = new MarkDto();
        markDto.setCode(source.getCode());
        markDto.setId(source.getId());
        markDto.setMailFree(source.getMailFree());
        markDto.setNew(source.getNew());
        markDto.setNowPrice(source.getNowPrice());
        markDto.setPrice(source.getPrice());
        markDto.setRecommend(source.getRecommend());
        markDto.setSpecialPrice(source.getSpecialPrice());
        return markDto;
    }

    public static ProductInfo builderTarget(Product source) {
        ProductInfo productInfo = new ProductInfo();
        productInfo.setCode(source.getCode());
        productInfo.setDescription(source.getDescription());
        productInfo.setIntroduce(source.getIntroduce());
        productInfo.setMailFree(source.getMailFree());
        productInfo.setName(source.getName());
        productInfo.setTitle(source.getTitle());
        productInfo.setNew(source.getNew());
        productInfo.setNowPrice(source.getNowPrice());
        productInfo.setPrice(source.getPrice());
        productInfo.setRecommend(source.getRecommend());
        productInfo.setSaleCount(source.getSaleCount());
        productInfo.setSearchKey(source.getSearchKey());
        productInfo.setSpecialPrice(source.getSpecialPrice());
        productInfo.setStatus(source.getStatus());
        productInfo.setStock(source.getStock());
        return productInfo;
    }

}
