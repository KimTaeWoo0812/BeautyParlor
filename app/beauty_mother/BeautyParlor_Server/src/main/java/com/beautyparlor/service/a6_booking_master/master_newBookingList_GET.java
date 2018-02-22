package com.beautyparlor.service.a6_booking_master;


import java.util.HashMap;
import java.util.Iterator;
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
import com.beautyparlor.service.a5_booking.BookingForm;
import com.beautyparlor.service.a5_booking.SC_BookingData;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("master_newBookingList_GET")
@Scope("prototype")
public class master_newBookingList_GET extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public master_newBookingList_GET(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("beauty_num"))) {
            throw new RequestParamException("beauty_num가 없습니다.");
        }
    }

    public void service() throws ServiceException {
        

        try{
        String beauty_num = this.reqData.get("beauty_num");
        List<Map<String, String>> result = sqlSession.selectList("booking.get_New_BookingList_forMaster", this.reqData);
        

        JsonArray infoArray = new JsonArray(); 
        
        for(int i=0;i<result.size();i++){
            Map<String, String> temp = result.get(i);

            if (temp.get("isDone").equals("0")) { // 아직 승인/거절 되지 않은 예약내역
                JsonObject bInfo = new JsonObject();
                bInfo.addProperty("num", String.valueOf(temp.get("num")));
                bInfo.addProperty("user_id", temp.get("user_id"));
                bInfo.addProperty("user_name", temp.get("user_name"));
                bInfo.addProperty("user_sex", temp.get("user_sex"));
                bInfo.addProperty("booking_date", temp.get("booking_date"));
                bInfo.addProperty("booking_time", temp.get("booking_time"));
                bInfo.addProperty("end_time", temp.get("end_time"));
                bInfo.addProperty("create_date", temp.get("create_date"));
                bInfo.addProperty("memo", temp.get("memo"));
                bInfo.addProperty("hair_style", temp.get("hair_style"));
                infoArray.add(bInfo);
            }
        }
        
        
        this.apiResult.addProperty("resultCode", "200");

        this.apiResult.add("bookingyList",infoArray);
        } catch(Exception e){
            e.printStackTrace();
        }
    }

}