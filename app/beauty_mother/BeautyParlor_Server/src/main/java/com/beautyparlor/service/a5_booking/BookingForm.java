package com.beautyparlor.service.a5_booking;

import java.util.Map;

import com.google.gson.JsonObject;

public class BookingForm {

    String beauty_num;
    String beauty_title;
    String user_id;
    String user_name;
    String user_sex;
    String booking_date;
    String booking_time;
    String end_time;
    String create_date;
    String memo;
    String hair_style;
    String booking_method;
    String isDone;
    
    
    public BookingForm(String beauty_num, String beauty_title, String user_id, String user_name, String user_sex, String booking_date,
            String booking_time, String end_time, String create_date, String memo, String hair_style,
            String booking_method,String isDone) {
        super();
        this.beauty_num = beauty_num;
        this.beauty_title = beauty_title;
        this.user_id = user_id;
        this.user_name = user_name;
        this.user_sex = user_sex;
        this.booking_date = booking_date;
        this.booking_time = booking_time;
        this.end_time = end_time;
        this.create_date = create_date;
        this.memo = memo;
        this.hair_style = hair_style;
        this.booking_method = booking_method;
        this.isDone = isDone;
    }

    //맵 받아서 셋팅
    public BookingForm(Map<String, String> map) {
        this.beauty_num = map.get("beauty_num");
        this.beauty_title = map.get("beauty_title");
        this.user_id = map.get("user_id");
        this.user_name = map.get("user_name");
        this.user_sex = map.get("user_sex");
        this.booking_date = map.get("booking_date");
        this.booking_time = map.get("booking_time");
        this.end_time = map.get("end_time");
        this.create_date = map.get("create_date");
        this.memo = map.get("memo");
        this.hair_style = map.get("hair_style");
        this.booking_method = map.get("booking_method");
        this.isDone = "0";
    }
    
    //json형식으로 데이터 리턴해줌
    public JsonObject Get_JsonData(JsonObject param){
        param.addProperty("beauty_num", beauty_num);
        param.addProperty("beauty_title", beauty_title);
        param.addProperty("user_id", user_id);
        param.addProperty("user_name", user_name);
        param.addProperty("user_sex", user_sex);
        param.addProperty("booking_date", booking_date);
        param.addProperty("booking_time", booking_time);
        param.addProperty("end_time", end_time);
        param.addProperty("create_date", create_date);
        param.addProperty("memo", memo);
        param.addProperty("hair_style", hair_style);
        param.addProperty("isDone", isDone);
        return param;
    }
    
    public String getBeauty_title() {
        return beauty_title;
    }

    public void setBeauty_title(String beauty_title) {
        this.beauty_title = beauty_title;
    }

    public String getIsDone() {
        return isDone;
    }

    public void setIsDone(String isDone) {
        this.isDone = isDone;
    }

    public String getBooking_method() {
        return booking_method;
    }

    public void setBooking_method(String booking_method) {
        this.booking_method = booking_method;
    }
   
    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }
    public String getBeauty_num() {
        return beauty_num;
    }
    public void setBeauty_num(String beauty_num) {
        this.beauty_num = beauty_num;
    }
    public String getUser_id() {
        return user_id;
    }
    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
    public String getUser_name() {
        return user_name;
    }
    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }
    public String getUser_sex() {
        return user_sex;
    }
    public void setUser_sex(String user_sex) {
        this.user_sex = user_sex;
    }
    public String getBooking_date() {
        return booking_date;
    }
    public void setBooking_date(String booking_date) {
        this.booking_date = booking_date;
    }
    public String getBooking_time() {
        return booking_time;
    }
    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }
    public String getCreate_date() {
        return create_date;
    }
    public void setCreate_date(String create_date) {
        this.create_date = create_date;
    }
    public String getMemo() {
        return memo;
    }
    public void setMemo(String memo) {
        this.memo = memo;
    }
    public String getHair_style() {
        return hair_style;
    }
    public void setHair_style(String hair_style) {
        this.hair_style = hair_style;
    }
    
    
    

}
