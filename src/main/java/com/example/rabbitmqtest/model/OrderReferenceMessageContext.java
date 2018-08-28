package com.example.rabbitmqtest.model;

import lombok.Data;

import java.util.List;

@Data
public class OrderReferenceMessageContext {
    private String orderId;
    private String externalReferenceId;
    private List<LineNumberMapping> lineNumbers;
}
