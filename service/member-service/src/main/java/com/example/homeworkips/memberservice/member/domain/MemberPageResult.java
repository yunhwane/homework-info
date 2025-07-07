package com.example.homeworkips.memberservice.member.domain;


import com.example.homeworkips.memberservice.member.application.service.response.MemberProfileResponse;
import lombok.Getter;
import lombok.ToString;

import java.util.List;

@Getter
@ToString
public class MemberPageResult {

    private List<MemberProfileResponse> members;
    private Long memberCount;

    public static MemberPageResult create(List<MemberProfileResponse> members, Long memberCount) {
        MemberPageResult result = new MemberPageResult();
        result.members = members;
        result.memberCount = memberCount;
        return result;
    }
}
