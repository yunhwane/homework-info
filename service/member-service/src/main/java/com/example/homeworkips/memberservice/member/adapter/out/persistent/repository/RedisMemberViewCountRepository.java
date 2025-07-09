package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;


import com.example.homeworkips.memberservice.member.domain.support.Direction;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class RedisMemberViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String ZSET_KEY = "member:view:count";


    public void initializeViewCount(Long memberId) {
        Double currentScore = redisTemplate.opsForZSet().score(ZSET_KEY, memberId.toString());

        if (currentScore == null) {
            redisTemplate.opsForZSet().add(ZSET_KEY, memberId.toString(), 0.0);
        }
    }

    public Long read(Long memberId) {
        Double score = redisTemplate.opsForZSet().score(ZSET_KEY, memberId.toString());
        System.out.println("Redis read - Key: " + ZSET_KEY + ", MemberId: " + memberId + ", Score: " + score);
        return score == null ? 0L : score.longValue();
    }

    public Long increase(Long memberId) {
        Double newScore = redisTemplate.opsForZSet().incrementScore(ZSET_KEY, memberId.toString(), 1.0);
        return newScore == null ? 0L : newScore.longValue();
    }

    public List<Long> findTopByViewCount(Direction direction, Long offset, Long limit) {
        Set<String> memberIds;

        if (direction == Direction.DESC) {
            memberIds = redisTemplate.opsForZSet()
                    .reverseRange(ZSET_KEY, offset, offset + limit - 1);
        } else {
            memberIds = redisTemplate.opsForZSet()
                    .range(ZSET_KEY, offset, offset + limit - 1);
        }

        if (memberIds == null) return List.of();

        return memberIds.stream()
                .map(Long::parseLong)
                .toList();
    }

}