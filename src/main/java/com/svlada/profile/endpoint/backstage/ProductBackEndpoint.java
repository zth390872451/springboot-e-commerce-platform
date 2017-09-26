package com.svlada.profile.endpoint.backstage;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.FileUploadUtils;
import com.svlada.entity.User;
import com.svlada.entity.product.Category;
import com.svlada.entity.product.DetailsImage;
import com.svlada.entity.product.MajorImage;
import com.svlada.entity.product.Product;
import com.svlada.profile.endpoint.dto.ProductDto;
import com.svlada.profile.endpoint.dto.ProductInfoDescDto;
import com.svlada.user.repository.CategoryRepository;
import com.svlada.user.repository.ProductRepository;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;
import static com.svlada.common.request.CustomResponseStatus._40000;
import static com.svlada.common.request.CustomResponseStatus._40401;

@RestController
@RequestMapping("/back/product")
public class ProductBackEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    /**
     * 录入商品
     *
     * @param dto
     * @return
     */
    @ApiOperation(value="商品基本信息录入", notes="商品录入：基本信息录入")
    @ApiImplicitParams({
           /* @ApiImplicitParam(name = "name", value = "商品名称", paramType = "body", dataType = "Long", required = true),
            @ApiImplicitParam(name = "title", value = "商品页面标题", paramType = "body", dataType = "String", required = true),
            @ApiImplicitParam(name = "introduce", value = "商品简介", paramType = "body", dataType = "String", required = true),
            @ApiImplicitParam(name = "title", value = "商品页面标题", paramType = "body", dataType = "String", required = true),
            @ApiImplicitParam(name = "description", value = "商品页面描述", paramType = "body", dataType = "String", required = true),
            @ApiImplicitParam(name = "searchKey", value = "商品页面搜索关键字，多个关键字以逗号分隔", paramType = "body", dataType = "String", required = true),
            @ApiImplicitParam(name = "price", value = "定价,单位分", paramType = "body", dataType = "Long", required = true),
            @ApiImplicitParam(name = "nowPrice", value = "现价,单位分", paramType = "body", dataType = "Long", required = true),
            @ApiImplicitParam(name = "stock", value = "库存", paramType = "body", dataType = "Long", required = true),*/
//            @ApiImplicitParam(name = "dto", value = "商品所属类别", paramType = "body", dataType = "ProductInfoDescDto", required = true)
    })
    @PostMapping(value = "/add")
    public CustomResponse add(@RequestBody @Valid ProductInfoDescDto dto) {
        Category category = categoryRepository.findOne(dto.getCategoryId());
        if (category==null){
            return fail(_40000,"categoryId对应类别记录不存在!");
        }
        Product product = productRepository.findOneByCode(dto.getCode());
        if (product!=null){
            return fail(_40000,"商品编号不能重复!");
        }
        product = new Product();
        product.setCode(dto.getCode());
        product.setCategory(category);
        product.setName(dto.getName());
        product.setTitle(dto.getTitle());
        product.setIntroduce(dto.getIntroduce());
        product.setPrice(dto.getPrice());
        product.setNowPrice(dto.getNowPrice());
        product.setSearchKey(dto.getSearchKey());
        product.setStock(dto.getStock());
        product.setDescription(dto.getDescription());

        product.setCreateTime(new Date());
        User currentUser = WebUtil.getCurrentUser();
        product.setCreateID(currentUser.getId());
        productRepository.save(product);
        return success();
    }


    /**
     * 上传照片
     * @param productId
     * @param majorImageFiles
     * @param detailImageFiles
     * @return
     */
    @ApiOperation(value="商品图片信息录入", notes="商品录入：图片信息录入")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID", paramType = "path", dataType = "Long", required = true),
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "query", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "query", dataType = "file", required = false)
    })
    @PostMapping(value = "/upload/{productId}")
    public CustomResponse upload(@PathVariable("productId") Long productId,
                                 @RequestParam("majorImageFiles") MultipartFile[] majorImageFiles,
                                 @RequestParam("detailImageFiles") MultipartFile[] detailImageFiles) {
        User user = WebUtil.getCurrentUser();
        Product product = productRepository.findOne(productId);
        if(product==null){
            return fail(_40401);
        }
        String uuid = UUID.randomUUID().toString();
        String path = product.getId() + File.separator + product.getName() + uuid + File.separator;

        if (!StringUtils.isEmpty(majorImageFiles)){
            List<String> filePaths = FileUploadUtils.saveCommonFile(majorImageFiles, path);
            List<MajorImage> majorImages = filePaths.stream().map(imageUrl -> new MajorImage(product, imageUrl)).collect(Collectors.toList());
            List<MajorImage> images = product.getMajorImages();
            images.addAll(majorImages);
            product.setMajorImages(images);
        }
        if (!StringUtils.isEmpty(detailImageFiles)){
            List<String> detailImagesPaths = FileUploadUtils.saveCommonFile(detailImageFiles, path);
            List<DetailsImage> detailImages = detailImagesPaths.stream().map(imageUrl -> new DetailsImage(product, imageUrl)).collect(Collectors.toList());
            List<DetailsImage> images = product.getDetailsImages();
            images.addAll(detailImages);
            product.setDetailsImages(images);
        }
        product.setCreateID(user.getId());
        product.setCreateTime(new Date());
        return success();
    }

    /**
     * 产品后台信息展示，选取活动列表
     * 产品营销设定：
     * 活动特价
     * 促销商品
     * 限时抢购
     * 卖家包邮
     * @param id
     * @param dto
     * @return
     */
    @ApiOperation(value="商品营销策略设置", notes="营销策略：活动、包邮、促销、限时")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", paramType = "path", dataType = "Long", required = true)
    })
    @PutMapping(value = "/mark/{id}")
    public CustomResponse updateProduct(@PathVariable("id") Long id,@RequestBody @Valid ProductDto dto) {
        Product product = productRepository.findOne(id);
        productRepository.save(product);
        return success();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID", paramType = "path", dataType = "Long", required = true),
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "query", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "query", dataType = "file", required = false)
    })
    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
        Product product = productRepository.findOne(id);
        return success(product);
    }

    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody ProductDto dto) {
        Product product = productRepository.findOne(dto.getId());
        product.setUpdateTime(new Date());
        product.setUpdateUserId(WebUtil.getCurrentUser().getId());
        productRepository.save(product);
        return success();
    }

    @PutMapping(value = "/delete")
    public CustomResponse delete(@RequestBody ProductDto dto) {
        Product product = productRepository.findOne(dto.getId());
        productRepository.save(product);
        return success();
    }

}
