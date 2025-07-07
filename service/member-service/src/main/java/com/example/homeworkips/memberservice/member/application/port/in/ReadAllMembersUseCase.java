package com.example.homeworkips.memberservice.member.application.port.in;

import com.example.homeworkips.memberservice.member.domain.MemberPageResult;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;

public interface ReadAllMembersUseCase {

    MemberPageResult readAllMembers(SortType sortType, Direction direction, Long offset, Long limit);
}
