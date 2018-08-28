package com.example.rabbitmqtest.controller;

import com.example.rabbitmqtest.amqp.Configuration;
import com.example.rabbitmqtest.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;

/**
 * Created by Michael Sackmann on 30.03.2018.
 */
@Controller
public class PersonController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @RequestMapping(value = "/persons", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getPersons() {

        LOGGER.info("send message to rabbit");
        CorrelationData correlationData = new CorrelationData("abc");

        try {
            rabbitTemplate.convertAndSend(Configuration.topicExchangeNameReplication, "foo.bar.baz", createOrderReplicationMessage(), correlationData);
        } catch (JsonProcessingException e) {
            LOGGER.error("error occurred during messaging", e);
        }
        return ResponseEntity.ok(new Person("Michael", "Sackmann"));
    }

    @RequestMapping(value = "/cars", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity getCars() {

        LOGGER.info("send message to rabbit");
        CorrelationData correlationData = new CorrelationData("zzz");

        try {
            rabbitTemplate.convertAndSend(Configuration.topicExchangeNameFulfillment, "foo.bar.baz", createOrderDeliveryMessage(), correlationData);
        } catch (JsonProcessingException e) {
            LOGGER.error("error occurred during messaging", e);
        }
        return ResponseEntity.ok(new Car("Opel Zafira", "HD-JJ-1234"));
    }

    private Message createOrderDeliveryMessage() throws JsonProcessingException {
        OrderDeliveryUpdateMessage orderDeliveryMessage = new OrderDeliveryUpdateMessage();
        orderDeliveryMessage.setCreatedAt(Instant.now());
        orderDeliveryMessage.setDocumentNumber("80004711");
        orderDeliveryMessage.setSystemId("TESTSYSTEM");
        orderDeliveryMessage.setDeliveryEvents(new ArrayList<>());
        orderDeliveryMessage.getDeliveryEvents().add(new DeliveryEvent("006", Instant.now(), Instant.now()));
        orderDeliveryMessage.setItems(new HashSet<>());
        orderDeliveryMessage.getItems().add(new S4OrderItem("MAT1", 1, "EA", "1", "1", null, "4711"));

        return new Message(objectMapper.writeValueAsBytes(orderDeliveryMessage), createMessageProperties());
    }


    private Message createOrderReplicationMessage() throws JsonProcessingException {
        OrderReferenceUpdateMessage orderMessage = new OrderReferenceUpdateMessage();
        orderMessage.setWorkflowInstanceId("1");
        orderMessage.setCorrelationId("1234");
        orderMessage.setTenant("test");
        orderMessage.setContext(new OrderReferenceMessageContext());
        orderMessage.getContext().setExternalReferenceId("4711");
        orderMessage.getContext().setOrderId("6ee23627-bdd8-4a2f-988e-ef7bd2fceadb");
        orderMessage.getContext().setLineNumbers(new ArrayList<>());
        orderMessage.getContext().getLineNumbers().add(new LineNumberMapping("f900bc15-302e-4f8d-8e52-4a1b2aff6826", "1"));

        return new Message(objectMapper.writeValueAsBytes(orderMessage), createMessageProperties());
    }


    private MessageProperties createMessageProperties() {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("X-tenant", "bf2e8407-5fa3-4137-9683-75d4e95c8c6c");
        messageProperties.setHeader("hybris-tenant", "bf2e8407-5fa3-4137-9683-75d4e95c8c6c");
        messageProperties.setHeader("X-Ngom-Tenant", "bf2e8407-5fa3-4137-9683-75d4e95c8c6c");
        messageProperties.setHeader("correlation_id", "1234");
        messageProperties.setHeader("X-Product-Configuration", "ew0KICAiZmVhdHVyZXMiOiBbDQogICAgeyJuYW1lIjogIk9yZGVyX1M0X0Z1bGZpbGxtZW50X1N0YXR1cyIsICJlbmFibGVkIjogdHJ1ZX0NCiAgXQ0KfQ==");
        return messageProperties;
    }
}
