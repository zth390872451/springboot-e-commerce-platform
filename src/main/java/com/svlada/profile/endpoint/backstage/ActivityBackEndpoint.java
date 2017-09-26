package com.svlada.profile.endpoint.backstage;

import com.svlada.common.request.CustomResponse;
import com.svlada.entity.Activity;
import com.svlada.entity.product.Product;
import com.svlada.profile.endpoint.dto.ActivityDto;
import com.svlada.user.repository.ActivityRepository;
import com.svlada.user.repository.CategoryRepository;
import com.svlada.user.repository.ProductRepository;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/back/activity")
public class ActivityBackEndpoint {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ActivityRepository activityRepository;

    /**
     *
     * 添加活动:并添加一系列对应的产品
     * @param dto
     * @return
     */
    @ApiOperation(value="添加活动", notes="")
    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody @Valid ActivityDto dto) {
        Activity activity = activityRepository.findOneByName(dto.getName());
        if (activity!=null){
            return fail();
        }
        Long[] productIds = dto.getProductIds();
        List<Product> productList = new ArrayList<>();
        /*for (Long productId:productIds){
            Product product = productRepository.findOne(productId);
            if (product!=null){
                productList.add(product);
            }
        }*/
        activity = new Activity();
        activity.setName(dto.getName());
        activity.setContent(dto.getContent());
        activity.setMaxSaleCount(dto.getMaxSaleCount());
//        activity.setProductList(productList);
        activity.setStartDate(dto.getStartDate());
        activity.setEndDate(dto.getEndDate());
        activity.setStatus(Activity.STATUS_NORMAL);
        activityRepository.save(activity);
        return success();
    }


    /**
     * 活动下线，则对应的所有的产品都下线
     * 修改活动信息:状态 内容 名字 日期
     * @param dto
     * @return
     */
    @ApiOperation(value="更新活动信息", notes="")
    @PostMapping(value = "/update")
    public CustomResponse update(@RequestBody @Valid ActivityDto dto) {
        Activity activity = activityRepository.findOne(dto.getId());
        if (dto.getStatus()!=null){
            activity.setStatus(dto.getStatus());
        }
        if (dto.getContent()!=null){
            activity.setContent(dto.getContent());
        }
        if (dto.getName()!=null){
            activity.setName(dto.getName());
        }
        if (activity.getMaxSaleCount()!=null&&activity.getMaxSaleCount()>0){
            activity.setMaxSaleCount(dto.getMaxSaleCount());
        }
        if (dto.getStartDate()!=null){
            activity.setStartDate(dto.getStartDate());
        }
        if (dto.getEndDate()!=null){
            activity.setEndDate(dto.getEndDate());
        }
        activityRepository.save(activity);
        return success();
    }

    /**
     * 查询所有的活动:1、在活动页面添加商品 2、在商品页面添加活动
     * @return
     */
    @ApiOperation(value="查询所有的活动", notes="")
    @GetMapping(value = "/list")
    public CustomResponse list() {
        List<Activity> activityList = activityRepository.findAll();
        activityList.stream().forEach(activity->activity.setProductList(null));
        return success(activityList);
    }





}
