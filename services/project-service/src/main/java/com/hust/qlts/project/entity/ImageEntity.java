package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table(name = "IMAGE")
@AllArgsConstructor
@NoArgsConstructor
public class ImageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "PATH")
    private String name;
    @Column(name = "ID_CODE")
    private Long idCode;
    @Column(name = "TYLE")
    private Integer tyle;

    @Column(name = "DATEADD")
    private Date dateAdd;
}
