package com.LGS.service;

import com.LGS.exception.ApiRequestException;
import com.rabbitmq.client.*;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImplRbtMQ implements NotificationService {

    private final static String HOST_NAME = "localhost";

    @Override
    public void send(String QUEUE_NAME, String message) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST_NAME);
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            channel.basicPublish("", QUEUE_NAME, null, message.getBytes());

            channel.close();
            connection.close();
        } catch (Exception e) {
            throw new ApiRequestException("Failed to send Notification");
        }
    }

    @Override
    public String receive(String QUEUE_NAME) {
        final String[] message = new String[1];

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, false, false, false, null);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag,
                                           Envelope envelope,
                                           AMQP.BasicProperties properties,
                                           byte[] body) {
                    message[0] = new String(body);
                }
            };
            channel.basicConsume(QUEUE_NAME, true, consumer);
        } catch (Exception e) {
            throw new ApiRequestException("Failed to receive Notification,queue either empty or doesn't exist");
        }
        return message[0];
    }
}
