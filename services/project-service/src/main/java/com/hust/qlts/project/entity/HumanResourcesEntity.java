package com.hust.qlts.project.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "HUMAN_RESOURCES")
@NoArgsConstructor
public class HumanResourcesEntity extends Auditable{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "HUMAN_RESOURCES_ID")
    private Long humanResourceId;

    @Column(name = "CODE")
    private String code;

    @Column(name = "FULLNAME")
    private String fullName;

    @Column(name = "EMAIL")
    private String email;

    @Column(name = "POSITION_ID")
    private Long positionId;

    @Column(name = "STATUS")
    private Integer status;

    @Column(name = "NOTE")
    private String note;


    @Column(name = "PART_ID")
    private Long partId;


    @Column(name = "DATE_GRADUATE")
    private Integer dateGraduate;


    @Column(name = "CREATE_DATE")
    private Date createDate;

    @Column(name = "CREATE_BY")
    private Long createBy;

    @Column(name = "UPDATE_DATE")
    private Date updateDate;

    @Column(name = "UPDATE_BY")
    private Long updateBy;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @Column(name = "IS_NEW")
    private Integer isNew;

    @Column(name = "VERIFY_KEY")
    private String verifyKey;

    @Column(name = "ROLE")
    private String role;
    @Transient
    public List<GrantedAuthority> authorities;

}
