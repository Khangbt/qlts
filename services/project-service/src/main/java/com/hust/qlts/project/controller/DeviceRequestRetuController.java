package com.hust.qlts.project.controller;
import com.hust.qlts.project.service.DeviceRequestRetuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;
import com.hust.qlts.project.dto.DeviceRequestRetuDto;

import java.util.UUID;

@RestController
@RequestMapping("/deviceRequestRetu")
@CrossOrigin("*")
public class DeviceRequestRetuController {
    @Autowired
    private DeviceRequestRetuService deviceRequestRetuService;
    @Autowired
    private SimpMessagingTemplate template;
    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceRequestRetuDto dto) {

        return new ResponseEntity<>(deviceRequestRetuService.searList(dto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceRequestRetuDto reqDto, @RequestParam Long id) {
        DeviceRequestRetuDto deviceDto = deviceRequestRetuService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceRequestRetuDto reqDto) {
        DeviceRequestRetuDto deviceDto = deviceRequestRetuService.craet(reqDto);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Long id) {

        if (deviceRequestRetuService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") Long code) {
        DeviceRequestRetuDto dto = deviceRequestRetuService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/browserRequest")
    public ResponseEntity<?> browserRequest(@RequestBody DeviceRequestRetuDto reqDto, @RequestParam Long id) {
        try {
            DeviceRequestRetuDto dto = deviceRequestRetuService.browserRequest(reqDto, id);
            if (dto == null) {
                return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
            }
            String c= UUID.randomUUID().toString();
            // Push notifications to front-end
            template.convertAndSend("/topic/notification", c);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lôi hệ thông", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/cancelRequest")
    public ResponseEntity<?> cancelRequest(@RequestBody DeviceRequestRetuDto reqDto, @RequestParam Long id) {
//        try {
        DeviceRequestRetuDto dto = deviceRequestRetuService.cancelRequest(reqDto, id);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(dto, HttpStatus.OK);
//        } catch (Exception e) {
//            return new ResponseEntity<>("Lôi hệ thông", HttpStatus.BAD_REQUEST);
//        }

    }
}
