package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.RequestDto;
import com.hust.qlts.project.service.RequestService;
import common.ErrorCode;
import common.ResultResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/request")
@CrossOrigin("*")
public class RequestControlle {
    @Autowired
    private RequestService requestService;

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Long id) {
        if (requestService.delete(id)) {
            return ResultResp.success("xóa thành công");
        }

        return ResultResp.badRequest(ErrorCode.DELETE_DEPARMENTS_ERROR);
    }

    @PostMapping("/srearh")
    public ResponseEntity<?> srearh(@RequestBody RequestDto dto) {
        return new ResponseEntity<>(requestService.sreah(dto), HttpStatus.OK);
    }
}
