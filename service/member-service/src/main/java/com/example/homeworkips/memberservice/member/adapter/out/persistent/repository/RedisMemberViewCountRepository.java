package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisMemberViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String KEY_FORMAT = "view::member::%s::view_count";


    public Long read(Long memberId) {
        String result = redisTemplate.opsForValue().get(generateKey(memberId));
        return result == null ? 0L : Long.parseLong(result);
    }

    public Long increase(Long memberId) {
        return redisTemplate.opsForValue().increment(generateKey(memberId));
    }

    private String generateKey(Long memberId) {
        return String.format(KEY_FORMAT, memberId);
    }
}
