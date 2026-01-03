package com.springboard.eventmate.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;

import com.paypal.core.PayPalHttpClient;
import com.paypal.http.HttpResponse;
import com.paypal.http.exceptions.HttpException;
import com.paypal.orders.AmountWithBreakdown;
import com.paypal.orders.ApplicationContext;
import com.paypal.orders.Order;
import com.paypal.orders.OrderRequest;
import com.paypal.orders.OrdersCreateRequest;
import com.paypal.orders.PurchaseUnitRequest;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayPalService {

    private final PayPalHttpClient payPalHttpClient;

    /**
     * Create a PayPal order (SANDBOX)
     * Returns approval URL
     */
    public String createOrder(Integer amountInRupees, Long bookingId) {

        try {
            // 1️ Order intent
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.checkoutPaymentIntent("CAPTURE");

            // 2️ Amount (INR)
            AmountWithBreakdown amount = new AmountWithBreakdown()
                    .currencyCode("INR")
                    .value(amountInRupees.toString());

            // 3️ Purchase unit
            PurchaseUnitRequest purchaseUnit = new PurchaseUnitRequest()
                    .referenceId("BOOKING_" + bookingId)
                    .description("EventMate Booking Payment")
                    .amountWithBreakdown(amount);

            orderRequest.purchaseUnits(List.of(purchaseUnit));

            // 4️ Redirect URLs (temporary, localhost)
            ApplicationContext context = new ApplicationContext()
                    .returnUrl("http://localhost:8080/payments/paypal/success")
                    .cancelUrl("http://localhost:8080/payments/paypal/cancel");

            orderRequest.applicationContext(context);

            // 5️ Create order request
            OrdersCreateRequest request = new OrdersCreateRequest()
                    .requestBody(orderRequest);

            HttpResponse<Order> response =
                    payPalHttpClient.execute(request);

            Order order = response.result();

            // 6️ Extract approval URL
            return order.links().stream()
                    .filter(link -> "approve".equalsIgnoreCase(link.rel()))
                    .findFirst()
                    .orElseThrow(() -> new RuntimeException("Approval URL not found"))
                    .href();

        } catch (HttpException e) {
            throw new RuntimeException("PayPal HTTP error", e);
        } catch (IOException e) {
            throw new RuntimeException("PayPal IO error", e);
        }
    }
}
