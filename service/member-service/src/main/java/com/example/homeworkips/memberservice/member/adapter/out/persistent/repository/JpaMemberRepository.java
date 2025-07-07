package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;


import com.example.homeworkips.memberservice.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface JpaMemberRepository extends JpaRepository<Member,Long> {

}
