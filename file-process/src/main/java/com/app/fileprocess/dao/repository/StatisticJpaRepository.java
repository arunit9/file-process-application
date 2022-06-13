package com.app.fileprocess.dao.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.app.fileprocess.dao.entity.Statistic;

@Repository
public interface StatisticJpaRepository extends JpaRepository<Statistic, Long> {

}
