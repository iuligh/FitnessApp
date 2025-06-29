package com.backend.DTOS;

import lombok.Data;

@Data
public class CheckoutSessionDTO {
    private String sessionId;
    private String sessionUrl;
    private String publicKey;
}
