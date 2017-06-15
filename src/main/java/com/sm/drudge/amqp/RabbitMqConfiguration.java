package com.sm.drudge.amqp;

import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.sm.drudge.domain.DrudgeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


/**
 * Created by Sreekanth Mahesala on 11/7/16.
 */


public class RabbitMqConfiguration {

    private final Logger logger = LoggerFactory.getLogger(RabbitMqConfiguration.class);
    private DrudgeDomain drudgeDomainObject;
    private Connection connection;

    public boolean convertAndSend() throws IOException, TimeoutException {
        return convertAndSend(false);
    }

    public boolean convertAndSend(Boolean keepConnectionAlive) throws IOException, TimeoutException {
        String message = convertToJson();

        logger.info("[x] Sending message to com.sm.drudge.drudge@"+drudgeDomainObject.forwardQueue()+": "+message);

        Channel channel = channel();
        channel.basicPublish("com/sm/drudge/application", drudgeDomainObject.forwardQueue(), null, message.getBytes());

        if(!keepConnectionAlive) {
            channel.close();
            closeConnection();
        }

        return true;
    }

    public RabbitMqConfiguration(DrudgeDomain drudgeDomainObject) throws IOException, TimeoutException {
        this.drudgeDomainObject = drudgeDomainObject;

        String host = "sm-work";
        String user = "mahsr001";
        String password = "welcome@121";

        ConnectionFactory connectionFactory;

        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setUsername(user);
        connectionFactory.setPassword(password);
        connectionFactory.setConnectionTimeout(10000);

        connection = connectionFactory.newConnection();
    }

    public Channel channel()  throws IOException, TimeoutException {
        Channel channel = connection.createChannel();
        channel.exchangeDeclare("com/sm/drudge/application","direct");
        channel.basicQos(10);

        return channel;
    }

    private String convertToJson() {
        Gson gson = new Gson();
        String json = gson.toJson(drudgeDomainObject, drudgeDomainObject.getClass());

        return json;
    }

    public void closeConnection() throws IOException {
        connection.close();
    }

}

