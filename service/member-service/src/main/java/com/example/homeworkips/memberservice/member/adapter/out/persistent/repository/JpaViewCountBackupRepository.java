package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;

import com.example.homeworkips.memberservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaViewCountBackupRepository extends JpaRepository<Member, Long> {

    @Query(
            value = """
                    update member m set m.view_count = :viewCount
                    where m.id = :memberId
                    """, nativeQuery = true
    )
    @Modifying
    void updateViewCount(@Param("memberId") Long memberId, @Param("viewCount") Long viewCount);
}
