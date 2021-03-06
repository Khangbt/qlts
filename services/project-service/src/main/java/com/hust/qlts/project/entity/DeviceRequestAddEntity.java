package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "device_request_add")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestAddEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;
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

    @Column(name = "PART_ID")
    private Long partId;


    @Column(name = "REASON")
    private String reason;

    @Column(name = "STATUS")
    private Integer status;
    @Version
    private Long version;

}
