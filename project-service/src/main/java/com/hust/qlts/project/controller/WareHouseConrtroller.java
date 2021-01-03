package com.hust.qlts.project.controller;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hust.qlts.project.dto.WarehouseDTO;
import com.hust.qlts.project.service.WarehouseService;
import common.ErrorCode;
import common.ResultResp;
import exception.CustomExceptionHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/warehouse")
@CrossOrigin("*")
//chu y may cai ten dat cho classs
public class WareHouseConrtroller {
    private final Logger log = LogManager.getLogger(HumanResourcesController.class);

    @Autowired
    private WarehouseService warehouseService;

    @PostMapping("/searchWarehouse")
    public ResponseEntity<List<WarehouseDTO>> searchPart(@RequestBody WarehouseDTO dto) {
        log.info("----------------api searchHumanResources nhan su-----------------");
        try {
            log.info("----------------api searchHumanResources nhan su Ok-----------------");
            return new ResponseEntity(warehouseService.getPageWarehouseSeach(dto), HttpStatus.OK);
        } catch (Exception e) {
            log.info("----------------api searchHumanResources nhan su fail-----------------");
            throw e;
        }
    }

    @PostMapping("/add")
    public ResultResp createHR(@RequestBody Map dto, HttpServletRequest request) {
        log.info("<--- api createNewHr: start,", dto);
        //lấy ra username đang đăng nhập
        // String username = authenService.getEmailCurrentlyLogged(request);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        WarehouseDTO warehouseDTO = objectMapper.convertValue(dto, WarehouseDTO.class);
        warehouseDTO.setIdWare((Integer) dto.get("iD"));
        try {
            return ResultResp.success(ErrorCode.CREATED_HR_OK, warehouseService.create(warehouseDTO));

        } catch (CustomExceptionHandler e) {
            if (e.getMsgCode().equalsIgnoreCase(ErrorCode.CREATED_HR_EXIST.getCode()))
                return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
        return ResultResp.badRequest(ErrorCode.CREATED_HR_FALSE);
    }

    @GetMapping("/get-supplier-by-id/{id}")
    public ResultResp getOneById(@PathVariable("id") Long id) {
        log.info("<-- api updateHumanResources: start, ", id);
        try {
            return ResultResp.success(warehouseService.findById(id));
        } catch (CustomExceptionHandler e) {
            return ResultResp.badRequest(ErrorCode.USERNAME_NOT_FOUND);
        } catch (Exception e) {
            log.error("<--- api find HumanResources: error, ");
            e.printStackTrace();
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }

    }

    @GetMapping(value = "/check-usercode/{code}")
    public ResultResp checkUsername(@PathVariable("code") String code) {
        log.info("<-- api check duplicate code: start, ");
        try {
            return ResultResp.success(warehouseService.findByCode(code));

        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
    }

    @DeleteMapping("/deletewarehouse/{id}")
    public ResultResp deleteProject(@PathVariable("id") Long id) {

        log.info("----------------api delete kho-----------------");
        try {
            log.info("----------------api delete nhà cung cấp -----------------");
            if (warehouseService.delete(id)) {

                log.info("----------------api delete kho su Ok-----------------");
                return ResultResp.success(ErrorCode.DELETE_HR_OK);
            } else {
                log.error("<--- DELTE SUPPLIER, HAVE ASSOCIATION");
                return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
            }
        } catch (CustomExceptionHandler e) {
            log.info("----------------api delete kho faile-----------------");
            return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
        }
    }

    @GetMapping("/searhPart")
    public ResponseEntity<?> getListWareByPart(@RequestParam Long id) {
        return new ResponseEntity<>(warehouseService.findByPart(id), HttpStatus.OK);

    }
    @GetMapping("/checkCode")
    public ResponseEntity<?> checkCode(@RequestParam String id) {
        if(warehouseService.checkCode(id)){
            return new ResponseEntity<>("OK", HttpStatus.OK);

        }
        return new ResponseEntity<>("Lỗi", HttpStatus.BAD_REQUEST);

    }


}
