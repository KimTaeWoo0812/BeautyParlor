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

@Service("master_newBookingNo_DELETE")
@Scope("prototype")
public class master_newBookingNo_DELETE extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public master_newBookingNo_DELETE(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("num"))) {
            throw new RequestParamException("num가 없습니다.");
        }
    }

    public void service() throws ServiceException {

        Map<String, String> map = sqlSession.selectOne("booking.get_phoneId_By_beautyNum_For_master", this.reqData);
        String msg =this.reqData.get("date")+"\n 예약이 거절되었습니다";

        FCM_Magager fcmm = new FCM_Magager(map.get("phone_Id"), this.reqData.get("title"), msg, SC.isMaster_user);
        

        if(fcmm.Send_FCM()){
            this.apiResult.addProperty("resultCode", "404");
            return ;
        }
        sqlSession.update("booking.DELETE_item", this.reqData);
        
       this.apiResult.addProperty("resultCode", "200");
            
    }

}