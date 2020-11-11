package com.hust.qlts.project.controller;



import com.hust.qlts.project.common.CoreUtils;
import com.hust.qlts.project.dto.AssetDTO;
import com.hust.qlts.project.service.AsserService;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/asset")
@CrossOrigin("*")
public class AssetControler {
    private final Logger log = LogManager.getLogger(HumanResourcesController.class);

    @Autowired
    AsserService asserService;

    @PostMapping ("/searchAseer")
     public ResponseEntity<List<AssetDTO>> searchAseer(@RequestBody AssetDTO assetDTO){
        log.info("----------------api searchAsser-----------------");
        try {
            log.info("----------------api searchAsser Ok-----------------");

            return new ResponseEntity(asserService.searchAsser(assetDTO), HttpStatus.OK);

        }catch (Exception e){
            log.info("----------------api searchAsser thất bại-----------------");

            throw  e;
        }
    }
    @GetMapping(value = "/doExport")
    public ResponseEntity<?> importExcel(@RequestParam("file") MultipartFile file) throws IOException {
        byte[] bytes = null;
        try {
            String file1 = "/templates/IMPORT_DanhSachNhanSu.xlsx";
            InputStream inputStream = new ClassPathResource(file1).getInputStream();
            bytes = IOUtils.toByteArray(inputStream);
        }catch (IOException e){
            log.error(e.getMessage(), e);
            return new ResponseEntity<>(HttpStatus.ALREADY_REPORTED);
        }



        HttpHeaders headers = new HttpHeaders();
        //byte[] result = asserService.exportExcel(bytes);
      //  if (!Objects.isNull(result)) {

            String fileNameExcel = "KQ_IMPORT_NHAN_SU" +
                    CoreUtils.castDateToStringByPattern(new Date(), "yyMMdd") + "_" +
                    CoreUtils.castDateToStringByPattern(new Date(), "hhmmss") + ".xlsx";

            headers.add("File", fileNameExcel);
//            headers.add("totalRecord", String.valueOf(result.getTotalRecord()));
//            headers.add("successRecord", String.valueOf(result.getImportSuccessRecord()));
            headers.add("Content-Disposition", "attachment; filename=" + fileNameExcel);
            headers.add("Access-Control-Expose-Headers", "File");
            headers.add("Access-Control-Expose-Headers", "totalRecord");
            headers.add("Access-Control-Expose-Headers", "successRecord");
            //return ResponseEntity.ok().headers(headers).body(result);
        //}
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }


}
