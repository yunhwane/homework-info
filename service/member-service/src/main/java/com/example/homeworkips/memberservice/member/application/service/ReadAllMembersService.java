package com.example.homeworkips.memberservice.member.application.service;

import com.example.homeworkips.memberservice.member.application.port.out.LoadMemberPort;
import com.example.homeworkips.memberservice.member.application.port.in.ReadAllMembersUseCase;
import com.example.homeworkips.memberservice.member.application.service.response.MemberProfileResponse;
import com.example.homeworkips.memberservice.member.domain.MemberPageResult;
import com.example.homeworkips.memberservice.member.domain.PageLimitCalculator;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class ReadAllMembersService implements ReadAllMembersUseCase {

    private final LoadMemberPort loadMemberPort;
    private static final Long MOVABLE_PAGE_SIZE = 10L;

    @Override
    public MemberPageResult readAllMembers(SortType sortType, Direction direction, Long page, Long pageSize) {
        return MemberPageResult.create(
                loadMemberPort.findAll(sortType, direction, (page - 1) * pageSize, pageSize)
                        .stream()
                        .map(MemberProfileResponse::create)
                        .toList(),
                loadMemberPort.count(PageLimitCalculator.calculatePageLimit(page, pageSize, MOVABLE_PAGE_SIZE)));
    }
}
