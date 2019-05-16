package com.rabbitmq.demo.customers;

import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
@RabbitListener(queues = "q2")
public class Customer2 {

    @RabbitHandler
    public void showMessage(String msg){
        System.out.println("q2接受到消息："+msg);
    }
}
