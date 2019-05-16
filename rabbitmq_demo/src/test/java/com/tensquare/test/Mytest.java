package com.tensquare.test;


import com.rabbitmq.demo.RdemoApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes= RdemoApplication.class)
public class Mytest{
    @Autowired
    private RabbitTemplate rabbitTemplate;

    /**
     * 直接模式
     */
    @Test
    public void testSend1(){
        rabbitTemplate.convertAndSend("q3","直接模式");
    }

    /**
     * 分列模式
     */
    @Test
    public void testSend2(){
        rabbitTemplate.convertAndSend("ec","","分列模式");
    }

    /**
     * 主题模式
     */
    @Test
    public void testSend3(){
        rabbitTemplate.convertAndSend("topictest","goods.log","主题模式");
    }
}
