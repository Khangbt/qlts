package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
//@Table(name = "DEVICE_TO_REQUEST")
@Table(name = "device_to_request")

@AllArgsConstructor
@NoArgsConstructor
public class DeviceToRequestEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "DEVICE_REQUEST_ID")
    private Long deviceRequestId;

    @Column(name = "DEVICE_ID")
    private Long deviceId;

    @Column(name = "STATUS")
    private Long status;

    @Column(name = "DEVICE_GROUP_ID")
    private Long deviceGroupId;

    @Version
    private Long version;
}
