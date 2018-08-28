package com.example.rabbitmqtest.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * Created by Michael Sackmann on 30.03.2018.
 */
@Component
public class Configuration {

    public static final String topicExchangeNameReplication = "spring-boot-exchange-replication";
    public static final String topicExchangeNameFulfillment = "spring-boot-exchange-fulfillment";

    public static final String queueNameReplication = "order-s4-replication.order-update-event.version.1.queue";
    public static final String queueNameFulfillment = "order-s4-replication.order-fulfillment-status-update-event.version.1.queue";

    @Bean
    Queue queueReplication() {
        return new Queue(queueNameReplication, true);
    }

    @Bean
    Queue queueFulfillment() {
        return new Queue(queueNameFulfillment, true);
    }

    @Bean
    TopicExchange exchangeReplication() {
        return new TopicExchange(topicExchangeNameReplication);
    }

    @Bean
    TopicExchange exchangeFulfillment() {
        return new TopicExchange(topicExchangeNameFulfillment);
    }

    @Bean
    Binding bindingFulFillment() {
        return BindingBuilder.bind(queueFulfillment()).to(exchangeFulfillment()).with("foo.bar.#");
    }

    @Bean
    Binding bindingReplication() {
        return BindingBuilder.bind(queueReplication()).to(exchangeReplication()).with("foo.bar.#");
    }

}
