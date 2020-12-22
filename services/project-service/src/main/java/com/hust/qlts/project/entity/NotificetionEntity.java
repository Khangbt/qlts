package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Table(name = "NOTIFICETION")
@AllArgsConstructor
@NoArgsConstructor
public class NotificetionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    @Column(name = "CONTEN")
    private String conten;
    @Column(name = "ID_HUMMER_SHOW")
    private Long idHummerShow;
    @Column(name = "LIST_SHOW")
    private String listShow;
    @Column(name = "NOTE1")
    private Long note1;
    @Column(name = "NOTE2")
    private Long note2;
    @Column(name = "TYLE")
    private String tyle;
    @Column(name = "PARTID")
    private Long partId;
    @Column(name = "STATUS")
    private boolean status;

}
