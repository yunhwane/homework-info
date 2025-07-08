package com.example.homeworkips.paymentservice.point.adapter.out.persistent.repository;


import com.example.homeworkips.paymentservice.point.application.port.in.PointRewardCommand;
import com.example.homeworkips.paymentservice.point.domain.PointResult;
import com.example.homeworkips.paymentservice.point.domain.PointType;
import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class R2DBCPointRepository implements PointRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<PointResult> save(PointRewardCommand command) {
        String INSERT_POINT_QUERY = """
                INSERT INTO point_events (order_id, user_id, points, point_type)
                VALUES (:orderId, :userId, :points, :pointType)
                """.trim();

        return databaseClient.sql(INSERT_POINT_QUERY)
                .bind("orderId", command.getOrderId())
                .bind("userId", command.getBuyerId())
                .bind("points", command.getPaymentAmount())
                .bind("pointType", PointType.REWARD.name())
                .fetch()
                .rowsUpdated()
                .flatMap(rowsUpdated -> {
                    if (rowsUpdated > 0) {
                        return Mono.just(
                                PointResult.create(
                                        PointType.REWARD,
                                        command.getPaymentAmount(),
                                        command.getOrderId(),
                                        command.getBuyerId()
                                )
                        );
                    } else {
                        return Mono.error(new RuntimeException("Failed to save point event"));
                    }
                })
                .as(transactionalOperator::transactional);
    }
}
