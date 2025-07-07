package com.example.homeworkips.memberservice.member.adapter.out.persistent.repository;

import com.example.homeworkips.memberservice.member.domain.Member;
import com.example.homeworkips.memberservice.member.domain.support.Direction;
import com.example.homeworkips.memberservice.member.domain.support.SortType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.example.homeworkips.memberservice.member.domain.QMember.member;

@Repository
@RequiredArgsConstructor
public class QueryDslMemberRepository {

    private final JPAQueryFactory jpaQueryFactory;

    public List<Member> findAll(SortType sortType, Direction direction, Long offset, Long limit) {
        List<Long> memberIds = jpaQueryFactory
                .select(member.id)
                .from(member)
                .orderBy(createOrderSpecifier(sortType, direction))
                .offset(offset)
                .limit(limit)
                .fetch();

        if (memberIds.isEmpty()) {
            return List.of();
        }

        return jpaQueryFactory
                .selectFrom(member)
                .where(member.id.in(memberIds))
                .orderBy(createOrderSpecifier(sortType, direction))
                .fetch();
    }

    private OrderSpecifier<?> createOrderSpecifier(SortType sortType, Direction direction) {
        Order order = direction == Direction.ASC ? Order.ASC : Order.DESC;

        return switch (sortType) {
            case NAME -> new OrderSpecifier<>(order, member.username);
            case VIEW_COUNT -> new OrderSpecifier<>(order, member.viewCount);
            case REGISTERED_AT -> new OrderSpecifier<>(order, member.registeredAt);
        };
    }

    public Long count(Long limit) {
        List<Long> sub = jpaQueryFactory
                .select(member.id)
                .from(member)
                .orderBy(member.id.desc())
                .limit(limit)
                .fetch();

        return (long) sub.size();
    }
}
