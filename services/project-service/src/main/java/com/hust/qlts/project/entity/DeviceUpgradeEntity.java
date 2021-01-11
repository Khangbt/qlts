package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

//thieetss bi nâng cap
@Data
@Entity
//@Table(name = "DEVICE_UPGRADE")
@Table(name = "device_upgrade")

@AllArgsConstructor
@NoArgsConstructor
public class DeviceUpgradeEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;

    @Column(name = "ID_MAIN_DEVICE")
    private Long idMainDevice;

    @Column(name = "ID_AUXILIARY_DEVICE")
    private Long idAuxiliaryDevice;

    @Column(name = "NOTE")
    private String note;
    @Version
    private Long version;
}
