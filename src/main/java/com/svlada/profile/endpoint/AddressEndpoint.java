package com.svlada.profile.endpoint;

import com.svlada.common.WebUtil;
import com.svlada.common.request.CustomResponse;
import com.svlada.entity.Address;
import com.svlada.entity.User;
import com.svlada.user.repository.AddressRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import static com.svlada.common.request.CustomResponseBuilder.success;

@RestController
@RequestMapping("/api/address")
public class AddressEndpoint {

    @Autowired
    private AddressRepository addressRepository;

    /**
     * 添加收货地址
     *
     * @return
     */
    @PostMapping("/add")
    public CustomResponse add(@RequestBody Address address) {
        User user = WebUtil.getCurrentUser();
        address.setUserId(user.getId());
        addressRepository.save(address);
        return success();
    }

    /**
     * 编辑收货地址
     * @return
     */
    @PutMapping("/update")
    public CustomResponse update(@RequestBody Address dto) {
        Address address = addressRepository.findOne(dto.getId());
        if (StringUtils.isEmpty(dto.getAddress())) {
            address.setAddress(dto.getAddress());
        }
        if (StringUtils.isEmpty(dto.getArea())) {
            address.setArea(dto.getArea());
        }
        if (StringUtils.isEmpty(dto.getCity())) {
            address.setCity(dto.getCity());
        }
        if (StringUtils.isEmpty(dto.getDefault())) {
            address.setDefault(dto.getDefault());
        }
        if (StringUtils.isEmpty(dto.getMobile())) {
            address.setMobile(dto.getMobile());
        }
        if (StringUtils.isEmpty(dto.getName())) {
            address.setName(dto.getName());
        }
        if (StringUtils.isEmpty(dto.getPhone())) {
            address.setPhone(dto.getPhone());
        }
        if (StringUtils.isEmpty(dto.getPcaDetail())) {
            address.setPcaDetail(dto.getPcaDetail());
        }
        if (StringUtils.isEmpty(dto.getProvince())) {
            address.setProvince(dto.getProvince());
        }
        if (StringUtils.isEmpty(dto.getZip())) {
            address.setZip(dto.getZip());
        }
        return success();
    }


    /**
     * 删除收货地址
     * @return
     */
    @PutMapping("/delete/{id}")
    public CustomResponse delete(@PathVariable("id") Long id) {
        User user = WebUtil.getCurrentUser();
        addressRepository.deleteOneByIdAndUserId(id,user.getId());
        return success();
    }

}
