package com.example.homeworkips.memberservice.member.adapter.out.persistent;

import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.JpaMemberRepository;
import com.example.homeworkips.memberservice.member.adapter.out.persistent.repository.RedisMemberViewCountRepository;
import com.example.homeworkips.memberservice.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class RedisViewCountInitializer implements CommandLineRunner {

    private final JpaMemberRepository jpaMemberRepository;
    private final RedisMemberViewCountRepository redisMemberViewCountRepository;

    @Override
    public void run(String... args) throws Exception {
        List<Member> allMembers = jpaMemberRepository.findAll();
        List<Long> memberIds = allMembers.stream().map(Member::getId).toList();
        redisMemberViewCountRepository.initializeBatch(memberIds);
        log.info("Redis view count initialization completed for {} members", allMembers.size());
    }
}
