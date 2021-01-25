package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.HumanResourcesDTO;
import com.hust.qlts.project.entity.DeviceRequestAddEntity;
import com.hust.qlts.project.entity.DeviceRequestEntity;
import com.hust.qlts.project.entity.DeviceRequestRetuEntitty;
import com.hust.qlts.project.service.HumanResourcesService;
import com.hust.qlts.project.service.SendMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
@Service
public class SendMailServiceImpl implements SendMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Autowired
    private HumanResourcesService humanResourcesService;
    @Override
    @Async
    public void sendMailCandRequestGood(DeviceRequestEntity entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU MƯỢN THIẾU BỊ ĐÃ ĐƯỢC DUYỆT", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu mượn thiết bị với mã số:\n" +entity.getCode()+ "đã đươc duyệt\n"+
                    "Người Duyệt:\n" +humanResources.getFullName()+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }

    @Override
    @Async
    public void sendMailCandRequestError(DeviceRequestEntity entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU MƯỢN THIẾU BỊ ĐÃ HỦY", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu mượn thiết bị với mã số:\n" +entity.getCode()+ "đã không đươc duyệt\n"+
                    "Người Duyệt:" +humanResources.getFullName()+"\n"+
                    "Lý do"+entity.getReason()+"\n"+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }

    @Override
    @Async

    public void sendMailCandRequestAddGood(DeviceRequestAddEntity entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU NHẬP THIẾT BỊ ĐÃ ĐƯỢC DUYỆT", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu nhập thiết bị với mã số:\n" +entity.getCode()+ "đã đươc duyệt\n"+
                    "Người Duyệt:\n" +humanResources.getFullName()+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }

    @Override
    @Async

    public void sendMailCandRequestAddError(DeviceRequestAddEntity entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU NHẬP THIẾU BỊ ĐÃ HỦY", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu nhập thiết bị với mã số:\n" +entity.getCode()+ "đã không đươc duyệt\n"+
                    "Người Duyệt:" +humanResources.getFullName()+"\n"+
                    "Lý do"+entity.getReason()+"\n"+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }

    @Override
    @Async

    public void sendMailCandRequestRetuGood(DeviceRequestRetuEntitty entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU TRẢ THIẾT BỊ ĐÃ ĐƯỢC DUYỆT", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu trả thiết bị với mã số:\n" +entity.getCode()+ "đã đươc duyệt\n"+
                    "Người Duyệt:\n" +humanResources.getFullName()+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }

    @Override
    @Async

    public void sendMailCandRequestRetuError(DeviceRequestRetuEntitty entity) {
        HumanResourcesDTO humanResourcesEntity=humanResourcesService.findById(entity.getCreatHummerId());
        HumanResourcesDTO humanResources=humanResourcesService.findById(entity.getHandlerHummerId());


        try {
            MimeMessage message = javaMailSender.createMimeMessage();
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(humanResourcesEntity.getEmail()));
            message.setSubject("PHIẾU YÊU CẦU TRẢ THIẾU BỊ ĐÃ HỦY", "UTF-8");
            String subject = "Kính gửi anh/chị,\n\n" +
                    "Phiếu yêu cầu trả thiết bị với mã số:\n" +entity.getCode()+ "đã không đươc duyệt\n"+
                    "Người Duyệt:" +humanResources.getFullName()+"\n"+
                    "Lý do"+entity.getReason()+"\n"+
                    "Trân trọng!";
            message.setText(subject, "UTF-8");
            javaMailSender.send(message);

        } catch (MessagingException | MailException ex) {
        }
    }
}
