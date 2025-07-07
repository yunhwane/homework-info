package com.example.homeworkips.memberservice.member.application.service;


import com.example.homeworkips.memberservice.member.application.port.out.UpdateMemberViewCountPort;
import com.example.homeworkips.memberservice.member.application.port.in.MemberViewCountUpdateUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class MemberViewCountUpdateService implements MemberViewCountUpdateUseCase {

    private final UpdateMemberViewCountPort updateMemberViewCountPort;
    private final MemberViewCountBackUpProcessor memberViewCountBackUpProcessor;
    private static final int BACKUP_THRESHOLD = 300;

    @Override
    public Long increaseViewCount(Long memberId) {
        Long viewCount = updateMemberViewCountPort.increaseViewCount(memberId);
        log.info("Member ID: {}, View Count: {}", memberId, viewCount);

        if (viewCount % BACKUP_THRESHOLD == 0) {
            memberViewCountBackUpProcessor.backUp(memberId, viewCount);
            log.info("Backed up view count for Member ID: {}", memberId);
        }

        return viewCount;
    }
}
