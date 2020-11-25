package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "REQUEST")
@AllArgsConstructor
@NoArgsConstructor
public class RequestEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "TYLE")
    private String tyle;

    @Column(name = "ID_REQUEST")
    private Long idRequest;

    @Column(name = "PART_ID")
    private Long partId;

    @Column(name = "CREART_HUMMER_ID")
    private Long creatHummerId;

    @Column(name = "CODE")
    private String code;
    @Column(name = "CREART_DATE")
    private Date creatDate;
}
