package com.example.homeworkips.paymentservice.payment.adapter.out.persistent;


import com.example.homeworkips.paymentservice.payment.adapter.out.persistent.dto.PaymentStatusQueryDto;
import com.example.homeworkips.paymentservice.payment.application.port.out.PaymentStatusUpdateCommand;
import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;
import lombok.RequiredArgsConstructor;

import org.springframework.r2dbc.core.DatabaseClient;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.reactive.TransactionalOperator;
import reactor.core.publisher.Mono;

@Repository
@RequiredArgsConstructor
public class R2DBCPaymentStatusUpdateRepository implements PaymentStatusUpdateRepository {

    private final DatabaseClient databaseClient;
    private final TransactionalOperator transactionalOperator;

    @Override
    public Mono<Boolean> updatePaymentStatusToExecuting(String orderId, String paymentKey) {
        return checkPreviousPaymentStatus(orderId)
                .flatMap( it -> insertPaymentHistory(it, PaymentStatus.EXECUTING, "PAYMENT_CONFIRMATION_STARTED"))
                .flatMap(it -> updatePaymentEventStatus(paymentKey, PaymentStatus.EXECUTING, orderId))
                .as(transactionalOperator::transactional)
                .thenReturn(true);
    }

    @Override
    public Mono<Boolean> updatePaymentStatus(PaymentStatusUpdateCommand command) {
        return switch (command.status()) {
            case SUCCESS -> updatePaymentStatusToSuccess(command);
            case FAILURE -> updatePaymentStatusToFailure(command);
            case UNKNOWN -> updatePaymentStatusToUnknown(command);
            default -> Mono.error(new IllegalArgumentException(
                    "Unsupported payment status: " + command.status()
            ));
        };
    }

    private Mono<Boolean> updatePaymentStatusToFailure(PaymentStatusUpdateCommand command) {
        return selectPaymentStatusQuery(command.orderId())
                .flatMap(it -> insertPaymentHistory(it, PaymentStatus.FAILURE, "PAYMENT_CONFIRMATION_FAILED"))
                .flatMap(it -> updatePaymentEventStatus(command.status(), command.orderId()))
                .as(transactionalOperator::transactional)
                .thenReturn(true);
    }


    private Mono<Boolean> updatePaymentStatusToUnknown(PaymentStatusUpdateCommand command) {
        return selectPaymentStatusQuery(command.orderId())
                .flatMap(it -> insertPaymentHistory(it, PaymentStatus.UNKNOWN, "PAYMENT_CONFIRMATION_UNKNOWN"))
                .flatMap(it -> updatePaymentEventStatus(command.status(), command.orderId()))
                .flatMap(it -> incrementPaymentFailedCount(command.orderId()))
                .as(transactionalOperator::transactional)
                .thenReturn(true);
    }

    private Mono<Long> incrementPaymentFailedCount(String orderId) {
        String INCREMENT_PAYMENT_FAILED_COUNT_QUERY = """
                UPDATE payment_events
                SET failed_count = failed_count + 1
                WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(INCREMENT_PAYMENT_FAILED_COUNT_QUERY)
                .bind("orderId", orderId)
                .fetch()
                .rowsUpdated();
    }

    private Mono<Boolean> updatePaymentStatusToSuccess(PaymentStatusUpdateCommand command) {
        return selectPaymentStatusQuery(command.orderId())
                .flatMap(it -> insertPaymentHistory(it, PaymentStatus.SUCCESS, "PAYMENT_CONFIRMATION_IS_DONE"))
                .flatMap(it -> updatePaymentEventStatus(command.status(), command.orderId()))
                .flatMap(it -> updatePaymentExtraDetails(command))
                .as(transactionalOperator::transactional)
                .thenReturn(true);
    }

    private Mono<Long> updatePaymentExtraDetails(PaymentStatusUpdateCommand command) {
        String UPDATE_PAYMENT_EXTRA_DETAILS_QUERY = """
                UPDATE payment_events
                SET payment_type = :type, payment_method = :method, approved_at = :approvedAt, psp_raw_data = :pspRawData
                WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(UPDATE_PAYMENT_EXTRA_DETAILS_QUERY)
                .bind("type", command.extraDetails().getType().name())
                .bind("method", command.extraDetails().getMethod().name())
                .bind("approvedAt", command.extraDetails().getApprovedAt().toString())
                .bind("pspRawData", command.extraDetails().getPspRawData())
                .bind("orderId", command.orderId())
                .fetch()
                .rowsUpdated();
    }

    private Mono<Long> updatePaymentEventStatus(PaymentStatus paymentStatus, String orderId) {
        String UPDATE_PAYMENT_EVENT_STATUS_QUERY = """
                UPDATE payment_events
                SET payment_status = :paymentStatus
                WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(UPDATE_PAYMENT_EVENT_STATUS_QUERY)
                .bind("paymentStatus", paymentStatus.name())
                .bind("orderId", orderId)
                .fetch()
                .rowsUpdated();
    }

    private Mono<Long> updatePaymentEventStatus(String paymentKey, PaymentStatus paymentStatus, String orderId) {
        String UPDATE_PAYMENT_EVENT_STATUS_QUERY = """
                UPDATE payment_events
                SET payment_status = :paymentStatus, payment_key = :paymentKey
                WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(UPDATE_PAYMENT_EVENT_STATUS_QUERY)
                .bind("paymentKey", paymentKey)
                .bind("paymentStatus", paymentStatus.name())
                .bind("orderId", orderId)
                .fetch()
                .rowsUpdated();
    }

    private Mono<Long> insertPaymentHistory(PaymentStatusQueryDto dto, PaymentStatus newStatus, String reason) {

        if (dto.paymentStatus() == null) {
            return Mono.empty();
        }

        String INSERT_PAYMENT_HISTORY_QUERY = """
                INSERT INTO payment_order_histories (
                payment_order_id, previous_payment_status, new_status, reason)
                VALUES (:paymentOrderId, :previousPaymentStatus, :newStatus, :reason)
                """.trim();

        return databaseClient.sql(INSERT_PAYMENT_HISTORY_QUERY)
                .bind("paymentOrderId", dto.orderId())
                .bind("previousPaymentStatus", dto.paymentStatus().name())
                .bind("newStatus", newStatus.name())
                .bind("reason", reason)
                .fetch()
                .rowsUpdated();
    }

    private Mono<PaymentStatusQueryDto> checkPreviousPaymentStatus(String orderId) {
        return selectPaymentStatusQuery(orderId)
                .handle((it, sink) -> {
                    switch (it.paymentStatus()) {
                        case EXECUTING, NOT_STARTED, UNKNOWN -> sink.next(it);

                        case SUCCESS -> sink.error(
                                new IllegalStateException("Payment already completed for orderId: " + orderId));

                        case FAILURE -> sink.error(
                                new IllegalStateException("Payment failed for orderId: " + orderId));
                    }
                });
    }



    private Mono<PaymentStatusQueryDto> selectPaymentStatusQuery(String orderId) {
        String SELECT_PAYMENT_STATUS_QUERY = """
                SELECT id, payment_status FROM payment_events
                WHERE order_id = :orderId
                """.trim();

        return databaseClient.sql(SELECT_PAYMENT_STATUS_QUERY)
                .bind("orderId", orderId)
                .fetch()
                .one()
                .map(row -> {
                    Long id = ((Number) row.get("id")).longValue();
                    String paymentStatusStr = row.get("payment_status").toString();
                    PaymentStatus status = PaymentStatus.valueOf(paymentStatusStr);

                    return new PaymentStatusQueryDto(id, status);
                });
    }




}
