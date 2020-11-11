package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "DEVICE_REQUEST")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestAddEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CREAT_DATE")
    private Date creatDate;

    @Column(name = "PROCESSING_DATE")
    private Date processingDate;

    @Column(name = "CREAT_HUMMER_ID")
    private Long creatHummerId;

    @Column(name = "HANDLER_HUMMER_ID")
    private Long handlerHummerId;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Integer status;

}
