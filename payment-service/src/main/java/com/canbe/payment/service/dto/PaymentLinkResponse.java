package com.canbe.payment.service.dto;

import lombok.Data;

@Data
public class PaymentLinkResponse {
    private String paymentLinkUrl;
    private String getPaymentLinkId;
}
