package com.example.homeworkips.memberservice.member.adapter.out.persistent;


import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.JpaMemberRepository;
import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.JpaViewCountBackupRepository;
import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.QueryDslMemberRepository;
import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.RedisMemberViewCountRepository;
import com.example.homeworkips.memberservice.member.application.port.out.LoadMemberPort;
import com.example.homeworkips.memberservice.member.application.port.out.SaveMemberPort;
import com.example.homeworkips.memberservice.member.application.port.out.UpdateMemberViewCountPort;
import com.example.homeworkips.memberservice.member.domain.Member;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MemberPersistentAdapter implements
        SaveMemberPort,
        LoadMemberPort,
        UpdateMemberViewCountPort {

    private final JpaMemberRepository jpaMemberRepository;
    private final RedisMemberViewCountRepository redisMemberViewCountRepository;
    private final JpaViewCountBackupRepository jpaViewCountBackupRepository;
    private final QueryDslMemberRepository queryDslMemberRepository;

    @Override
    public Member save(Member member) {
        return jpaMemberRepository.save(member);
    }

    @Override
    public void backUpViewCount(Long memberId, Long viewCount) {
        jpaViewCountBackupRepository.updateViewCount(memberId, viewCount);
    }

    @Override
    public List<Member> findAll(SortType sortType, Direction direction, Long offset, Long limit) {

        if (sortType == SortType.VIEW_COUNT) {

            List<Long> memberIds = redisMemberViewCountRepository.findTopByViewCount(direction, offset, limit);

            if (!memberIds.isEmpty()) {
                Map<Long, Member> memberMap = queryDslMemberRepository.findAllByIds(memberIds)
                        .stream()
                        .collect(Collectors.toMap(Member::getId, Function.identity()));

                return memberIds.stream()
                        .map(memberMap::get)
                        .filter(Objects::nonNull)
                        .peek(member -> member.addViewCount(redisMemberViewCountRepository.read(member.getId())))
                        .toList();
            }
        }

        return queryDslMemberRepository.findAll(sortType, direction, offset, limit)
                .stream().peek(it ->
                        it.addViewCount(redisMemberViewCountRepository.read(it.getId()))
                ).toList();
    }

    @Override
    public Long count(Long limit) {
        return queryDslMemberRepository.count(limit);
    }

    @Override
    public Long increaseViewCount(Long memberId) {
        return redisMemberViewCountRepository.increase(memberId);
    }

}
