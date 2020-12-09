package com.hust.qlts.project.controller;

import com.hust.qlts.project.common.CoreUtils;
import com.hust.qlts.project.dto.DeviceDto;
import com.hust.qlts.project.dto.DeviceFindDto;
import com.hust.qlts.project.service.DeviceService;
import common.CommonUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/device")
@CrossOrigin("*")
public class DeviceController {
    @Autowired
    private DeviceService deviceService;

    @PostMapping("/search")
    public ResponseEntity<?> searchDevice(@RequestBody DeviceDto reqDto) {

        return new ResponseEntity<>(deviceService.searList(reqDto), HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody DeviceDto reqDto, @RequestParam Long id) {
        DeviceDto deviceDto = deviceService.update(reqDto, id);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @PostMapping("/creat")
    public ResponseEntity<?> CreatDevice(@RequestBody DeviceDto reqDto) {
        DeviceDto deviceDto = deviceService.craet(reqDto);
        if (deviceDto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(deviceDto, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDevice(@PathVariable("id") Integer id) {

        if (deviceService.deleteDevice(id)) {
            return new ResponseEntity<>("OK", HttpStatus.OK);
        }
        return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
    }

    @GetMapping("/findbyid/{code}")
    public ResponseEntity<?> getFindByCode(@PathVariable("code") String code) {
        DeviceFindDto dto = deviceService.getFindByCode(code);
        if (dto == null) {
            return new ResponseEntity<>("Lôi", HttpStatus.BAD_GATEWAY);
        }
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PostMapping("/getListId")
    public ResponseEntity<?> getList(@RequestParam Long Request,@RequestParam Long partId) {
        return new ResponseEntity(deviceService.getList(Request,partId), HttpStatus.OK);
    }
    @GetMapping("/getListyId")
    public ResponseEntity<?> getListIdHummer(@RequestParam Long id) {
        return new ResponseEntity(deviceService.getListIdHumme(id), HttpStatus.OK);
    }

    @GetMapping("/getListyReturm")
        public ResponseEntity<?> getListReturn(@RequestParam Long idHummer,@RequestParam Long partId) {
        return new ResponseEntity(deviceService.getIdHummeReti(idHummer, partId), HttpStatus.OK);
    }

    @GetMapping("/getListFormRe")
    public ResponseEntity<?> getListFormRe(@RequestParam Long idRequest) {
        return new ResponseEntity(deviceService.getListReturn(idRequest), HttpStatus.OK);
    }
    @GetMapping("/getListFormReBrowser")
    public ResponseEntity<?> getListFormReBrowser(@RequestParam Long idRequestRe) {
        return new ResponseEntity(deviceService.getListReturnbor(idRequestRe), HttpStatus.OK);
    }


    @GetMapping("/checkcode")
    public ResponseEntity<?> checkcode(@RequestParam String code) {
        if(deviceService.checkCode(code)){
            return new ResponseEntity("ok", HttpStatus.OK);
        }
        return new ResponseEntity("Lỗi", HttpStatus.BAD_GATEWAY);
    }
    @GetMapping("/getListyReturmById")
    public ResponseEntity<?> getListyReturmById(@RequestParam Long idHummer,@RequestParam Long partId,@RequestParam Long idReque) {
        return new ResponseEntity(deviceService.getIdHummeRetiByReque(idHummer, partId,idReque), HttpStatus.OK);
    }
    @GetMapping("/getListyReturmByIdStus")
    public ResponseEntity<?> getListyReturmById(@RequestParam Long idReque) {
        return new ResponseEntity(deviceService.getIdHummeRetiByIdStaue(idReque), HttpStatus.OK);
    }


    @GetMapping("/export")
    public ResponseEntity<?> exportt(@RequestBody DeviceDto reqDto){
        return new ResponseEntity<>("ok",HttpStatus.OK);
    }

    @RequestMapping(value = "/doImport", method = RequestMethod.POST)
    public ResponseEntity<?> importExcel(@RequestBody DeviceDto reqDto) throws IOException {
//        HttpHeaders headers = new HttpHeaders();
//        byte[] result = deviceService.exportXel(reqDto);
//        if (!Objects.isNull(result)) {
//
//            String fileNameExcel = "BÁO CÁI" +
//                    CoreUtils.castDateToStringByPattern(new Date(), "yyMMdd") + "_" +
//                    CoreUtils.castDateToStringByPattern(new Date(), "hhmmss") + ".xlsx";
//
//            headers.add("File", fileNameExcel);
//
//            headers.add("Content-Disposition", "attachment; filename=" + fileNameExcel);
//            headers.add("Access-Control-Expose-Headers", "File");
//            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
//
//            return ResponseEntity.ok().headers(headers).body(result);
//        }
//
//        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        try {
            byte[] result = deviceService.exportXel(reqDto);

            HttpHeaders headers = new HttpHeaders();

//            String fileName = CommonUtils.getFileNameReportUpdate("IMPORT_DanhSachNhanSu");
            String fileNameExcel = "BÁO CÁI" +
                    CoreUtils.castDateToStringByPattern(new Date(), "yyMMdd") + "_" +
                    CoreUtils.castDateToStringByPattern(new Date(), "hhmmss") + ".xlsx";
//
            headers.add("File", fileNameExcel);
            headers.add("Content-Disposition", "attachment; filename=" + fileNameExcel);
            headers.add("Access-Control-Expose-Headers", "File");
            headers.setContentType(MediaType.parseMediaType("application/vnd.ms-excel"));
            return new ResponseEntity<>(result,headers,HttpStatus.OK);

        } catch (Exception e) {

            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }
    }
}
