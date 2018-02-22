package com.beautyparlor.a1_main;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.AbstractApplicationContext;

public class ApiServerMain {
    public static void main(String[] args) {
        AbstractApplicationContext springContext = null;
        try {
            springContext = new AnnotationConfigApplicationContext(ApiServerConfig.class);
            springContext.registerShutdownHook();

            //서버 관리 클래스
            ServerManage server_manager = new ServerManage();
            server_manager.start();
            
            
            ApiServer server = springContext.getBean(ApiServer.class);
            server.start();
        }
        finally {
            springContext.close();
        }
    }
}
