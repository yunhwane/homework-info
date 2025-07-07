package com.example.homeworkips.memberservice.member.adapter.in.api;


import com.example.homeworkips.memberservice.member.application.port.in.MemberViewCountUpdateUseCase;
import com.example.homeworkips.memberservice.member.application.port.in.ReadAllMembersUseCase;
import com.example.homeworkips.memberservice.common.ApiResponse;
import com.example.homeworkips.memberservice.member.domain.MemberPageResult;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RequestMapping("/api/v1/members")
@RestController
@RequiredArgsConstructor
public class MemberController {

    private final ReadAllMembersUseCase readAllMembersUseCase;
    private final MemberViewCountUpdateUseCase memberViewCountUpdateUseCase;


    @GetMapping
    public ResponseEntity<ApiResponse<MemberPageResult>> getMembers(
            @RequestParam(defaultValue = "REGISTERED_AT") SortType sortType,
            @RequestParam(defaultValue = "ASC") Direction direction,
            @RequestParam(defaultValue = "1") Long page,
            @RequestParam(defaultValue = "10") Long pageSize) {

        return ResponseEntity.ok(
                ApiResponse.with(
                        HttpStatus.OK,
                        "Members retrieved successfully",
                        readAllMembersUseCase.readAllMembers(
                                sortType,
                                direction,
                                page,
                                pageSize
                        )
                )
        );
    }

    @PostMapping("/{memberId}/view-count")
    public ResponseEntity<ApiResponse<Long>> incrementViewCount(@PathVariable("memberId") Long memberId) {
        Long viewCount = memberViewCountUpdateUseCase.increaseViewCount(memberId);
        return ResponseEntity.ok(ApiResponse.with(
                HttpStatus.OK,
                "View count updated successfully",
                viewCount
        ));
    }
}
