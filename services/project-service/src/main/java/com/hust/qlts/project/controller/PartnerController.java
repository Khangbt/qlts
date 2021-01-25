package com.hust.qlts.project.controller;

import com.hust.qlts.project.dto.PartnerDTO;
import com.hust.qlts.project.service.PartService;
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
@RequestMapping("/partner")
@CrossOrigin("*")
public class PartnerController {
    private final Logger log = LogManager.getLogger(HumanResourcesController.class);

    @Autowired private PartService partService;

    @PostMapping("/searchPart")
    public ResponseEntity<List<PartnerDTO>> searchPart(@RequestBody PartnerDTO dto) {
        log.info("----------------api searchHumanResources nhan su-----------------");
        try {
            log.info("----------------api searchHumanResources nhan su Ok-----------------");
            return new ResponseEntity(partService.getPagePartSeach(dto), HttpStatus.OK);
        } catch (Exception e) {
            log.info("----------------api searchHumanResources nhan su fail-----------------");
            throw e;
        }
    }

    @PostMapping("/add")
    public ResultResp createHR(@RequestBody PartnerDTO partnerDTO, HttpServletRequest request) {
        log.info("<--- api createNewHr: start,", partnerDTO);


        try {
            return ResultResp.success(ErrorCode.CREATED_HR_OK, partService.create(partnerDTO));

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
            return ResultResp.success(partService.findById(id));
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
            return ResultResp.success(partService.findByCode(code));

        } catch (CustomExceptionHandler e) {
            log.error("<--- api updateHumanResources: error, ");
            return ResultResp.badRequest(ErrorCode.CREATED_HR_EXIST);
        }
    }

    @DeleteMapping(value = "/delete/{id}")
    public ResponseEntity<?> deletePartId(@PathVariable("id") Long id){
        if(partService.delete(id)){
            return ResultResp.success("xóa thành công");
        }else {
            return ResultResp.badRequest(ErrorCode.DELETE_DEPARMENTS_ERROR);
        }



    }
}
