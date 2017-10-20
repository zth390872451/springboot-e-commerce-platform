package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.component.repository.MajorImageRepository;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.ProductRepository;
import com.svlada.component.repository.ShopCartRepository;
import com.svlada.endpoint.dto.CartProductDto;
import com.svlada.endpoint.dto.ShopCartDto;
import com.svlada.entity.ShopCart;
import com.svlada.entity.User;
import com.svlada.entity.product.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/shopCart")
public class ShopCartEndpoint {

    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private ShopCartRepository shopCartRepository;
    @Autowired
    private ProductRepository productRepository;

    /**
     * 添加到购物车
     */
    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody ShopCartDto dto) {
        Product product = productRepository.getOne(dto.getProductId());
        if (StringUtils.isEmpty(product)) {
            return fail(CustomResponseStatus._40000, "商品不存在!");
        }
        if (Product.STATUS_DOWN.equals(product.getStatus())) {
            return fail(CustomResponseStatus._40401, "商品已经下架了!");
        }
        User user = WebUtil.getCurrentUser();
        ShopCart shopCart = shopCartRepository.findOneByUserIdAndProduct(user.getId(), product.getId());
        if (shopCart == null) {//购物车中无该商品
            shopCart = new ShopCart();
            shopCart.setUserId(user.getId());
            shopCart.setProductId(dto.getProductId());
            shopCart.setCreateDate(new Date());
            shopCart.setNumber(dto.getNumber());
        }else {//购物车中已经有该商品，则添加数量
            shopCart.setNumber(shopCart.getNumber()+dto.getNumber());
        }
        shopCartRepository.save(shopCart);
        return success();
    }

    /**
     * 删除购物车的某些商品
     *
     * @param productId
     * @return
     */
    @GetMapping("/remove/{productId}")
    public CustomResponse removeProduct(@PathVariable("productId") Long productId) {
        User user = WebUtil.getCurrentUser();
        shopCartRepository.deleteOneByUserIdAndProductId(user.getId(), productId);
        return success();
    }

    /**
     * 清空购物车
     * @return
     */
    @PutMapping(value = "/clear")
    public CustomResponse clearShopCart() {
        User user = WebUtil.getCurrentUser();
        shopCartRepository.deleteAllByUserId(user.getId());
        return success();
    }

    @Autowired
    private MajorImageRepository majorImageRepository;

    /**
     * 获取用户的购物车中的商品信息:商品名称，现价，数量，是否包邮，首页图片，库存，
     * @return
     */
    @PutMapping(value = "/product")
    public CustomResponse product() {
        User user = WebUtil.getCurrentUser();
        List<Product> products = shopCartRepository.findAllProductByUserId(user.getId());

        List<CartProductDto> cartProductDtoList = products.stream().map(product -> new CartProductDto(product.getName(), product.getPrice(), product.getNowPrice(), product.getStock(), product.getIntroduce(), product.getMailFree(), majorImageRepository.findFirstImageUrlByProductIdOrderByIdAsc(product.getId())))
                .collect(Collectors.toList());
//        products.stream().forEach(product -> cartProductDto.setMajorImage(majorImageRepository.findFirstByProductIdOrderById(product.getId()))));
//        products.stream().forEach(product -> new MajorImage(majorImageRepository.findFirstImageUrlByProductIdOrderByIdAsc(product.getId())));
        //组装商品信息
//        products.stream().map(product -> majorImageRepository.findFirstByProductIdOrderById(product.getId()))
        return success(cartProductDtoList);
    }

}
