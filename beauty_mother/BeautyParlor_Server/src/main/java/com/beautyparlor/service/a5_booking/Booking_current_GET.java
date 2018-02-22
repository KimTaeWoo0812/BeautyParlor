package com.beautyparlor.service.a5_booking;


import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("Booking_current_GET")
@Scope("prototype")
public class Booking_current_GET extends ApiRequestTemplate {
    
    //이거 아직 안씀

    @Autowired
    private SqlSession sqlSession;

    public Booking_current_GET(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("user_id"))) {
            throw new RequestParamException("user_id가 없습니다.");
        }
    }

    public void service() throws ServiceException {

        int check = sqlSession.selectOne("booking.isFirst", this.reqData);

        System.err.println("check: " + this.reqData.get("user_id"));
        System.err.println("check: " + this.reqData.get("current_date"));
        System.err.println("check: " + check);
        if (check != 0) {
            try {
                Map<String, String> result = sqlSession.selectOne("booking.booking_current", this.reqData);

                System.err.println("beauty_title: " + result.get("beauty_title"));
                System.err.println("booking_date: " + result.get("booking_date"));
                
                String isDone = result.get("isDone");
                System.out.println(isDone);
                if(isDone.equals("0")){
                    //아직 승인 대기중
                    this.apiResult.addProperty("num", String.valueOf(result.get("num")));
                    this.apiResult.addProperty("beauty_title", result.get("beauty_title"));
                    this.apiResult.addProperty("booking_date", result.get("booking_date"));
                    this.apiResult.addProperty("booking_time", result.get("booking_time"));
                    this.apiResult.addProperty("hair_style", result.get("hair_style"));
                    this.apiResult.addProperty("memo", result.get("memo"));
                    this.apiResult.addProperty("resultCode", "201");
                    return;
                    
                }
                
                this.apiResult.addProperty("num", String.valueOf(result.get("num")));
                this.apiResult.addProperty("beauty_title", result.get("beauty_title"));
                this.apiResult.addProperty("booking_date", result.get("booking_date"));
                this.apiResult.addProperty("booking_time", result.get("booking_time"));
                this.apiResult.addProperty("hair_style", result.get("hair_style"));
                this.apiResult.addProperty("memo", result.get("memo"));
                this.apiResult.addProperty("resultCode", "200");

                return;

            }
            catch (Exception e) {
                e.printStackTrace();
                // TODO: handle exception
            }
            this.apiResult.addProperty("resultCode", "404");
        }

        this.apiResult.addProperty("resultCode", "404");

    }

}