package com.lgs.lgsclient;

import com.lgs.lgsclient.Activities.UserActivity;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Consumer {
    ConnectionFactory factory;
    Connection connection;
    Channel channel;
    QueueingConsumer consumer;
    Session session;

    public Consumer() throws IOException, TimeoutException {
        session = new Session(UserActivity.getContext());
        this.factory = new ConnectionFactory();
        factory.setHost("192.168.1.95");
        factory.setUsername("user");
        factory.setPassword("password");
        System.out.println(session.getUserID());
        this.connection = factory.newConnection();
        this.channel = connection.createChannel();
        channel.queueDeclare(session.getUserID(), false,false,false, null);
        consumer = new QueueingConsumer(channel);
    }

    public String consume() throws IOException, InterruptedException {
        channel.basicConsume(session.getUserID(), true, consumer);
        QueueingConsumer.Delivery delivery = consumer.nextDelivery();
        return new String(delivery.getBody());
    }
}
