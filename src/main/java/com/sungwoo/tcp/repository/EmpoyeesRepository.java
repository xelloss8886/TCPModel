package com.sungwoo.tcp.repository;

import com.sungwoo.tcp.entity.SalaryEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpoyeesRepository extends JpaRepository<SalaryEntity, Integer> {
    Page<SalaryEntity> findAll(Pageable pageable);
}
