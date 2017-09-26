package com.svlada.profile.endpoint;

import com.svlada.common.request.CustomResponse;
import com.svlada.profile.endpoint.dto.ProductDto;
import com.svlada.entity.product.Product;
import com.svlada.user.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/product")
public class ProductEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody ProductDto dto) {
        Product product = new Product();
        productRepository.save(product);
        return success();
    }

    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
        Product product = productRepository.findOne(id);
        return success(product);
    }

    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody ProductDto dto) {
        Product product = productRepository.findOne(dto.getId());
        productRepository.save(product);
        return success();
    }

    @PutMapping(value = "/delete")
    public CustomResponse delete(@RequestBody ProductDto dto) {
        Product product= productRepository.findOne(dto.getId());
        productRepository.save(product);
        return success();
    }

    /**
     *  根据类别查询商品 分页 排序
     */
    @GetMapping(value = "/category/list")
    public CustomResponse list2(@RequestParam("categoryId")Long categoryId,
                                Pageable pageable) {
        List<Product> productList = productRepository.findAllProductByCategoryId(categoryId,pageable);
        return success(productList);
    }



}
