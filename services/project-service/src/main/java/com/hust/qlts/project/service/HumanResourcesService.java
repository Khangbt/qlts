package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.*;
import com.hust.qlts.project.entity.HumanResourcesEntity;
import com.hust.qlts.project.dto.*;
import common.ResultResp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface HumanResourcesService {
    List<HumanResourcesDTO> getListHumanResourceByNameOrCode(DTOSearch dto);

    HumanResourcesDTO getUserInfo(String username);

    List<HumanResourcesDTO> getListHumanResourceByProjectID(long i);

    //tinpd
    /* Map<String,Object> updateDataHumanResources(Long id);*/
    // end
    /*duc service*/
    HumanResourcesDTO create(String username, HumanResourcesDTO humanResourcesDTO);

    void sendMailChangeEmail(HumanResourcesDTO humanResourcesDTO, HumanResourcesEntity oldEmail);

    HumanResourcesDTO update(HumanResourcesDTO humanResourcesDTO);



    List<IPartDTO> getPart();



    List<IPositionDTO> getPosition();

    HumanResourcesDTO findById(Long Id);

    List<HistoryDTO> getHumanHistory();

    List<HistoryDTO> getHumanHistoryById(Long Id);

    void humanHistory(String username, HumanResourcesEntity oldEntity, HumanResourcesEntity newEntity);

    HumanResourcesDTO findByCode(String code);

    /* HumanResourcesDTO checkUserCode(String code);*/

    List<HumanResourcesDTO> findByEmail(String email);

    HumanResourcesDTO getByEmail(String email);

    ResultResp resetPassword(Long userID, String usernameAdmin);

    /*end */

    //TanNV
    DataPage<HumanResourcesShowDTO> getPageHumanResourcesSeach(HumanResourcesShowDTO dto);

    Boolean deleteHumanResources(Long id,String name);

    Boolean lockHumanResources(Long id,String name);

    Integer getActiveFromHumanResourceId(Long id);

    List<HumanResourcesDTO> getHumanResources(DTOSearch dto);

    // end TanNV
    //hungnv change password
    Long changePassword(HumanResourcesDTO humanResourcesDTO);

    Long checkPassword(HumanResourcesDTO humanResourcesDTO);

    /*
     *@author ThaoLC - IIST
     *created on 9/9/2020
     */
    List<HumanResourcesDTO> getListHumanResources(Long projectId);

    String getLeaderNameFromProject(Long projectId);

    byte[] getFileImport(List<HumanResourcesImportDTO> list);

    byte[] importExcel(MultipartFile file) throws IOException;

    List<HumanResourcesDTO> findAllHumanResourcesByPositionId(Long id);

}
