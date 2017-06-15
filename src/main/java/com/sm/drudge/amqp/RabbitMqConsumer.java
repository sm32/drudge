package com.sm.drudge.amqp;

import com.rabbitmq.client.*;
import com.sm.drudge.domain.DrudgeDomain;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Sreekanth Mahesala on 11/14/16.
 */
public class RabbitMqConsumer implements Consumer {

    private final org.slf4j.Logger logger = LoggerFactory.getLogger(RabbitMqConsumer.class);
    private final Channel channel;
    private String message;
    private String consumerTag;
    private List consumerListeners = new ArrayList();
    private final DrudgeDomain drudgeDomain;

    public RabbitMqConsumer(Channel channel, DrudgeDomain drudgeDomain, RabbitMqMessageListener listener) {
        this.channel = channel;
        this.drudgeDomain = drudgeDomain;

        try {
            this.channel.basicQos(10);
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

        addMessageListener(listener);
    }

    @Override
    public void handleConsumeOk(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    @Override
    public void handleCancelOk(String consumerTag) {
    }

    @Override
    public void handleCancel(String consumerTag) throws IOException {
    }

    @Override
    public void handleShutdownSignal(String consumerTag, ShutdownSignalException e) {
    }

    @Override
    public void handleRecoverOk(String consumerTag) {
    }

    @Override
    public void handleDelivery(String s, Envelope envelope, AMQP.BasicProperties basicProperties, byte[] bytes) throws IOException {
        this.message = new String(bytes, "UTF-8");

        logger.info("[x] Received Message: "+ message);
        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    fireMessageEvent();
                } catch (Exception e) {
                    logger.info(e.getMessage());
                }
            }
        });
        t.start();

        channel.basicAck(envelope.getDeliveryTag(), false);

    }

    public void fireMessageEvent() throws IOException {
        RabbitMqConsumerEvent event = new RabbitMqConsumerEvent(this,this.message,this.drudgeDomain.forwardQueue());

        Iterator listener = consumerListeners.iterator();
        while(listener.hasNext()) {
            ((RabbitMqMessageListener) listener.next()).messageReceived(event);
        }
    }

    public void addMessageListener(RabbitMqMessageListener listener) {
        consumerListeners.add(listener);
    }

}
