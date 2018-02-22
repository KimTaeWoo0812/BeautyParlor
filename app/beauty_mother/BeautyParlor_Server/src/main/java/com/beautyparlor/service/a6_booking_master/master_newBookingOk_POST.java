package com.beautyparlor.service.a6_booking_master;


import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a1_main.SC;
import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.a2_core.FCM_Magager;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("master_newBookingOk_POST")
@Scope("prototype")
public class master_newBookingOk_POST extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public master_newBookingOk_POST(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("num"))) {
            throw new RequestParamException("num가 없습니다.");
        }
    }

    public void service() throws ServiceException {
        
        Map<String, String> map = sqlSession.selectOne("booking.get_phoneId_By_userId_For_bookingOk", this.reqData);
        String msg =this.reqData.get("date")+"\n 예약이 승인되었습니다";
        
        System.out.println(map.get("phone_id"));
        FCM_Magager fcmm = new FCM_Magager(map.get("phone_id"), this.reqData.get("title"), msg, SC.isMaster_user);
        
        
        
        if(fcmm.Send_FCM()){
            this.apiResult.addProperty("resultCode", "404");
            return ;
        }
        sqlSession.update("booking.OK_booking_item", this.reqData);
        sqlSession.update("booking.OK_booking__add_cumulative_booking_count", this.reqData); //미용실 누적 예약 숫자 증가
       this.apiResult.addProperty("resultCode", "200");
            
    }

}