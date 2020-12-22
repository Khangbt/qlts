package com.hust.qlts.project.service.impl;

import com.hust.qlts.project.dto.INotifition;
import com.hust.qlts.project.dto.NotificetionDto;
import com.hust.qlts.project.entity.NotificetionEntity;
import com.hust.qlts.project.repository.jparepository.NotificetionRepository;
import com.hust.qlts.project.service.NotificetionService;
import common.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class NotificetionServiceImpl implements NotificetionService {

    @Autowired
    private NotificetionRepository notificetionRepository;

    @Override
    public NotificetionDto getNoticeUser(Long idHummer) {
        NotificetionDto notificetionDto = new NotificetionDto();
        List<NotificetionDto.DataNotifice> list = new ArrayList<>();
        List<INotifition> notifitionList = notificetionRepository.getNotideUser(idHummer);
        int sum = 0;
        int sumSize = 0;
        for (INotifition notifition : notifitionList) {
            NotificetionDto.DataNotifice notifice = new NotificetionDto.DataNotifice();
            notifice.setId(notifition.getId());
            notifice.setStatus(notifition.getStatus());
            if (!notifition.getStatus()) {
                sum++;
            }
            notifice.setNote(notifition.getNameHand() + notifition.getConten());
            sumSize++;
            list.add(notifice);
        }
        notificetionDto.setSizeSum(sumSize);
        notificetionDto.setSum(sum);
        notificetionDto.setList(list);
        return notificetionDto;
    }

    @Override
    public NotificetionDto getNoticeAdmin(Long idHummer, Long partId) {
        NotificetionDto notificetionDto = new NotificetionDto();
        List<NotificetionDto.DataNotifice> list = new ArrayList<>();
        List<INotifition> notifitionList = notificetionRepository.getNotideAdmin(partId, idHummer);
        int sum = 0;
        int sumSize = 0;
        for (INotifition notifition : notifitionList) {
            NotificetionDto.DataNotifice dataNotifice = new NotificetionDto.DataNotifice();
            dataNotifice.setId(notifition.getId());
            if (notifition.getNote1().equals(idHummer) &&
                    (notifition.getTyle().equals(Constants.PHIEUMUON) || notifition.getTyle().equals(Constants.PHIEUTRA))
                            & notifition.getNote2() != null) {
                dataNotifice.setStatus(notifition.getStatus());
                if (!notifition.getStatus()) {
                    sum++;
                }
                dataNotifice.setNote(notifition.getNameHand() + notifition.getConten());
                sumSize++;
                list.add(dataNotifice);
            } else if (notifition.getTyle().equals(Constants.PHIEUMUON) || notifition.getTyle().equals(Constants.PHIEUTRA)) {
                dataNotifice.setNote(notifition.getNameCreat() + notifition.getConten());
                if (notifition.getListShow() == null) {
                    sum++;
                    dataNotifice.setStatus(false);
                } else {
                    List<String> data = Arrays.asList(notifition.getListShow().split(","));
                    if (data.contains(String.valueOf(idHummer))) {
                        dataNotifice.setStatus(true);
                    } else {
                        sum++;
                        dataNotifice.setStatus(false);
                    }
                }

                sumSize++;
                list.add(dataNotifice);

            } else if (notifition.getTyle().equals(Constants.PHIEUNHAPKHO)) {
                dataNotifice.setNote(notifition.getNameHand() + notifition.getConten());
                dataNotifice.setStatus(notifition.getStatus());
                if (notifition.getListShow() == null) {
                    sum++;
                    dataNotifice.setStatus(false);
                } else {
                    List<String> data = Arrays.asList(notifition.getListShow().split(","));
                    if (data.contains(String.valueOf(idHummer))) {
                        dataNotifice.setStatus(true);
                    } else {
                        sum++;
                        dataNotifice.setStatus(false);
                    }
                }

                sumSize++;
                list.add(dataNotifice);


            }
        }
        notificetionDto.setSizeSum(sumSize);
        notificetionDto.setSum(sum);
        notificetionDto.setList(list);
        return notificetionDto;
    }

    @Override
    public NotificetionDto getNoticeAdminAll(Long idHummer) {
        NotificetionDto notificetionDto = new NotificetionDto();
        List<NotificetionDto.DataNotifice> list = new ArrayList<>();
        List<INotifition> notifitionList = notificetionRepository.getNotideAdminAll(idHummer);
        int sum = 0;
        int sumSize = 0;

        for (INotifition notifition : notifitionList) {
            NotificetionDto.DataNotifice dataNotifice = new NotificetionDto.DataNotifice();
            dataNotifice.setId(notifition.getId());

            if(notifition.getTyle().equals(Constants.PHIEUNHAPKHO)){
                dataNotifice.setNote(notifition.getNameCreat() + notifition.getConten());
                if (notifition.getListShow() == null) {
                    sum++;
                    dataNotifice.setStatus(false);
                } else {
                    List<String> data = Arrays.asList(notifition.getListShow().split(","));
                    if (data.contains(String.valueOf(idHummer))) {
                        dataNotifice.setStatus(true);
                    } else {
                        sum++;
                        dataNotifice.setStatus(false);
                    }
                }

                sumSize++;
                list.add(dataNotifice);
            }else {
                dataNotifice.setId(notifition.getId());
                dataNotifice.setStatus(notifition.getStatus());
                if (!notifition.getStatus()) {
                    sum++;
                }
                dataNotifice.setNote(notifition.getNameHand() + notifition.getConten());
                sumSize++;
                list.add(dataNotifice);
            }

        }


        notificetionDto.setSizeSum(sumSize);
        notificetionDto.setSum(sum);
        notificetionDto.setList(list);
        return notificetionDto;
    }

    @Override
    public Object updateNoticeUser(Long id) {
        if (!notificetionRepository.findById(id).isPresent()) {
            return null;
        }
        NotificetionEntity entity = notificetionRepository.findById(id).get();
        entity.setStatus(true);
        notificetionRepository.save(entity);
        return notificetionRepository.save(entity);
    }

    @Override
    public Object updateNoticeAdmin(Long idHumme, Long id) {
        if (!notificetionRepository.findById(id).isPresent()) {
            return null;
        }
        NotificetionEntity entity = notificetionRepository.findById(id).get();
        if (entity.getNote1().equals(idHumme)) {
            entity.setStatus(true);
        } else {
            if (entity.getListShow() == null) {
                entity.setListShow(String.valueOf(idHumme));
            } else {
                List<String> data = Arrays.asList(entity.getListShow().split(","));
                if (!data.contains(String.valueOf(idHumme))) {
                    entity.setListShow(entity.getListShow() + "," + idHumme);
                }
            }
        }
        notificetionRepository.save(entity);
        return notificetionRepository.save(entity);
    }

    @Override
    public Object updateNoticeAdminAll(Long idHumme, Long id) {
        if (!notificetionRepository.findById(id).isPresent()) {
            return null;
        }
        NotificetionEntity entity = notificetionRepository.findById(id).get();
        if (entity.getNote1().equals(idHumme)) {
            entity.setStatus(true);
        } else {
            if (entity.getListShow() == null) {
                entity.setListShow(String.valueOf(idHumme));
            } else {
                List<String> data = Arrays.asList(entity.getListShow().split(","));
                if (!data.contains(String.valueOf(idHumme))) {
                    entity.setListShow(entity.getListShow() + "," + idHumme);
                }
            }
        }
        notificetionRepository.save(entity);
        return notificetionRepository.save(entity);
    }
}
