package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import com.example.homeworkips.paymentservice.payment.domain.PaymentEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
@Slf4j
public class R2DBCPaymentRepository implements PaymentRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Void> save(PaymentEvent paymentEvent) {
        return insertPaymentEvent(paymentEvent)
                .as(transactionalOperator::transactional)
                .then();
    }

    private Mono<Long> insertPaymentEvent(PaymentEvent paymentEvent) {
        String INSERT_PAYMENT_EVENT_QUERY = """
                INSERT INTO payment_events (buyer_id, amount, order_name, order_id)
                VALUES (:buyerId, :amount, :orderName, :orderId)
                """.trim();

        log.info("Inserting payment event: {}", paymentEvent.toString());
        return databaseClient.sql(INSERT_PAYMENT_EVENT_QUERY)
                .bind("buyerId", paymentEvent.getBuyerId())
                .bind("amount", paymentEvent.getAmount())
                .bind("orderName", paymentEvent.getOrderName())
                .bind("orderId", paymentEvent.getOrderId())
                .fetch()
                .rowsUpdated();
    }


}
