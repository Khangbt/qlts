package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.DeviceRequestAddDto;
import com.hust.qlts.project.service.DeviceRequestAddService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deviceRequestAdd")
@CrossOrigin("*")
public class DeviceRequestAddController {
    @Autowired
    private DeviceRequestAddService deviceRequestAddService;

    @Autowired
    private SimpMessagingTemplate template;

    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceRequestAddDto dto) {

        return new ResponseEntity<>(deviceRequestAddService.searList(dto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceRequestAddDto reqDto, @RequestParam Long id) {
        DeviceRequestAddDto deviceDto = deviceRequestAddService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceRequestAddDto reqDto) {
        DeviceRequestAddDto deviceDto = deviceRequestAddService.craet(reqDto);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c=UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Long id) {

        if (deviceRequestAddService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        String c=UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") Long code) {
        DeviceRequestAddDto dto = deviceRequestAddService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/browserRequestAdd")
    public ResponseEntity<?> browserRequest(@RequestBody DeviceRequestAddDto reqDto, @RequestParam Long id) {
        try {
            DeviceRequestAddDto dto = deviceRequestAddService.browserRequest(reqDto, id);
            if (dto == null) {
                return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
            }
            String c=UUID.randomUUID().toString();
            // Push notifications to front-end
            template.convertAndSend("/topic/notification", c);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Lôi hệ thông", HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/cancelRequestAdd")
    public ResponseEntity<?> cancelRequest(@RequestBody DeviceRequestAddDto reqDto, @RequestParam Long id) {
//        try {
        DeviceRequestAddDto dto = deviceRequestAddService.cancelRequest(reqDto, id);
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
