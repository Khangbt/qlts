package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.dto.DeviceRequestAddDto;
import com.hust.qlts.project.dto.DeviceRequestDTO;
import com.hust.qlts.project.service.DeviceRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/deviceRequest")
@CrossOrigin("*")
public class DeviceRequestController {
    @Autowired
    private DeviceRequestService deviceRequestService;
    @Autowired
    private SimpMessagingTemplate template;
    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceRequestDTO dto) {

        return new ResponseEntity<>(deviceRequestService.searList(dto), HttpStatus.OK);
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceRequestDTO reqDto, @RequestParam Long id) {
        DeviceRequestDTO deviceDto = deviceRequestService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceRequestDTO reqDto) {
        DeviceRequestDTO deviceDto = deviceRequestService.craet(reqDto);
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

        if (deviceRequestService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") Long code) {
        DeviceRequestDTO dto = deviceRequestService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
    @PreAuthorize("hasAnyRole( 'ROLE_ADMINPART')")

    @PostMapping("/browserRequest")
    public ResponseEntity<?> browserRequest(@RequestBody DeviceRequestDTO reqDto, @RequestParam Long id) {
        try {
            DeviceRequestDTO dto = deviceRequestService.browserRequest(reqDto, id);
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
    @PreAuthorize("hasAnyRole('ROLE_ADMINPART')")

    @PostMapping("/cancelRequest")
    public ResponseEntity<?> cancelRequest(@RequestBody DeviceRequestDTO reqDto, @RequestParam Long id) {
//        try {
        DeviceRequestDTO dto = deviceRequestService.cancelRequest(reqDto, id);
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
