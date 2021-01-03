package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.entity.RequestEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RequestRepository extends JpaRepository<RequestEntity, Long> {
}
