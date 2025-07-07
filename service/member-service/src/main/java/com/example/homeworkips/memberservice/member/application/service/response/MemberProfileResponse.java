package com.example.homeworkips.memberservice.member.application.service.response;

import com.example.homeworkips.memberservice.member.domain.Member;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;

@Getter
@ToString
public class MemberProfileResponse {
    private Long id;
    private String name;
    private Long viewCount;
    private LocalDateTime registeredAt;

    public static MemberProfileResponse create(Member member) {
        MemberProfileResponse response = new MemberProfileResponse();
        response.id = member.getId();
        response.name = member.getUsername();
        response.viewCount = member.getViewCount();
        response.registeredAt = member.getRegisteredAt();
        return response;
    }
}
