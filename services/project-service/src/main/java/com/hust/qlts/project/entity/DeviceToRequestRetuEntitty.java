package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "DEVICE_TO_REQUEST_RETU")
@Table(name = "device_to_request_retu")

@AllArgsConstructor
@NoArgsConstructor
public class DeviceToRequestRetuEntitty extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEVICE_ID")
    private Long deviceId;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "LOST_DEVICE")
    private Integer lostDevice;
    @Column(name = "DEVICE_REQUEST_ID_RETU")
    private Long deviceRequestIdRetu;

    @Column(name = "WAREHOUSE_ID")
    private Long warehouseId;
    @Version
    private Long version;
}
