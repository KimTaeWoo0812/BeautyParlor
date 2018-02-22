package com.beautyparlor.service.a7.setting_beauty;


import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("setting_beauty_PUT")
@Scope("prototype")
public class setting_beauty_PUT extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public setting_beauty_PUT(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("num"))) {
            throw new RequestParamException("num가 없습니다.");
        }
    }

    public void service() throws ServiceException {

        sqlSession.update("beauty.setting_beauty", this.reqData);
        
       this.apiResult.addProperty("resultCode", "200");
            
    }

}