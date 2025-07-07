package com.example.homeworkips.memberservice.member.application.service;

import com.example.homeworkips.memberservice.member.application.port.out.SaveMemberPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class MemberViewCountBackUpProcessor {

    private final SaveMemberPort saveMemberPort;

    @Transactional
    public void backUp(Long memberId, Long viewCount) {
        saveMemberPort.backUpViewCount(memberId, viewCount);
    }
}
