package com.beautyparlor.a1_main;

import java.net.InetSocketAddress;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

@Configuration
@ImportResource("classpath:spring/hsqlApplicationContext.xml")
@ComponentScan("com.beautyparlor.a0_manage_master, com.beautyparlor.a1_main, com.beautyparlor.a2_core, com.beautyparlor.service, "
        + "com.beautyparlor.service.a4_beauty, com.beautyparlor.service.a3_users,"
        + "com.beautyparlor.service.a7_setting_beauty,"
        + "com.beautyparlor.service.a5_booking, com.beautyparlor.service.a6_booking_master")
@PropertySource("classpath:api-server.properties")
public class ApiServerConfig {
    @Value("${boss.thread.count}")
    private int bossThreadCount;
    
    @Value("${worker.thread.count}")
    private int workerThreadCount;

    @Value("${tcp.port}")
    private int tcpPort;

    @Bean(name = "bossThreadCount")
    public int getBossThreadCount() {
        return bossThreadCount;
    }

    @Bean(name = "workerThreadCount")
    public int getWorkerThreadCount() {
        return workerThreadCount;
    }

    public int getTcpPort() {
        return tcpPort;
    }

    @Bean(name = "tcpSocketAddress")
    public InetSocketAddress tcpPort() {
        return new InetSocketAddress(tcpPort);
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyPlaceholderConfigurer() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
