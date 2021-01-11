package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "DEVICE_TO_REQUEST_ADD")
@Table(name = "device_to_request_add")

@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=false)

public class DeviceToRequestAddEntity  extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEVICE_GROUP_ID")
    private Long deviceGroup;

    @Column(name = "DEVICE_REQUEST_ADD_ID")
    private Long deviceRequestAddId;

    @Column(name = "SIZE")
    private Long size;

    @Column(name = "PRICE")
    private Long price;

    @Column(name = "SUPPLIER_ID")
    private Long supplierId;

    @Column(name = "WAREHOUSE_ID")
    private Long warehouseId;

    @Column(name = "STATUS")
    private Integer status;
    @Version
    private Long version;

}
