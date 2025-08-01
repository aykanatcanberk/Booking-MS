package com.canbe.payment.service.controller;

import com.canbe.payment.service.dto.BookingDto;
import com.canbe.payment.service.dto.PaymentLinkResponse;
import com.canbe.payment.service.dto.UserDto;
import com.canbe.payment.service.modal.PaymentMethod;
import com.canbe.payment.service.modal.PaymentOrder;
import com.canbe.payment.service.service.PaymentService;
import com.razorpay.RazorpayException;
import com.stripe.exception.StripeException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/create")
    public ResponseEntity<PaymentLinkResponse> createPayment(@RequestBody BookingDto booking, @RequestParam PaymentMethod paymentMethod) throws RazorpayException, StripeException {

        UserDto userDto = new UserDto();
        userDto.setFullName("John Doe");
        userDto.setId(1L);
        userDto.setEmail("johndoe@gmail.com");

        PaymentLinkResponse paymentLinkResponse = paymentService.createOrder(userDto, booking, paymentMethod);

        return ResponseEntity.ok(paymentLinkResponse);
    }

    @GetMapping("/{paymentOrderId}")
    public ResponseEntity<PaymentOrder> getPaymentOrderById(@PathVariable Long paymentOrderId) {
        try {
            PaymentOrder paymentOrder = paymentService.getPaymentOrderById(paymentOrderId);
            return ResponseEntity.ok(paymentOrder);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/proceed")
    public ResponseEntity<Boolean> proceedPayment(
            @RequestParam String paymentId,
            @RequestParam String paymentLinkId) throws StripeException, RazorpayException {

        PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
        Boolean response = paymentService.proceedPayment(paymentOrder,paymentId,paymentLinkId);

        return ResponseEntity.ok(response);

    }
}
