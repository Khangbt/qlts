package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.SupplierDTO;
import com.hust.qlts.project.repository.jparepository.SupplierRepository;
import com.hust.qlts.project.service.SupplierService;
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

@RestController
@RequestMapping("/supplier")
@CrossOrigin("*")
public class SupplierControler {
    @Autowired
    private SupplierService supplierService;
    @Autowired
    SupplierRepository supplierRepository;
    private final Logger log = LogManager.getLogger(HumanResourcesController.class);
    // TanNV get HumanResourcesShowDTO danh sach
    @PostMapping("/searchSupplier")
    public ResponseEntity<?> searchHumanResources(@RequestBody SupplierDTO dto) {
        log.info("----------------api searchHumanResources nhan su-----------------");
        try {
            log.info("----------------api searchHumanResources nhan su Ok-----------------");
            return new ResponseEntity(supplierService.getPageSupplierSeach(dto), HttpStatus.OK);
        } catch (Exception e) {
            log.info("----------------api searchHumanResources nhan su fail-----------------");
            return new ResponseEntity("Lõi", HttpStatus.BAD_GATEWAY);
        }
    }

    @PostMapping("/add")
    public ResultResp createHR(@RequestBody SupplierDTO supplierDTO, HttpServletRequest request) {
        log.info("<--- api createNewHr: start,", supplierDTO);
        //lấy ra username đang đăng nhập
       // String username = authenService.getEmailCurrentlyLogged(request);

        try {
            return ResultResp.success(ErrorCode.CREATED_HR_OK, supplierService.create(supplierDTO));

        } catch (CustomExceptionHandler e) {
            if (e.getMsgCode().equalsIgnoreCase(ErrorCode.CREATED_HR_EXIST.getCode()))
                return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
        return ResultResp.badRequest(ErrorCode.CREATED_HR_FALSE);
    }

    @PutMapping(value = "/update")
    public ResultResp updateHr(@RequestBody SupplierDTO supplierDTO) {
        log.info("<-- api update Nhà cung cấp: start, ", supplierDTO);
        try {
            return ResultResp.success(ErrorCode.UPDATED_OK, supplierService.update(supplierDTO));

        } catch (Exception e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.SERVER_ERROR);
        }
    }

    @DeleteMapping("/deleteSupplier/{id}")
    public ResultResp deleteProject(@PathVariable("id") Long id) {

        log.info("----------------api delete nhà cung cấp-----------------");
        try {
            log.info("----------------api delete nhà cung cấp -----------------");
            if (supplierService.delete(id)) {

                log.info("----------------api delete nhà cung cấp su Ok-----------------");
                return ResultResp.success(ErrorCode.DELETE_HR_OK);
            } else {
                log.error("<--- DELTE SUPPLIER, HAVE ASSOCIATION");
                return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
            }
        } catch (CustomExceptionHandler e) {
            log.info("----------------api delete nhà cung câp faile-----------------");
            return ResultResp.badRequest(ErrorCode.DELETE_HR_FAIL);
        }
    }
    @GetMapping("/get-supplier-by-id/{id}")
    public ResultResp getOneById(@PathVariable("id") Long id) {
        log.info("<-- api updateHumanResources: start, ", id);
        try {
            return ResultResp.success(supplierService.findById(id));
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
            return ResultResp.success(supplierService.findByCode(code));

        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
    }
    @GetMapping(value = "/listpart")
    public ResponseEntity<?> getListPart(){
        return new ResponseEntity<>(supplierService.getListPart(),HttpStatus.OK);
    }

}
