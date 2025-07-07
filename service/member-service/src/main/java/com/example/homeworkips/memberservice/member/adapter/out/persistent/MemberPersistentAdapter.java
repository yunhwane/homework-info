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
        return queryDslMemberRepository.findAll(sortType, direction, offset, limit)
                .stream().peek(
                        it -> it.addViewCount(redisMemberViewCountRepository.read(it.getId()))
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
