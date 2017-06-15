package com.sm.drudge.move;

import com.sm.drudge.amqp.RabbitMqConfiguration;
import com.sm.drudge.amqp.RabbitMqConsumer;
import com.sm.drudge.amqp.RabbitMqConsumerEvent;
import com.sm.drudge.amqp.RabbitMqMessageListener;
import com.google.gson.Gson;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.sm.drudge.domain.DrudgeDomain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by Sreekanth Mahesala on 11/15/16.
 */
public abstract class TransferConfiguration implements RabbitMqMessageListener {

    private final Logger logger = LoggerFactory.getLogger(TransferConfiguration.class);
    public RabbitMqConfiguration rabbitMqConfiguration;
    public String message;
    public Class drudgeDomainClass;
    public DrudgeDomain transferSettings;

    public abstract void processTransfer();
    public abstract void rabbitMqReceiveMessages() throws IOException, TimeoutException;

    public Object convertToObject(Class o){
        Gson gson = new Gson();
        return gson.fromJson(message,o);
    }

    public void rabbitMqReceiveMessages(RabbitMqMessageListener listener) throws IOException, TimeoutException {
        Channel channel = rabbitMqConfiguration.channel();
        String queue = channel.queueDeclare().getQueue();
        channel.queueBind(queue, "com/sm/drudge/application", transferSettings.monitorQueue());
        Consumer consumer = new RabbitMqConsumer(channel, transferSettings, listener);
        channel.basicConsume(queue, false, consumer);
        logger.info("Listening to Rabbitmq@Drudge "+transferSettings.toString());
    };

    public void sendToRabbitMq() {
        try {
            rabbitMqConfiguration.convertAndSend(true);
        } catch (IOException e) {

        } catch (TimeoutException e) {

        }
    }

    public boolean filePartCheck(String fileName) {
        if(transferSettings.filePartCheck()) {
            return  fileName.contains(transferSettings.filePart());
        }
        return true;
    }

    public boolean fileActionCheck(String fileAction) {
        if(transferSettings.fileActionCheck()) {
            return fileAction.contains(transferSettings.fileAction());
        }
        return true;
    }

    @Override
    public void messageReceived(RabbitMqConsumerEvent event) {
        this.message = event.getMessage();

        if (event.getDestination().equals(transferSettings.forwardQueue())) {
            processTransfer();
        }
    }
}
