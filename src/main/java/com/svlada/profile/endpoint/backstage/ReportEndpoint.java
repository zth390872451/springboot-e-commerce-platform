package com.svlada.profile.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.component.service.ReportService;
import com.svlada.entity.Order;
import com.svlada.entity.product.Product;
import com.svlada.profile.endpoint.dto.OrderDto;
import com.svlada.component.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.component.service.ReportService.CURRENT_DAY;
import static com.svlada.component.service.ReportService.CURRENT_MONTH;
import static com.svlada.component.service.ReportService.RECENT_MONTH;

@RestController
@RequestMapping("/api/back/report2")
public class ReportEndpoint {

    @Autowired
    private ReportService reportService;

    @GetMapping("/user")
    public CustomResponse getUser(){
        Long current_day = reportService.getLoginUser(CURRENT_DAY);
        Long current_month = reportService.getLoginUser(CURRENT_MONTH);
        Long recent_month = reportService.getLoginUser(RECENT_MONTH);
        Map<String,Object> result = new HashMap<>();
        result.put("currentDay",current_day);
        result.put("currentMonth",current_month);
        result.put("recentMonth",recent_month);
        return success(result);
    }

    /**
     *
     * @param pageable 分页参数:
     * 1、商品销量 pageable=(sort=saleCount,DESC) 默认
     * 2、商品庫存數量 pageable=(sort=stock,DESC)
     * 3、商品收藏數量 pageable=(sort=favorite,DESC)
     * 4、商品评论数量 pageable=(sort=badComment,DESC || goodComment,DESC)
     * 5、商品浏览数量 pageable=(sort=hit,DESC)
     * @return
     */
    @GetMapping("/product")
    public CustomResponse getProduct(@PageableDefault(value = 10, sort = {"saleCount"}, direction = Sort.Direction.DESC)
                                                 Pageable pageable){
        List<Product> result = reportService.getProduct(pageable);
        return success(result);
    }

}