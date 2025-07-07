package com.example.homeworkips.memberservice.member.application.port.out;

import com.example.homeworkips.memberservice.member.domain.Member;

public interface SaveMemberPort {
    Member save(Member member);
    void backUpViewCount(Long memberId, Long viewCount);
}
