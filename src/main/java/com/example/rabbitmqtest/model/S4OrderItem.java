package com.example.rabbitmqtest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class S4OrderItem {
    private String matNr;
    private long quantity;
    private String unit;
    private String externalOrderLineNumber;
    private String externalDeliveryLineNumber;
    private String itemId;
    private String externalOrderId;
}
