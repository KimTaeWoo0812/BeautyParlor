package com.beautyparlor.service.a3_users;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("users_post")
@Scope("prototype")
public class users_POST extends ApiRequestTemplate {

    @Autowired  
    private SqlSession sqlSession;

    public users_POST(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("id"))) {
            throw new RequestParamException("id가 없습니다.");
        }
        if (StringUtils.isEmpty(this.reqData.get("name"))) {
            throw new RequestParamException("name이 없습니다.");
        }
        if (StringUtils.isEmpty(this.reqData.get("sex"))) {
            throw new RequestParamException("sex가 없습니다.");
        }
    }

    private String getDate() {
        long time = System.currentTimeMillis(); 
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        return f.format(new Date(time));
    }
    
    public void service() throws ServiceException {

        reqData.put("isMaster","0");
        reqData.put("joindate",getDate());
        reqData.put("contact_date",getDate());
        reqData.put("temp","0");

        String result = sqlSession.selectOne("users.isJoinedCheckById", this.reqData);

        if(result.equals("0")){ //처음 가입하는 번호면
            sqlSession.insert("users.insertUser", this.reqData);
            this.apiResult.addProperty("penalty_count", "ok");
        }
        else{ //이미 가입한 번호면

            Map<String, String> map = sqlSession.selectOne("users.isMasterById", this.reqData);
            

            if(map.get("isMaster").equals("1")){
                //관리자가 앱을 다시 깔았다
                map.put("phone_id", this.reqData.get("phone_id"));
                sqlSession.update("beauty.updateNewPhone_id", map);
                
            }
            sqlSession.update("users.updateUser", this.reqData);
            
            
            
          //불량 사용자
            Map<String, Object> result2 = sqlSession.selectOne("users.check_penalty_count", this.reqData);
            
            if(Integer.parseInt(String.valueOf(result2.get("penalty_count"))) > 1){
                this.apiResult.addProperty("resultCode", "200");
                this.apiResult.addProperty("penalty_count", "out");
                
                return;
            }
            this.apiResult.addProperty("penalty_count", "ok");
        }

        this.apiResult.addProperty("resultCode", "200");
        System.out.println(" test1) "+this.apiResult.get("resultCode"));
        
    }
}