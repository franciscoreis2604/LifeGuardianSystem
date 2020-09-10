package com.LGS.service;

public interface NotificationService {

    void send(String queueName, String Message);

    String receive(String queueName);
}

