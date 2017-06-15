package com.sm.drudge.amqp;

import java.util.EventObject;

/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public class RabbitMqConsumerEvent extends EventObject{

    private String message;
    private String destination;

    public RabbitMqConsumerEvent(Object source, String message, String destination) {
        super(source);
        this.message = message;
        this.destination = destination;
    }

    public String getMessage() {
        return message;
    }

    public String getDestination() {
        return destination;
    }
}
