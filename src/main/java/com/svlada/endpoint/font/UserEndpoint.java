package com.svlada.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.component.repository.OrderRepository;
import com.svlada.component.repository.PartnerRepository;
import com.svlada.component.repository.UserRepository;
import com.svlada.endpoint.dto.UserDto;
import com.svlada.entity.Order;
import com.svlada.entity.Partner;
import com.svlada.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.svlada.common.request.CustomResponseBuilder.fail;
import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/font/user")
public class UserEndpoint {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PartnerRepository partnerRepository;

    @PostMapping(value = "/register")
    public CustomResponse register(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        userRepository.save(user);
        return success();
    }




    @PutMapping(value = "/forgot")
    public CustomResponse forgot(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        userRepository.save(user);
        return success();
    }

    @GetMapping("/get")
    public CustomResponse get(){
        User user = WebUtil.getCurrentUser();
        Map<String,Object> result = new HashMap<>();
        result.put("username",user.getUsername());
        result.put("nickName",user.getNickName());
        result.put("balance",user.getBalance());
        result.put("mobile",user.getMobile());
        result.put("sex",user.getSex());
        result.put("province",user.getProvince());
        result.put("city",user.getCity());
        result.put("address",user.getAddress());
        return success(result);
    }


    @PostMapping(value = "/update")
    public CustomResponse update(@RequestBody UserDto dto) {
        User user = WebUtil.getCurrentUser();
        if (user==null){
            return fail(CustomResponseStatus._40000,"用户ID对应的记录不存在!");
        }
        if (!StringUtils.isEmpty(dto.getMobile())){
            user.setMobile(dto.getMobile());
        }
        if (!StringUtils.isEmpty(dto.getNickName())){
            user.setNickName(dto.getNickName());
        }
        if (!StringUtils.isEmpty(dto.getProvince())){
            user.setProvince(dto.getProvince());
        }
        if (!StringUtils.isEmpty(dto.getCity())){
            user.setCity(dto.getCity());
        }
        if (!StringUtils.isEmpty(dto.getAddress())){
            user.setAddress(dto.getAddress());
        }
        userRepository.save(user);
        return success();
    }


    /**
     * 查看用户合伙人列表
     * @return
     */
    @GetMapping("/list/partner")
    public CustomResponse partner(){
        User user = WebUtil.getCurrentUser();
        List<Partner> partners = partnerRepository.findAllByUserId(user.getId());
        return success(partners);
    }

    @Autowired
    private OrderRepository orderRepository;

    /**
     * 查看订单列表
     * @return
     */
    @GetMapping("/list/order")
    public CustomResponse order(){
        User user = WebUtil.getCurrentUser();
        List<Order> orders = orderRepository.findOneByUserId(user.getOpenId());
        List<Partner> partners = partnerRepository.findAllByUserId(user.getId());
        return success(partners);
    }

}
