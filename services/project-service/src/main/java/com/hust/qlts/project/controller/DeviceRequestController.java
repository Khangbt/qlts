package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.service.DeviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/deviceRequest")
@CrossOrigin("*")
public class DeviceRequestController {
    @Autowired
    private DeviceRequestService deviceRequestService;
    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceRequestDTO dto) {

        return new ResponseEntity<>(deviceRequestService.searList(dto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceRequestDTO reqDto, @RequestParam Long id) {
        DeviceDto deviceDto = deviceRequestService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }
    @PutMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceRequestDTO reqDto) {
        DeviceRequestDTO deviceDto = deviceRequestService.craet(reqDto);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Integer id) {

        if (deviceRequestService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }
    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") String code) {
        DeviceRequestDTO dto=deviceRequestService.getFindByCode(code);
        if(dto==null){
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
