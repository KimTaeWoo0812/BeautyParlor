package com.beautyparlor.service.a5_booking;


import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.beautyparlor.a1_main.SC;
import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.a2_core.FCM_Magager;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("user_booking_cancel_DELETE")
@Scope("prototype")
public class user_booking_cancel_DELETE extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public user_booking_cancel_DELETE(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
//        if (StringUtils.isEmpty(this.reqData.get("id"))) {
//            throw new RequestParamException("id가 없습니다.");
//        }
    }

    public void service() throws ServiceException {

        try {
            Map<String, String> map = sqlSession.selectOne("booking.get_phoneId_By_beautyNum_For_booking_cancel", this.reqData);

            String msg = this.reqData.get("date")+" "+this.reqData.get("user_name")+"님의 예약이 취소되었습니다.";


            System.out.println(map.get("phone_Id"));
            
            FCM_Magager fcmm = new FCM_Magager(map.get("phone_Id"), "예약 취소", msg, SC.isMaster_master);
            
            if(fcmm.Send_FCM()){
                this.apiResult.addProperty("resultCode", "404");
                return ;
            }

            sqlSession.delete("booking.user_booking_cancel", this.reqData);
            
            this.apiResult.addProperty("resultCode", "200");
            
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "404");
            e.printStackTrace();
        }
    }

}