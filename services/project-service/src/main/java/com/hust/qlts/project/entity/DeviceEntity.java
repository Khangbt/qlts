package com.hust.qlts.project.entity;
//thieetss bị

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "DEVICE")
@AllArgsConstructor
@NoArgsConstructor
public class DeviceEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "DEVICE_ID")
    private Long id;

    @Column(name = "CODE")
    private String code;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "LOCATION")
    private String location;

    @Column(name = "DATE_ADD")
    private Date dateAdd;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "PART_ID")
    private Long partId;

    @Column(name = "EQUIPMENT_GROUP_ID")//nhom thiết bi
    private Long idEquipmentGroup;

    @Column(name = "DEVICE_TO_REQUEST_ADD_ID")// thiêt bi thêm vào theo phiếu yêu cầu
    private Long requestAddId;

    @Column(name = "SUPPLIER_ID")//nhà cung cấp
    private Long supplierId;

    @Column(name = "WAREHOUSE_ID")
    private Long warehouseId;

    @Column(name = "UNIT")
    private Integer unit;

    @Column(name = "SIZE_UNIT")
    private Integer sizeUnit;

    @Column(name = "LOST_DEVICE")
    private Integer lostDevice;

    @Column(name = "EXIST")
    private boolean exist;

    @Column(name = "USE_HUMMER_ID")
    private Long useHummerId;

    @Column(name = "SPECIFICATIONS")//thông số ki thuât
    private String specifications;

    @Version
    private Long version;

}
