package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.request.DeviceGroupReqDto;
import com.hust.qlts.project.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deviceGroup")
@CrossOrigin("*")
public class DeviceGroupController {
    @Autowired
    private DeviceGroupService deviceGroupService;

    @PostMapping("/create")
    public ResponseEntity<?> creatDeviceGroup(@RequestBody DeviceGroupReqDto dto) {
        Object o = deviceGroupService.creatDeviceGoup(dto);

        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateDeviceGroup(@RequestBody DeviceGroupReqDto dto, @PathVariable("id") Integer id) {
        Object o = deviceGroupService.updateDeviceGroup(dto, id);

        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDeviceGroup(@PathVariable("id") Integer id) {
        return new ResponseEntity<>("", HttpStatus.OK);

    }

    @GetMapping("/check/{code}")
    public ResponseEntity<?> checkCodeDeviceGroup(@PathVariable("code") String code) {
        if (deviceGroupService.checkCode(code)) {
            return new ResponseEntity<>("ok", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("NO", HttpStatus.BAD_GATEWAY);

        }
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchDeviceGroup(@RequestBody DeviceGroupReqDto reqDto) {

        return new ResponseEntity<>(deviceGroupService.searchAsser(reqDto), HttpStatus.OK);
    }
}
