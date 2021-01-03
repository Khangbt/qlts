package com.hust.qlts.project.repository.jparepository;

import com.hust.qlts.project.dto.IHistoryDTO;
import com.hust.qlts.project.entity.HistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HistoryRepository extends JpaRepository<HistoryEntity, Long> {


    
}
