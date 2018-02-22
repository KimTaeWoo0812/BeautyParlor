package com.beautyparlor.service.a0_manage_master;


import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("add_beauty")
@Scope("prototype")
public class add_beauty extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public add_beauty(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
//        if (StringUtils.isEmpty(this.reqData.get("id"))) {
//            throw new RequestParamException("id가 없습니다.");
//        }
    }

    public void service() throws ServiceException {

        try {
            
            sqlSession.insert("manage_master.add_beauty");

            
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}