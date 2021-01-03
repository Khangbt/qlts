package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DEVICE_GROUP")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceGroupEntity extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYLE")
    private String tyle;

    @Column(name = "SIZE_ID")
    private Integer sizeId;

    @Column(name = "SPECIFICATIONS")//thông số ki thuât
    private String specifications;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "SIZE_UNIT")
    private Integer sizeUnit;

    @Column(name = "NOTE")
    private String note;
    @Version
    private Long version;

}
