package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;


import com.example.homeworkips.memberservice.member.domain.support.Direction;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.RedisScript;
import org.springframework.stereotype.Component;

import java.util.*;

@Component
@RequiredArgsConstructor
@Slf4j
public class RedisMemberViewCountRepository {

    private final StringRedisTemplate redisTemplate;

    private static final String ZSET_KEY = "member:view:count";

    private static final String BATCH_READ_SCRIPT = """
        local key = KEYS[1]
        local memberIds = ARGV
        local result = {}
        for i = 1, #memberIds do
            local score = redis.call('zscore', key, memberIds[i])
            if score then
                result[i] = score
            else
                result[i] = '0'
            end
        end
        return result
        """.trim();

    private static final String BATCH_INIT_SCRIPT = """
        local key = KEYS[1]
        local memberIds = ARGV
        local count = 0
        for i = 1, #memberIds do
            local exists = redis.call('zscore', key, memberIds[i])
            if not exists then
                redis.call('zadd', key, 0, memberIds[i])
                count = count + 1
            end
        end
        return count
        """.trim();

    private static final String INCREASE_SCRIPT = """
        local key = KEYS[1]
        local memberId = ARGV[1]
        local exists = redis.call('zscore', key, memberId)
        if not exists then
            redis.call('zadd', key, 1, memberId)
            return 1
        else
            return redis.call('zincrby', key, 1, memberId)
        end
        """.trim();

    private static final String GET_TOP_SCRIPT = """
        local key = KEYS[1]
        local direction = ARGV[1]
        local offset = tonumber(ARGV[2])
        local limit = tonumber(ARGV[3])
        local result
        if direction == 'DESC' then
            result = redis.call('zrevrange', key, offset, offset + limit - 1)
        else
            result = redis.call('zrange', key, offset, offset + limit - 1)
        end
        return result
        """.trim();

    private static final String READ_SINGLE_SCRIPT = """
        local key = KEYS[1]
        local memberId = ARGV[1]
        local score = redis.call('zscore', key, memberId)
        if score then
            return score
        else
            return '0'
        end
        """.trim();

    public void initializeBatch(List<Long> memberIds) {
        if (memberIds.isEmpty()) return;
        
        try {
            String[] memberIdStrings = memberIds.stream()
                    .map(String::valueOf)
                    .toArray(String[]::new);
            
            RedisScript<Long> script = RedisScript.of(BATCH_INIT_SCRIPT, Long.class);

            redisTemplate.execute(script, List.of(ZSET_KEY), memberIdStrings);

        } catch (Exception e) {
           log.error("initialize batch Lua script execution failed: {}", e.getMessage());
        }
    }

    public Map<Long, Long> readBatch(List<Long> memberIds) {
        if (memberIds.isEmpty()) return Map.of();

        try {
            String[] memberIdStrings = memberIds.stream()
                    .map(String::valueOf)
                    .toArray(String[]::new);

            RedisScript<List> script = RedisScript.of(BATCH_READ_SCRIPT, List.class);

            List<Object> results = redisTemplate.execute(script, List.of(ZSET_KEY), memberIdStrings);

            Map<Long, Long> result = new HashMap<>();
            
            if (results != null && results.size() == memberIds.size()) {
                for (int i = 0; i < memberIds.size(); i++) {
                    Object scoreObj = results.get(i);
                    Long score = parseScore(scoreObj);
                    result.put(memberIds.get(i), score);
                }
            }
            
            return result;
            
        } catch (Exception e) {
            log.error("read batch Lua script execution failed: " + e.getMessage());
            return Map.of();
        }
    }

    public Long increase(Long memberId) {
        try {
            RedisScript<String> script = RedisScript.of(INCREASE_SCRIPT, String.class);
            String result = redisTemplate.execute(script, List.of(ZSET_KEY), memberId.toString());
            return parseScore(result);
        } catch (Exception e) {
            log.error("Increase view count failed for memberId {}: {}", memberId, e.getMessage());
            return 0L;
        }
    }

    public List<Long> findTopByViewCount(Direction direction, Long offset, Long limit) {
        try {
            RedisScript<List> script = RedisScript.of(GET_TOP_SCRIPT, List.class);
            List<Object> results = redisTemplate.execute(
                script, 
                List.of(ZSET_KEY), 
                direction.name(), 
                offset.toString(), 
                limit.toString()
            );
            
            if (results == null || results.isEmpty()) {
                return List.of();
            }

            return results.stream()
                    .map(obj -> Long.parseLong(obj.toString()))
                    .toList();
                    
        } catch (Exception e) {
            log.error("find top view count failed for memberId {}: {}", direction.name(), e.getMessage());
            return List.of();
        }
    }

    private Long parseScore(Object scoreObj) {
        switch (scoreObj) {
            case null -> {
                return 0L;
            }
            case Number number -> {
                return number.longValue();
            }
            case String s -> {
                try {
                    return Long.parseLong(s);
                } catch (NumberFormatException e) {
                    return 0L;
                }
            }
            default -> {
            }
        }

        return 0L;
    }
}
