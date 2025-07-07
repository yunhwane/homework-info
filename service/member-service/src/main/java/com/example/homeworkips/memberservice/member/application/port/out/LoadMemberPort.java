package com.example.homeworkips.memberservice.member.application.port.out;

import com.example.homeworkips.memberservice.member.domain.Member;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;

import java.util.List;

public interface LoadMemberPort {

    List<Member> findAll(SortType sortType, Direction direction, Long offset, Long limit);
    Long count(Long limit);
}
