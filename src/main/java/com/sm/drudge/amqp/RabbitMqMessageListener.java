package com.sm.drudge.amqp;

/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public interface RabbitMqMessageListener {
    void messageReceived(RabbitMqConsumerEvent event);
}
