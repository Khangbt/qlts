package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.service.DeviceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/device")
@CrossOrigin("*")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceDto reqDto) {

        return new ResponseEntity<>(deviceService.searList(reqDto), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceDto reqDto, @RequestParam Long id) {
        DeviceDto deviceDto = deviceService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceDto reqDto) {
        DeviceDto deviceDto = deviceService.craet(reqDto);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Integer id) {

        if (deviceService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") String code) {
        DeviceFindDto dto = deviceService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping("/getListId")
    public ResponseEntity<?> getList(@RequestParam List<Long> id) {
        return new ResponseEntity(deviceService.getList(id), HttpStatus.OK);
    }
    @GetMapping("/getListyId")
    public ResponseEntity<?> getListIdHummer(@RequestParam Long id) {
        return new ResponseEntity(deviceService.getListIdHumme(id), HttpStatus.OK);
    }
}
