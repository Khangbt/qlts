package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigInteger;

@Data
@Entity
//@Table(name = "SUPPLIER")
@Table(name = "supplier")

@AllArgsConstructor
@NoArgsConstructor
public class SupplierEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SUPPLIER_ID")
    private Long supplierId;

//    @Column(name = "GROUP_SUPPLIER_ID")
//    private BigInteger groupSupplierid;

    @Column(name = "CODE")
    private String code;

    @Column(name = "NAME")
    private String name;

    @Column(name = "TYPE")
    private String type;

//    @Column(name = "HUMAN_ID")
//    private BigInteger human_id;

//    @Column(name = "POSITION_ID")
//    private BigInteger position;

    @Column(name = "PHONENUMBER")
    private String phoneNumber;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "FAX")
    private String fax;

    @Column(name = "WEBSITE")
    private String website;

//    @Column(name = "AREA")
//    private String area;

    @Column(name = "ADDRESS")
    private String address;

    @Column(name = "NOTE")
    private String note;

    @Column(name = "STATUS")
    private Integer status;
    @Column(name = "HUMAN_CONTACT")
    private String humanContact;

}
