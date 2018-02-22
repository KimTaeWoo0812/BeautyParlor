package com.beautyparlor.service;

import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.a2_core.JedisHelper;
import com.google.gson.JsonObject;

import redis.clients.jedis.Jedis;

@Service("tokenIssue")
@Scope("prototype")
public class TokenIssue extends ApiRequestTemplate {
    private static final JedisHelper helper = JedisHelper.getInstance();

    @Autowired  
    private SqlSession sqlSession;

    public TokenIssue(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
        if (StringUtils.isEmpty(this.reqData.get("userNo"))) {
            throw new RequestParamException("userNo이 없습니다.");
        }

        if (StringUtils.isEmpty(this.reqData.get("password"))) {
            throw new RequestParamException("password가 없습니다.");
        }
    }

    public void service() throws ServiceException {
        Jedis jedis = null;
        try {
            Map<String, Object> result = sqlSession.selectOne("users.userList", this.reqData);

            if (result != null) {
                
                System.out.println("===========hsql================");
                
                Iterator<String> iterator = result.keySet().iterator();
                while (iterator.hasNext()) {
                    String key = (String) iterator.next();
                    System.out.println(" value="+result.get(key));
                }
                System.out.println("================================");
                
                
                
                
                final long threeHour = 60 * 60 * 3;
                long issueDate = System.currentTimeMillis() / 1000;
                String email = String.valueOf(result.get("USERID"));

                // token 만들기.
                JsonObject token = new JsonObject();
                token.addProperty("issueDate", issueDate);
                token.addProperty("expireDate", issueDate + threeHour);
                token.addProperty("email", email);
                token.addProperty("userNo", reqData.get("userNo"));

                // helper.
                this.apiResult.addProperty("resultCode", "200");
                this.apiResult.addProperty("message", "Success");
            }
            else {
                // 데이터 없음.
                this.apiResult.addProperty("resultCode", "404");
            }
        }
        catch (Exception e) {
            helper.returnResource(jedis);
        }
    }
}
