package com.example.rabbitmqtest.model;

import lombok.Data;


@Data
public class OrderReferenceUpdateMessage {

    private String workflowInstanceId;
    private String tenant;
    private String correlationId;
    private OrderReferenceMessageContext context;

}
