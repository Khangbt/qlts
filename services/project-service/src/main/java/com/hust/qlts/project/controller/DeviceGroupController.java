package com.hust.qlts.project.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.dto.DeviceGroupDto;
import com.hust.qlts.project.dto.DeviceGroupFindDto;
import com.hust.qlts.project.dto.DeviceGroupListDto;
import com.hust.qlts.project.service.DeviceGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deviceGroup")
@CrossOrigin("*")
public class DeviceGroupController {
    @Autowired
    private DeviceGroupService deviceGroupService;

    @PostMapping( "/create")
    public ResponseEntity<?> creatDeviceGroup(
                                              @RequestBody DeviceGroupDto dto) {
        ObjectMapper mapper=new ObjectMapper();
        Object o = deviceGroupService.creatDeviceGoup(dto);
//        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
//        String uploadDir = "user-photos/" + multipartFile.getName();
//            try {
//                FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }

        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    @PostMapping("/update/{id}")
    public ResponseEntity<?> updateDeviceGroup(@RequestBody DeviceGroupDto dto, @PathVariable("id") Integer id) {
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
    public ResponseEntity<?> searchDeviceGroup(@RequestBody DeviceGroupDto reqDto) {

        return new ResponseEntity<>(deviceGroupService.searchAsser(reqDto), HttpStatus.OK);
    }

    @GetMapping("/findbyid/{id}")
    public ResponseEntity<?> getFindByCode(@PathVariable("id") Long code) {
        DeviceGroupFindDto dto = deviceGroupService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @GetMapping("/getListPart")
    private ResponseEntity<?> getListPart(@RequestParam("id") Integer id){
        List<DeviceGroupListDto> list=deviceGroupService.getList(id);
        if(list.size()==0){
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);

        }
        return new ResponseEntity<>(list, HttpStatus.OK);

    }
    @GetMapping("/searhListAll")
    private  ResponseEntity<?> getAllGroup(){
        return new ResponseEntity<>(deviceGroupService.listAll(),HttpStatus.OK);
    }
}
