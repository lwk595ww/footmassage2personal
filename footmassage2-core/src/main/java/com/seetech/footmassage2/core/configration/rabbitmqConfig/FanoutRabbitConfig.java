package com.seetech.footmassage2.core.configration.rabbitmqConfig;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * 广播式交换机 扇形交换机
 */
@Configuration
public class FanoutRabbitConfig {

    /**********************************队列*********************************************/
    //测试队列1
    @Bean
    public Queue TestFanoutQueue1() {
        return new Queue("TestFanoutQueue1", true);
    }

    //测试队列2
    @Bean
    public Queue TestFanoutQueue2() {
        return new Queue("TestFanoutQueue2", true);
    }

    //测试队列3
    @Bean
    public Queue TestFanoutQueue3() {
        return new Queue("TestFanoutQueue3", true);
    }


    /*********************************交换机*********************************************/
    //测试交换机
    @Bean
    public FanoutExchange TestFanoutExchange() {
        return new FanoutExchange("TestFanoutExchange", true, false);
    }


    /*********************************绑定队列*********************************************/
    //测试绑定
    @Bean
    public Binding bindingExchange1() {
        return BindingBuilder.bind(TestFanoutQueue1()).to(TestFanoutExchange());
    }

    @Bean
    public Binding bindingExchange2() {
        return BindingBuilder.bind(TestFanoutQueue2()).to(TestFanoutExchange());
    }

    @Bean
    public Binding bindingExchange3() {
        return BindingBuilder.bind(TestFanoutQueue3()).to(TestFanoutExchange());
    }
}
