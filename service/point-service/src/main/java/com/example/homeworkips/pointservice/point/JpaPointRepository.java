package com.example.homeworkips.pointservice.point;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface JpaPointRepository extends JpaRepository<Point, Long> {

    Optional<Point> findByMemberId(Long memberId);
}
