package com.example.homeworkips.memberservice.member.domain;


import com.example.homeworkips.memberservice.member.domain.support.AbstractBaseEntity;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Member extends AbstractBaseEntity {

    private String username;
    private Long viewCount;
    private LocalDateTime registeredAt;

    public static Member create(String username) {
        Member member = new Member();
        member.username = username;
        member.viewCount = 0L;
        member.registeredAt = LocalDateTime.now();
        return member;
    }

    public void addViewCount(Long viewCount) {
        this.viewCount = viewCount;
    }
}
