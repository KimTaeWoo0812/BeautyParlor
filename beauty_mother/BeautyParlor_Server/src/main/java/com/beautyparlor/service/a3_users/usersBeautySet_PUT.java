package com.beautyparlor.service.a3_users;

import java.util.Iterator;
import java.util.List;
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

/**
 * user테이블의 미용실 정보 수정하고 그 미용실 정보 돌려주기
 * 
 * @author kimtaewoo
 *
 */
@Service("usersBeautySet_PUT")
@Scope("prototype")
public class usersBeautySet_PUT extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public usersBeautySet_PUT(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("id"))) {
            throw new RequestParamException("id가 없습니다.");
        }
    }

    public void service() throws ServiceException {
        try {
            sqlSession.update("beauty.setUsersBeautyById", this.reqData);
            this.apiResult.addProperty("resultCode", "200");

            Map<String, Object> result = sqlSession.selectOne("users.getBeautyInfo", this.reqData);
            Iterator<String> iterator = result.keySet().iterator();
            while (iterator.hasNext()) {
                String key = (String) iterator.next();
                this.apiResult.addProperty(key, result.get(key).toString());
            }
            
            ServerManage.countAccept++;
        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }
}