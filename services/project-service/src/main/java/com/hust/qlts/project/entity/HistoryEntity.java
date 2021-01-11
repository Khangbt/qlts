package com.hust.qlts.project.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
//@Table(name ="HISTORY")
@Table(name ="history")

@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class HistoryEntity extends Auditable{

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "HISTORY_ID")
    private Long historyId;

    @Column(name = "VALUE_ID")
    private Long valueId;

    @CreatedBy
    @Column(name = "USER_CREATE")
    private Long userCreate;

    @Column(name = "ACTION")
    private Integer action;

	@Column(name = "TYPE_SCREEN")
    private Integer typeScreen;

    @Column(name = "CONTENT")
    private String content;

    @CreatedDate
	@Column(name = "DATE")
    private Date date;

    @Column(name = "VALUE_OLD")
    private String valueOld;

    @Column(name = "VALUE_NEW")
    private String valueNew;

}
