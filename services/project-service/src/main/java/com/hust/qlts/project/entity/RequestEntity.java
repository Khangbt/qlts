package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;

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

}
