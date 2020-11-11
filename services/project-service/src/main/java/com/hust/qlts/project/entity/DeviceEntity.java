package com.hust.qlts.project.entity;
//thieetss bị

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "DEVICE")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVICE_ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "SPECIFICATIONS")
    private String specifications;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "DATE_ADD")
    private Date dateAdd;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "PART_ID")
    private String partId;

    @Column(name = "EQUIPMENT_GROUP_ID")
    private Long equipmentGroupId;

}
