package com.svlada.profile.endpoint.backstage;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.utils.FileUploadUtils;
import com.svlada.entity.User;
import com.svlada.entity.product.Category;
import com.svlada.entity.product.DetailsImage;
import com.svlada.entity.product.MajorImage;
import com.svlada.entity.product.Product;
import com.svlada.profile.endpoint.dto.BasicProductInfoDto;
import com.svlada.profile.endpoint.dto.MarkDto;
import com.svlada.profile.endpoint.dto.ProductInfoDescDto;
import com.svlada.user.repository.CategoryRepository;
import com.svlada.user.repository.DetailsImageRepository;
import com.svlada.user.repository.MajorImageRepository;
import com.svlada.user.repository.ProductRepository;
import io.swagger.annotations.*;
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
@RequestMapping("/api/back/product")
public class ProductBackEndpoint {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private MajorImageRepository majorImageRepository;
    @Autowired
    private DetailsImageRepository detailsImageRepository;


    /**
     * 录入商品
     *
     * @param dto
     * @return
     */
    @ApiOperation(value="商品基本信息录入", notes="商品录入：基本信息录入")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/storage")
    public CustomResponse add(@RequestBody @Valid BasicProductInfoDto dto) {
        Product product = productRepository.findOneByCode(dto.getCode());
        if (product!=null){
            return fail(_40000,"商品编号不能重复!");
        }
        product = new Product();
        product.setCode(dto.getCode());

        if(dto.getCategoryId()!=null && dto.getCategoryId()>0){
            Category category = categoryRepository.findOne(dto.getCategoryId());
            if (category==null){
                return fail(_40000,"categoryId对应类别记录不存在!");
            }
            product.setCategory(category);
        }
        User currentUser = WebUtil.getCurrentUser();
        product.setCreateID(currentUser.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
        return success();
    }
    /**
     * 录入商品
     * @param dto
     * @return
     */
    @ApiOperation(value="商品信息设置[修改]", notes="商品参数设置")
    @ApiImplicitParams({
    })
    @PostMapping(value = "/update")
    public CustomResponse add(@RequestBody @Valid ProductInfoDescDto dto) {
        Product product = productRepository.findOneByCode(dto.getCode());
        if (product==null){
            return fail(_40401,"商品不存在!");
        }
        if (!StringUtils.isEmpty(dto.getName())){
            product.setName(dto.getName());
        }
        if (!StringUtils.isEmpty(dto.getTitle())){
            product.setTitle(dto.getTitle());
        }
        if (!StringUtils.isEmpty(dto.getIntroduce())){
            product.setIntroduce(dto.getIntroduce());
        }
        if (!StringUtils.isEmpty(dto.getPrice())){
            product.setPrice(dto.getPrice());
        }
        if (!StringUtils.isEmpty(dto.getNowPrice())){
            product.setNowPrice(dto.getNowPrice());
        }
        if (!StringUtils.isEmpty(dto.getName())){
            product.setSearchKey(dto.getSearchKey());
        }
        if (!StringUtils.isEmpty(dto.getStock())){
            product.setStock(dto.getStock());
        }
        if (!StringUtils.isEmpty(dto.getDescription())){
            product.setDescription(dto.getDescription());
        }
        if (!StringUtils.isEmpty(dto.getStatus())){
            product.setStatus(dto.getStatus());
        }
        product.setUpdateTime(new Date());
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
            @ApiImplicitParam(name = "majorImageFiles", value = "商品页面轮播图", paramType = "body", dataType = "file", required = false),
            @ApiImplicitParam(name = "detailImageFiles", value = "商品详情页面图", paramType = "body", dataType = "file", required = false)
    })
    @PostMapping(value = "/upload/{productId}")
    public CustomResponse upload(@PathVariable("productId") Long productId,
                                 @RequestParam(value = "majorImageFiles",required = false) MultipartFile[] majorImageFiles,
                                 @RequestParam(value = "detailImageFiles",required = false) MultipartFile[] detailImageFiles) {
        User user = WebUtil.getCurrentUser();
        Product product = productRepository.findOne(productId);
        if(product==null){
            return fail(_40401);
        }
        String uuid = UUID.randomUUID().toString();
        String path = product.getId() + File.separator+ product.getName() + File.separator+ uuid + File.separator;

        if (!StringUtils.isEmpty(majorImageFiles)){
            List<String> filePaths = FileUploadUtils.saveCommonFile(majorImageFiles, path);
            List<MajorImage> majorImages = filePaths.stream().map(imageUrl -> new MajorImage(product, imageUrl)).collect(Collectors.toList());
            List<MajorImage> images = product.getMajorImages();
            images.addAll(majorImages);
            majorImageRepository.save(majorImages);
            product.setMajorImages(images);
        }
        if (!StringUtils.isEmpty(detailImageFiles)){
            List<String> detailImagesPaths = FileUploadUtils.saveCommonFile(detailImageFiles, path);
            List<DetailsImage> detailImages = detailImagesPaths.stream().map(imageUrl -> new DetailsImage(product, imageUrl)).collect(Collectors.toList());
            List<DetailsImage> images = product.getDetailsImages();
            images.addAll(detailImages);
            detailsImageRepository.save(detailImages);
            product.setDetailsImages(images);
        }
        product.setCreateID(user.getId());
        product.setCreateTime(new Date());
        productRepository.save(product);
        return success();
    }


    @ApiOperation(value="商品营销策略设置", notes="营销策略：是否包邮、卖家强推、新品上市、特价优惠")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "productId", value = "商品ID", paramType = "path", dataType = "Long", required = true)
    })
    @PutMapping(value = "/mark/{productId}")
    public CustomResponse updateProduct(@PathVariable("productId") Long productId,
                                        @RequestBody @Valid MarkDto dto) {
        Product product = productRepository.findOne(productId);
        if (product!=null){
            if (dto.getMailFree()!=null){
                product.setMailFree(dto.getMailFree());
            }
            if (dto.getNew()!=null){
                product.setNew(dto.getNew());
            }
            if (dto.getRecommend()!=null){
                product.setRecommend(dto.getRecommend());
            }
            if (dto.getSpecialPrice()!=null){
                if (dto.getSpecialPrice() && dto.getNowPrice()!=null){
                    product.setNowPrice(dto.getNowPrice());
                }
                if (dto.getSpecialPrice() && dto.getPrice()!=null){
                    product.setPrice(dto.getPrice());
                }
                product.setSpecialPrice(dto.getSpecialPrice());
            }
            productRepository.save(product);
        }
        return success();
    }

    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "商品ID", paramType = "path", dataType = "Long", required = true),
    })
    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id) {
        Product product = productRepository.findOne(id);
        if (product==null){
            return fail(_40000,"商品ID对应的商品不存在!");
        }
        return success(product);
    }



}
