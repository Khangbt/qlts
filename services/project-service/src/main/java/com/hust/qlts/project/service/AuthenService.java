package com.hust.qlts.project.service;

import com.hust.qlts.project.dto.UserLoginDTO;
import common.ObjectError;

import javax.servlet.http.HttpServletRequest;

public interface AuthenService {
    String login(UserLoginDTO userLoginDTO);

    String register(UserLoginDTO userLoginDTO);

    ObjectError forgotPassword(String email);

    String getEmailCurrentlyLogged(HttpServletRequest request);
}
