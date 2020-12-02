package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "DEVICE_REQUEST_RETU")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceRequestRetuEntitty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "CREAT_HUMMER_ID")
    private Long creatHummerId;

    @Column(name = "CREAT_DATE")
    private Date creatDate;

    @Column(name = "REASON")
    private String reason;

    @Column(name = "PART_ID")
    private Long partId;
    @Column(name = "HANDLER_HUMMER_ID")
    private Long handlerHummerId;

    @Column(name ="APPROVED_DATE")
    private Date approvedDate;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Integer status;

    @Version
    private Long version;
}
