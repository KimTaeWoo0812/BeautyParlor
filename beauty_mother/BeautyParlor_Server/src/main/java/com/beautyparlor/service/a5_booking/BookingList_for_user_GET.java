package com.beautyparlor.service.a5_booking;


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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

@Service("BookingList_for_user_GET")
@Scope("prototype")
public class BookingList_for_user_GET extends ApiRequestTemplate {
    
    //이거 아직 안씀

    @Autowired
    private SqlSession sqlSession;

    public BookingList_for_user_GET(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("id"))) {
            throw new RequestParamException("id가 없습니다.");
        }
    }

    public void service() throws ServiceException {

        try {
            List<Map<String, Object>> result = sqlSession.selectList("beauty.BookingList_for_user_GET", this.reqData);

            JsonArray infoArray = new JsonArray();
            for (int i = 0; i < result.size(); i++) {
                JsonObject bInfo = new JsonObject();
                bInfo.addProperty("num", result.get(i).get("num").toString());
                bInfo.addProperty("title", result.get(i).get("beauty_title").toString());
                bInfo.addProperty("style", result.get(i).get("hair_style").toString());
                bInfo.addProperty("memo", result.get(i).get("memo").toString());
                bInfo.addProperty("date", result.get(i).get("booking_date").toString() + ". "
                        + result.get(i).get("booking_time").toString());
                infoArray.add(bInfo);
            }
            this.apiResult.addProperty("resultCode", "200");
            this.apiResult.add("bookingyList", infoArray);
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "200");
            e.printStackTrace();
        }
    }

}