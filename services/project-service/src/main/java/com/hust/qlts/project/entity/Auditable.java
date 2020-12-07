
package com.hust.qlts.project.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;



@Data
@MappedSuperclass
@AllArgsConstructor
@NoArgsConstructor
public class Auditable {
//    @CreatedDate
    @Column(name = "CREATEA_DATE")
    private Date createdDate;

//    @LastModifiedDate
    @Column(name = "LAST_MODIFIED_DATE")
    private Date lastModifiedDate;
}