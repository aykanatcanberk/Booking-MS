package com.canbe.payment.service.service;

import com.canbe.payment.service.dto.BookingDto;
import com.canbe.payment.service.dto.PaymentLinkResponse;
import com.canbe.payment.service.dto.UserDto;
import com.canbe.payment.service.modal.PaymentMethod;
import com.canbe.payment.service.modal.PaymentOrder;
import com.canbe.payment.service.repository.PaymentOrderRepository;
import com.razorpay.PaymentLink;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentOrderRepository paymentRepository;

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${razorpay.api.secret}")
    private String razorpaySecretKey;

    @Value("${razorpay.api.key}")
    private String razorpayApiKey;

    @Override
    public PaymentLinkResponse createOrder(UserDto user, BookingDto booking, PaymentMethod paymentMethod) {

        Long amount = (long) booking.getTotalPrice();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setAmount(amount);
        paymentOrder.setBookingId(booking.getId());
        paymentOrder.setSalonId(booking.getSalonId());

        PaymentOrder savedOrder = paymentRepository.save(paymentOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if(paymentMethod.equals(PaymentMethod.RAZORPAY)){
            PaymentLink payment = createRazorPayPaymentLink(user, savedOrder.getAmount(), savedOrder.getId());
            String paymentUrl =  payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
            paymentLinkResponse.setGetPaymentLinkId(paymentUrlId);

            savedOrder.setPaymentLinkId(paymentUrlId);

            paymentRepository.save(savedOrder);
        }
        else{
            String paymentUrl = createStripePaymentLink(user, savedOrder.getAmount(), savedOrder.getId());
            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
        }

        return paymentLinkResponse;
    }

    @Override
    public PaymentOrder getPaymentOrderById(Long id) {
        return paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("PaymentOrder with ID " + id + " not found."));
    }

    @Override
    public PaymentOrder getPaymentOrderByPaymentId(String paymentId) {
        return paymentRepository.findByPaymentLinkId(paymentId);
    }

    @Override
    public PaymentLink createRazorPayPaymentLink(UserDto user, Long amount, Long orderId) {
        return null;
    }

    @Override
    public String createStripePaymentLink(UserDto user, Long amount, Long orderId) {
        return "";
    }
}
