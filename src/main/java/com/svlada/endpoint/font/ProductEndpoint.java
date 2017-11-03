package com.svlada.endpoint.font;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.repository.ProductRepository;
import com.svlada.endpoint.dto.ProductDetailsDto;
import com.svlada.endpoint.dto.builder.ProductInfoBuilder;
import com.svlada.entity.product.Product;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.web.PageableDefault;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/product")
public class ProductEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @ApiOperation(value="商品信息列表", notes="商品信息列表分页")
    @ApiImplicitParams({
    })
    @GetMapping(value = "/list")
    public CustomResponse list(@RequestParam(name = "key",required = false) String key,
                               @RequestParam(name = "recommend",required = false) Boolean recommend,
                               @RequestParam(name = "isNew",required = false) Boolean isNew,
                               @PageableDefault(value = 20, sort = { "id" }, direction = Sort.Direction.ASC) Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(this.getSpecification(key,recommend,isNew), pageable);
        Page<ProductDetailsDto> dtoPage = productPage.map(entity -> {
            ProductDetailsDto dto = ProductInfoBuilder.builderProductDetailsDto(entity);
            return dto;
        });
        return success(dtoPage);
    }

    private Specification<Product> getSpecification(String key,Boolean recommend,Boolean isNew){
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if(!StringUtils.isEmpty(key)){
                Predicate searchKeyPredicate = cb.like(root.get("searchKey").as(String.class), "%" + key + "%");
                Predicate namePredicate = cb.like(root.get("name").as(String.class), "%" + key + "%");
                predicates.add(cb.or(searchKeyPredicate,namePredicate));
            }
            if(!StringUtils.isEmpty(recommend)){
                Predicate predicate = cb.equal(root.get("recommend").as(Boolean.class), recommend);
                predicates.add(predicate);
            }
            if(!StringUtils.isEmpty(isNew)){
                Predicate predicate = cb.equal(root.get("isNew").as(Boolean.class), isNew);
                predicates.add(predicate);
            }
            Predicate[] pre = new Predicate[predicates.size()];
            return query.where(predicates.toArray(pre)).getRestriction();
        };
    }

    /**
     *  根据类别查询商品 分页 排序
     */
    @GetMapping(value = "/category/list2")
    public CustomResponse list2(@RequestParam("categoryId")Long categoryId,
                                Pageable pageable) {
        List<Product> productList = productRepository.findAllProductByCategoryId(categoryId,pageable);
        return success(productList);
    }



}
