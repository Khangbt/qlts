package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "DEVICE_TO_REQUEST_ADD")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceToRequestAddEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEVICE_GROUP_ID")
    private Long deviceRequestId;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "SUPPLIER_ID")
    private Long supplierId;


    @Column(name = "STATUS")
    private Integer status;

}
