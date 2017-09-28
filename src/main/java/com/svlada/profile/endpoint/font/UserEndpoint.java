package com.svlada.profile.endpoint.font;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.common.request.CustomResponseStatus;
import com.svlada.common.utils.DateUtils;
import com.svlada.entity.Partner;
import com.svlada.entity.User;
import com.svlada.profile.endpoint.dto.UserDto;
import com.svlada.user.repository.PartnerRepository;
import com.svlada.user.repository.UserRepository;
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


    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
        User user = userRepository.findOne(id);
        Map<String,Object> result = new HashMap<>();
        result.put("username",user.getUsername());
        result.put("realName",user.getRealName());
        result.put("nickName",user.getNickName());
        result.put("birthday", DateUtils.getFormatDate(user.getBirthday(),DateUtils.PART_DATE_FORMAT));
        result.put("balance",user.getBalance());
        result.put("mobile",user.getMobile());
        result.put("sex",user.getSex());
        return success(result);
    }


    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        if (user==null){
            return fail(CustomResponseStatus._40000,"用户ID对应的记录不存在!");
        }
        if (!StringUtils.isEmpty(dto.getMobile())){
            user.setMobile(dto.getMobile());
        }
        if (!StringUtils.isEmpty(dto.getBirthday())){
            user.setBirthday(dto.getBirthday());
        }
        if (!StringUtils.isEmpty(dto.getNickName())){
            user.setNickName(dto.getNickName());
        }
        if (!StringUtils.isEmpty(dto.getEmail())){
            user.setEmail(dto.getEmail());
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


}
