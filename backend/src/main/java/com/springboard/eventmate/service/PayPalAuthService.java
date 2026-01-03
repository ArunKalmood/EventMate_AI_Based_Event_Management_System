package com.springboard.eventmate.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Base64;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springboard.eventmate.service.dto.PayPalCaptureResult;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PayPalAuthService {

    @Value("${paypal.client.id}")
    private String clientId;

    @Value("${paypal.client.secret}")
    private String clientSecret;

    @Value("${paypal.base.url}")
    private String baseUrl;

    // =========================
    // CURRENCY RULES
    // =========================
    private static final BigDecimal INR_TO_USD_RATE = new BigDecimal("83.00");
    private static final BigDecimal MIN_USD_AMOUNT = new BigDecimal("1.00");

    private final RestTemplate restTemplate = new RestTemplate();

    // =========================
    // GET PAYPAL ACCESS TOKEN
    // =========================
    public String getAccessToken() {

        String auth = clientId + ":" + clientSecret;
        String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> request =
                new HttpEntity<>("grant_type=client_credentials", headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        baseUrl + "/v1/oauth2/token",
                        request,
                        Map.class
                );

        Map body = response.getBody();
        if (body == null || body.get("access_token") == null) {
            throw new RuntimeException("PayPal auth failed: access token missing");
        }

        return body.get("access_token").toString();
    }

    // =========================
    // CREATE PAYPAL ORDER
    // =========================
    public String createOrder(String accessToken, BigDecimal amountInInr) {

        BigDecimal usdAmount = convertInrToUsd(amountInInr);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = """
        {
          "intent": "CAPTURE",
          "purchase_units": [{
            "amount": {
              "currency_code": "USD",
              "value": "%s"
            }
          }]
        }
        """.formatted(usdAmount.toPlainString());

        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        baseUrl + "/v2/checkout/orders",
                        request,
                        Map.class
                );

        Map bodyMap = response.getBody();
        if (bodyMap == null || bodyMap.get("id") == null) {
            throw new RuntimeException("PayPal create order failed");
        }

        return bodyMap.get("id").toString();
    }

    // =========================
    // CAPTURE PAYPAL ORDER
    // =========================
    public PayPalCaptureResult captureOrder(
            String accessToken,
            String paypalOrderId,
            BigDecimal expectedUsdAmount
    ) {

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        ResponseEntity<Map> response =
                restTemplate.postForEntity(
                        baseUrl + "/v2/checkout/orders/" + paypalOrderId + "/capture",
                        request,
                        Map.class
                );

        Map body = response.getBody();
        if (body == null) {
            throw new RuntimeException("PayPal capture failed: empty response");
        }

        List<Map> purchaseUnits = (List<Map>) body.get("purchase_units");
        if (purchaseUnits == null || purchaseUnits.isEmpty()) {
            throw new RuntimeException("PayPal capture failed: purchase_units missing");
        }

        Map payments = (Map) purchaseUnits.get(0).get("payments");
        if (payments == null) {
            throw new RuntimeException("PayPal capture failed: payments missing");
        }

        List<Map> captures = (List<Map>) payments.get("captures");
        if (captures == null || captures.isEmpty()) {
            throw new RuntimeException("PayPal capture failed: captures missing");
        }

        Map capture = captures.get(0);

        String captureId = (String) capture.get("id");
        String status = (String) capture.get("status");

        Map amount = (Map) capture.get("amount");
        String value = (String) amount.get("value");
        String currency = (String) amount.get("currency_code");

        if (!"COMPLETED".equals(status)) {
            throw new RuntimeException("PayPal capture not completed");
        }

        BigDecimal paidAmount = new BigDecimal(value);
        if (paidAmount.compareTo(expectedUsdAmount) != 0) {
            throw new RuntimeException("PayPal amount mismatch");
        }

        if (!"USD".equals(currency)) {
            throw new RuntimeException("Invalid PayPal currency");
        }

        return new PayPalCaptureResult(captureId, paidAmount, currency);
    }


    // =========================
    // INR → USD CONVERSION (SINGLE SOURCE)
    // =========================
    private BigDecimal convertInrToUsd(BigDecimal inrAmount) {

        if (inrAmount == null || inrAmount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than zero");
        }

        BigDecimal usd = inrAmount
                .divide(INR_TO_USD_RATE, 2, RoundingMode.HALF_UP);

        if (usd.compareTo(MIN_USD_AMOUNT) < 0) {
            throw new IllegalStateException(
                "Amount too low for PayPal. Minimum is $1.00 USD"
            );
        }

        return usd;
    }
}
