package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.DeviceRequestRetuDto;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * @author SyPT - IIST
 * created on 7/11/2020
 */
@RestController
@RequestMapping("/test")
@CrossOrigin("*")
public class TestController {
    private final Logger log = LogManager.getLogger(TestController.class);

    @Autowired
    private SimpMessagingTemplate template;
    @PostMapping("/get")
    public ResponseEntity<?> searchDevice() {
        String c= UUID.randomUUID().toString();
        // Push notifications to front-end
        template.convertAndSend("/topic/notification", c);
        return new ResponseEntity<>("okkkk", HttpStatus.OK);
    }

}
