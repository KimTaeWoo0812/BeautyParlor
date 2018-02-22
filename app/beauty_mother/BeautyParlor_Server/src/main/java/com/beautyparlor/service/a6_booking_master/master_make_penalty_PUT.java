package com.beautyparlor.service.a6_booking_master;


import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("master_make_penalty_PUT")
@Scope("prototype")
public class master_make_penalty_PUT extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public master_make_penalty_PUT(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
    }

    public void service() throws ServiceException {
        
        try {
            sqlSession.selectList("users.master_make_penalty", this.reqData);
            this.apiResult.addProperty("resultCode", "200");
            
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "404");
            e.printStackTrace();
        }
    }

}