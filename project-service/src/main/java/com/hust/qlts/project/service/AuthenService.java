package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.UserLoginDTO;
import common.ObjectError;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface AuthenService {
    String login(UserLoginDTO userLoginDTO);

    String register(UserLoginDTO userLoginDTO);

    ObjectError forgotPassword(String email);

    String getEmailCurrentlyLogged(HttpServletRequest request);

    Long getIdHummer(HttpServletRequest request);

    Map<String,Object> getRole(HttpServletRequest request);
}
