package com.beautyparlor.service.a3_users;

import java.sql.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a1_main.ServerManage;
import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;
import java.sql.SQLException;

@Service("users_GET")
@Scope("prototype")
public class users_GET extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public users_GET(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("id"))) {
            throw new RequestParamException("id가 없습니다.");
        }
    }

    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("yyyy/MM/dd");
        return f.format(new Date(time));
    }

    public void service() throws ServiceException {

        reqData.put("contact_date", getDate());
        try {
            sqlSession.update("users.loginTimeUpdate", this.reqData);

            Map<String, Object> result = sqlSession.selectOne("users.loginById_getBeautyInfo", this.reqData);
            
            
            //불량 사용자
            if(Integer.parseInt(String.valueOf(result.get("penalty_count"))) > 1){
                this.apiResult.addProperty("resultCode", "200");
                this.apiResult.addProperty("penalty_count", "out");
                
                return;
            }

            this.apiResult.addProperty("penalty_count", "ok");
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                this.apiResult.addProperty(key, result.get(key).toString());
//                System.out.println(key+" : "+result.get(key).toString());
            }
            this.apiResult.addProperty("resultCode", "200");
            
            ServerManage.countAccept++;
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "404");
            e.printStackTrace();
        }
    }

}