package com.canbe.payment.service.service;

import com.canbe.payment.service.dto.BookingDto;
import com.canbe.payment.service.dto.PaymentLinkResponse;
import com.canbe.payment.service.dto.UserDto;
import com.canbe.payment.service.modal.PaymentMethod;
import com.canbe.payment.service.modal.PaymentOrder;
import com.canbe.payment.service.modal.PaymentOrderStatus;
import com.canbe.payment.service.repository.PaymentOrderRepository;
import com.razorpay.Payment;
import com.razorpay.PaymentLink;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
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
    public PaymentLinkResponse createOrder(UserDto user, BookingDto booking, PaymentMethod paymentMethod) throws RazorpayException, StripeException {

        Long amount = (long) booking.getTotalPrice();

        PaymentOrder paymentOrder = new PaymentOrder();
        paymentOrder.setPaymentMethod(paymentMethod);
        paymentOrder.setAmount(amount);
        paymentOrder.setBookingId(booking.getId());
        paymentOrder.setSalonId(booking.getSalonId());

        PaymentOrder savedOrder = paymentRepository.save(paymentOrder);

        PaymentLinkResponse paymentLinkResponse = new PaymentLinkResponse();

        if (paymentMethod.equals(PaymentMethod.RAZORPAY)) {
            PaymentLink payment = createRazorPayPaymentLink(user, savedOrder.getAmount(), savedOrder.getId());
            String paymentUrl = payment.get("short_url");
            String paymentUrlId = payment.get("id");

            paymentLinkResponse.setPaymentLinkUrl(paymentUrl);
            paymentLinkResponse.setGetPaymentLinkId(paymentUrlId);

            savedOrder.setPaymentLinkId(paymentUrlId);

            paymentRepository.save(savedOrder);
        } else {
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
    public PaymentLink createRazorPayPaymentLink(UserDto user, Long Amount, Long orderId) throws RazorpayException {

        Long amount = Amount * 100;
        RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpaySecretKey);

        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", amount);
        paymentLinkRequest.put("currency", "TRY");

        JSONObject customer = new JSONObject();
        customer.put("name", user.getFullName());
        customer.put("email", user.getEmail());

        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);

        paymentLinkRequest.put("notify", notify);
        paymentLinkRequest.put("reminder_enable", true);
        paymentLinkRequest.put("callback_url", "http://localhost:3000/payment-success" + orderId);
        paymentLinkRequest.put("callback_method", "get");

        return razorpay.paymentLink.create(paymentLinkRequest);
    }


    @Override
    public String createStripePaymentLink(UserDto user, Long amount, Long orderId) throws StripeException {

        Stripe.apiKey = stripeSecretKey;

        SessionCreateParams params = SessionCreateParams.builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success" + orderId)
                .setCancelUrl("http://localhost:3000/payment-cancel" + orderId)
                .addLineItem(SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("try")
                                .setUnitAmount(amount * 100)
                                .setProductData(SessionCreateParams.LineItem
                                        .PriceData
                                        .ProductData
                                        .builder().setName("salon appointment booking").build()
                                ).build()
                        ).build()
                ).build();

        Session session = Session.create(params);

        return session.getUrl();
    }

    @Override
    public Boolean proceedPayment(PaymentOrder paymentOrder, String paymentId, String paymentLinkId) throws RazorpayException, StripeException {

        if (paymentOrder.getStatus().equals(PaymentOrderStatus.PENDING)) {
            if (paymentOrder.getPaymentMethod().equals(PaymentMethod.RAZORPAY)) {
                RazorpayClient razorpay = new RazorpayClient(razorpayApiKey, razorpaySecretKey);
                Payment payment = razorpay.payments.fetch(paymentId);
                Integer amount = payment.get("amount");
                String status = payment.get("status");

                if (status.equals("captured")) {
                    paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                    paymentRepository.save(paymentOrder);
                    return true;
                }

                return false;
            } else {
                paymentOrder.setStatus(PaymentOrderStatus.SUCCESS);
                paymentRepository.save(paymentOrder);
                return true;
            }
        }
        return false;
    }
}
