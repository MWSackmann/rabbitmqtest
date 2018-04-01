package com.example.rabbitmqtest.amqp;

import com.example.rabbitmqtest.controller.PersonController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.CountDownLatch;

/**
 * Created by Michael Sackmann on 30.03.2018.
 */
@Component
public class Receiver {

    private static final Logger LOGGER = LoggerFactory.getLogger(Receiver.class);

    public void receiveMessage(String message) {
        LOGGER.info("Received <" + message + ">");
    }

}