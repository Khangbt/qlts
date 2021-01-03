package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import javax.persistence.*;
//kho chua
@Data
@Entity
@Table(name = "WAREHOUSE")
@AllArgsConstructor
@NoArgsConstructor
//@EqualsAndHashCode(callSuper=false)
public class WarehouseEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "WAREHOUSE_ID")
    private Long warehouseID;

    @Column(name="CODE")
    private String code;

    @Column(name="NAME")
    private String name;


    @Column(name="PAR_ID")
    private Long parid;

    @Column(name="ADDRESS")
    private String address;

    @Column(name="NOTE")
    private String note;

    @Column(name="STATUS")
    private Integer status;

}
