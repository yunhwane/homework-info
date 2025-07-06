package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import lombok.RequiredArgsConstructor;
import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Repository
public class R2DBCPaymentValidationRepository implements PaymentValidationRepository{

    private final DatabaseClient databaseClient;

    @Override
    public Mono<Boolean> isValid(String orderId, Long amount) {
        String SELECT_PAYMENT_AMOUNT_QUERY = """
                SELECT amount FROM payment_events WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(SELECT_PAYMENT_AMOUNT_QUERY)
                .bind("orderId", orderId)
                .fetch()
                .one()
                .handle((row, sink) -> {
                    Long paymentAmount = ((Number) row.get("amount")).longValue();

                    if (paymentAmount.equals(amount)){
                        sink.next(true);
                    } else {
                        sink.error(new IllegalArgumentException("Payment amount does not match the expected amount."));
                    }
                });
    }
}
