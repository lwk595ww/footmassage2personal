package com.seetech.footmassage2.core.configration.rabbitmqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 直连交换机 （固定 routeKey）
 */
@Configuration
public class DirectRabbitConfig {

    // 队列
    //队列 起名：TestDirectQueue
    @Bean
    public Queue TestDirectQueue() {
        return new Queue("TestDirectQueue", true);
    }

    @Bean
    public Queue TestDirectQueue2() {
        return new Queue("TestDirectQueue2", true);
    }


    //用户操作日志队列
    @Bean
    public Queue logQueue() {
        return new Queue("logQueue", true);
    }
    // 队列结束


    // 交换机
    //Direct交换机 起名：TestDirectExchange
    @Bean
    public DirectExchange TestDirectExchange() {
        return new DirectExchange("TestDirectExchange", true, false);
    }

    //日志交换机
    @Bean
    public DirectExchange logExchange() {
        return new DirectExchange("logDirectExchange", true, false);
    }
    // 交换机结束


    // 队列和交换机绑定
    //绑定  将队列和交换机绑定, 并设置用于匹配键：TestDirectRouting
    @Bean
    public Binding bindingDirect() {
        return BindingBuilder.bind(TestDirectQueue()).to(TestDirectExchange()).with("TestDirectRouting");
    }


    @Bean
    public Binding bindingDirect2() {
        return BindingBuilder.bind(TestDirectQueue2()).to(TestDirectExchange()).with("abc");
    }

    @Bean
    public Binding bindingLog() {
        return BindingBuilder.bind(logQueue()).to(logExchange()).with("log");
    }
    // 队列和交换机绑定结束

    @Bean
    DirectExchange lonelyDirectExchange() {
        return new DirectExchange("lonelyDirectExchange");
    }

}