package com.example.homeworkips.pointservice.point;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JpaPointHistoryRepository extends JpaRepository<PointHistory, Long> {
}
