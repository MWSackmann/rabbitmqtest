package com.example.rabbitmqtest.controller;

import com.example.rabbitmqtest.amqp.Configuration;
import com.example.rabbitmqtest.amqp.Receiver;
import com.example.rabbitmqtest.model.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Michael Sackmann on 30.03.2018.
 */
@Controller
public class PersonController {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Receiver receiver;

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonController.class);

    @RequestMapping(value = "/persons", method = RequestMethod.GET, produces = {"application/json"})
    public ResponseEntity get(){
        for(int i = 0; i < 100; i++) {
            LOGGER.info("send message to rabbit");
            rabbitTemplate.convertAndSend(Configuration.topicExchangeName, "foo.bar.baz", "Hello from RabbitMQ!");
        }

        LOGGER.info("return response");
        return ResponseEntity.ok(new Person("Michael", "Sackmann"));
    }
}
