package com.hust.qlts.project.controller;

import com.hust.qlts.project.service.NotificetionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notification")
@CrossOrigin("*")
public class NotificetionController {

    @Autowired
    private NotificetionService notificetionService;
    @GetMapping("/notiviceUser/{idHummer}")
    public ResponseEntity<?> getNotiviceUser(@PathVariable Long idHummer){


        return new ResponseEntity<>(notificetionService.getNoticeUser(idHummer), HttpStatus.OK);
    }
    @PutMapping("/notiviceUser/{id}")
    public ResponseEntity<?> updateNotiviceUser(@PathVariable Long id){


        return new ResponseEntity<>(notificetionService.updateNoticeUser(id), HttpStatus.OK);
    }
    @GetMapping("/notiviceAdmin/{partId}/{idHummer}")
    public ResponseEntity<?> getNotiviceAdmin(@PathVariable Long idHummer,@PathVariable Long partId){


        return new ResponseEntity<>(notificetionService.getNoticeAdmin(idHummer,partId), HttpStatus.OK);
    }
    @PutMapping("/notiviceAdmin/{id}/{idHummer}")
    public ResponseEntity<?> updateNotiviceAdmin(@PathVariable Long idHummer,@PathVariable Long id){


        return new ResponseEntity<>(notificetionService.updateNoticeAdmin(idHummer,id), HttpStatus.OK);
    }
    @PutMapping("/notiviceAdminAll/{id}/{idHummer}")
    public ResponseEntity<?> updateNotiviceAdminAll(@PathVariable Long idHummer,@PathVariable Long id){


        return new ResponseEntity<>(notificetionService.updateNoticeAdminAll(idHummer,id), HttpStatus.OK);
    }
    @GetMapping("/notiviceAdminAll/{idHummer}")
    public ResponseEntity<?> getNotiviceAdminAll(@PathVariable Long idHummer){


        return new ResponseEntity<>(notificetionService.getNoticeAdminAll(idHummer), HttpStatus.OK);
    }
}
