package com.svlada.profile.endpoint;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.profile.endpoint.dto.UserDto;
import com.svlada.entity.User;
import com.svlada.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/user")
public class UserEndpoint {

    @Autowired
    private UserRepository userRepository;


    @PostMapping(value = "/register")
    public CustomResponse register(@RequestBody UserDto dto) {
        User user = userRepository.findOne(dto.getId());
        userRepository.save(user);
        return success();
    }

    @GetMapping("/get/{id}")
    public CustomResponse get(@PathVariable("id") Long id){
        User user = userRepository.findOne(id);
        return success(user);
    }

    @PutMapping(value = "/update")
    public CustomResponse update(@RequestBody UserDto dto) {
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

    /**
     * 查看用户合伙人列表
     * @return
     */
    @GetMapping("/list/partner")
    public CustomResponse partner(){
        User user = WebUtil.getCurrentUser();
        return success(user);
    }
}
