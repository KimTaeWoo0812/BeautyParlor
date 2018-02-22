package com.beautyparlor.a2_core;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

/**
 * Service class dispatcher by uri
 * 
 * @author kris
 */
@Component
public class ServiceDispatcher {
    private static ApplicationContext springContext;

    @Autowired
    public void init(ApplicationContext springContext) {
        ServiceDispatcher.springContext = springContext;
    }

    protected Logger logger = LogManager.getLogger(this.getClass());
    
    static String getDate() {
        long time = System.currentTimeMillis(); 
        SimpleDateFormat f = new SimpleDateFormat("yyyy:MM:dd//hh:mm:ss");
        return f.format(new Date(time));
    }
    
    public static ApiRequest dispatch(Map<String, String> requestMap) {
        String serviceUri = requestMap.get("REQUEST_URI");
        String beanName = null;

        System.out.println(" 받은 링크: "+requestMap.get("REQUEST_URI")+"   "+requestMap.get("REQUEST_METHOD")+"\t\t"+getDate());
        if (serviceUri == null) {
            beanName = "notFound";
        }
        String httpMethod = requestMap.get("REQUEST_METHOD");

        if (serviceUri.startsWith("/users")) {
            switch (httpMethod) {
            case "POST":
                beanName = "users_post";
                break;
            case "GET":
                beanName = "users_GET";
                break;
            case "PUT":
                beanName = "usersBeautySet_PUT";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
//        else if (serviceUri.startsWith("/test")) {
//            switch (httpMethod) {
//            case "POST":
//                beanName = "test";
//                break;
//            }
//        }
        else if (serviceUri.startsWith("/add_beauty")) {
            switch (httpMethod) {
            case "POST":
                beanName = "add_beauty";
                break;
                
            default:
                beanName = "notFound";
                break;
            }
        }
        
        
        else if (serviceUri.startsWith("/beauty")) {
            switch (httpMethod) {
            case "GET":
                beanName = "beautyList_GET";
                break;
                
            case "POST":
                beanName = "save_booking_POST";
                break;
                
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/beautyListUsingName")) {
            switch (httpMethod) {
            case "GET":
                beanName = "beautyListUsingName";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/booking")) {
            switch (httpMethod) {

            case "GET":
                beanName = "BookingList_for_user_GET";
                break;
            case "POST":
                beanName = "save_booking_POST";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/master_newBookingList")) {
            switch (httpMethod) {

            case "GET":
                beanName = "master_newBookingList_GET";
                break;
            case "POST":
                beanName = "master_newBookingOk_POST";
                break;
            case "DELETE":
                beanName = "master_newBookingNo_DELETE";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/master_bookingList")) {
            switch (httpMethod) {

            case "GET":
                beanName = "master_bookingList_GET";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/setting_beauty")) {
            switch (httpMethod) {

            case "PUT":
                beanName = "setting_beauty_PUT";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/Booking_current")) {
            switch (httpMethod) {

            case "GET":
                beanName = "Booking_current_GET";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/master_make_penalty")) {
            switch (httpMethod) {

            case "PUT":
                beanName = "master_make_penalty_PUT";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/user_booking_cancel")) {
            switch (httpMethod) {

            case "DELETE":
                beanName = "user_booking_cancel_DELETE";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        else if (serviceUri.startsWith("/master_booking_cancel_DELETE")) {
            switch (httpMethod) {

            case "DELETE":
                beanName = "master_booking_cancel_DELETE";
                break;
            default:
                beanName = "notFound";
                break;
            }
        }
        
        
        
        
//        if (serviceUri.startsWith("/tokens")) {
//
//            switch (httpMethod) {
//            case "POST":
//                beanName = "tokenIssue";
//                break;
//            case "DELETE":
//                beanName = "tokenExpier";
//                break;
//            case "GET":
//                beanName = "tokenVerify";
//                break;
//
//            default:
//                beanName = "notFound";
//                break;
//            }
//        }
        else {
            beanName = "notFound";
        }

        ApiRequest service = null;
        try {
            service = (ApiRequest) springContext.getBean(beanName, requestMap);
        }
        catch (Exception e) {
            e.printStackTrace();
            service = (ApiRequest) springContext.getBean("notFound", requestMap);
            System.out.println("xxxxx");
        }

        return service;
    }
}
