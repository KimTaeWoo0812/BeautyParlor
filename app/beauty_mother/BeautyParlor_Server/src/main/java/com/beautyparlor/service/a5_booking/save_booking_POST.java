package com.beautyparlor.service.a5_booking;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
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

@Service("save_booking_POST")
@Scope("prototype")
public class save_booking_POST extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public save_booking_POST(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
//        if (StringUtils.isEmpty(this.reqData.get("id"))) {
//            throw new RequestParamException("id가 없습니다.");
//        }
    }

    public void service() throws ServiceException {

        try {
            
            String booking_method = this.reqData.get("booking_method");
            
            //중복 불가능
            if(booking_method.equals("2")){
                System.out.println("1");
                //이미 예약한 사람이 한번 더 못하게 하기위해
                String t[] = this.reqData.get("create_date").split(" ");
                this.reqData.put("tempDate", t[0]);
                int check = sqlSession.selectOne("booking.isFirst", this.reqData);

                System.out.println("2 "+check);
                if(check != 0){
                    System.out.println("2 "+check);
                    this.apiResult.addProperty("value", "3"); //이미 예약된게 있다. 실패
                    this.apiResult.addProperty("resultCode", "200");
                    return;
                }

                List<Map<String, String>> result = sqlSession.selectList("booking.getBookingList_forUserSave", this.reqData);
                
                for(int i=0;i<result.size();i++){
                    Map<String, String> temp = result.get(i);

                    System.out.println(temp.get("booking_time"));
                    System.out.println(temp.get("end_time"));
                    System.out.println(this.reqData.get("booking_time"));
                    System.out.println(this.reqData.get("end_time"));
                    
                    SimpleDateFormat transFormat = new SimpleDateFormat("HH:mm");
                    Date a_booking_time = (Date) transFormat.parse(temp.get("booking_time"));
                    Date a_end_time = (Date) transFormat.parse(temp.get("end_time"));
                    
                    Date booking_time = (Date) transFormat.parse(this.reqData.get("booking_time"));
                    Date end_time = (Date) transFormat.parse(this.reqData.get("end_time"));
                    
                    
//                    int a_booking_time = Integer.valueOf(temp.get("booking_time"));
//                    int a_end_time = Integer.valueOf(temp.get("end_time"));
                    
                    //시간 중복 체크                    
//                    if((a_booking_time < booking_time
//                            && booking_time < a_end_time)
//                        ||
//                        (a_booking_time < end_time
//                                && end_time < a_end_time))
//                    {

                    if((booking_time.compareTo(a_booking_time) > 0 && a_end_time.compareTo(booking_time) > 0)
                            ||
                       (end_time.compareTo(a_booking_time) > 0 && a_end_time.compareTo(end_time) > 0))
                    {
                        System.out.println("1valuevaluevalue");
                        this.apiResult.addProperty("value", "2"); //예약 실패
                        this.apiResult.addProperty("resultCode", "200");
                        return;
                    }
                    
                    
                }
            }
            
            Map<String, String> map = sqlSession.selectOne("booking.get_phoneId_By_beautyNum_For_booking", this.reqData);
            String msg =this.reqData.get("booking_date")+"  "+this.reqData.get("booking_time")+"\n 예약이 신청되었습니다";
            System.out.println(map.get("master_phone_id"));
            System.out.println(msg);
            
            FCM_Magager fcmm = new FCM_Magager(map.get("master_phone_id"), "예약 신청", msg, SC.isMaster_master);
            
            if(fcmm.Send_FCM()){
                this.apiResult.addProperty("resultCode", "404");
                return ;
            }
            sqlSession.insert("booking.insertBooking_data", this.reqData);
                

            this.apiResult.addProperty("value", "1"); //예약 성공
            this.apiResult.addProperty("resultCode", "200");
            
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "404");
            e.printStackTrace();
        }
    }

}