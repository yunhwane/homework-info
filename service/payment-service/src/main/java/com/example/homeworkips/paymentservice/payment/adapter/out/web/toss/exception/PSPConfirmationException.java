package com.example.homeworkips.paymentservice.payment.adapter.out.web.toss.exception;


import com.example.homeworkips.paymentservice.payment.domain.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class PSPConfirmationException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    private Boolean isSuccess;
    private Boolean isUnknown;
    private Boolean isFailure;
    private Boolean isRetryableError;
    private Throwable cause;

    public PSPConfirmationException(String message, Throwable cause) {
        super(message, cause);
    }

    public static PSPConfirmationException of(String errorCode, String errorMessage, Boolean isSuccess, Boolean isUnknown, Boolean isFailure, Boolean isRetryableError, Throwable cause) {
        return new PSPConfirmationException(errorCode, errorMessage, isSuccess, isUnknown, isFailure, isRetryableError, cause);
    }
    public PaymentStatus getPaymentStatus() {

        if(isSuccess == true) {
            return PaymentStatus.SUCCESS;
        } else if(isFailure == true) {
            return PaymentStatus.FAILURE;
        } else if(isUnknown == true) {
            return PaymentStatus.UNKNOWN;
        } else {
            throw new IllegalStateException(
                String.format("결제 상태가 올바르지 않습니다. (errorCode: %s, errorMessage: %s)", errorCode, errorMessage)
            );
        }

    }
}
