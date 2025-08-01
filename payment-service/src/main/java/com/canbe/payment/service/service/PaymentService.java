package com.canbe.payment.service.service;

import com.canbe.payment.service.dto.BookingDto;
import com.canbe.payment.service.dto.PaymentLinkResponse;
import com.canbe.payment.service.dto.UserDto;
import com.canbe.payment.service.modal.PaymentMethod;
import com.canbe.payment.service.modal.PaymentOrder;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;

public interface PaymentService {
    PaymentLinkResponse createOrder(UserDto user, BookingDto booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException;

    PaymentOrder getPaymentOrderById(Long id);

    PaymentOrder getPaymentOrderByPaymentId(String paymentId);

    PaymentLink createRazorPayPaymentLink(UserDto user, Long amount, Long orderId) throws RazorpayException;

    String createStripePaymentLink(UserDto user, Long amount, Long orderId) throws StripeException;

    Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException, StripeException;
}
