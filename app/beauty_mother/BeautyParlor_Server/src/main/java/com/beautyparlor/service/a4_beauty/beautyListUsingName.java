package com.beautyparlor.service.a4_beauty;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

/**
 * 미용실 목록 전송
 * @author kimtaewoo
 *
 */

@Service("beautyListUsingName")
@Scope("prototype")
public class beautyListUsingName extends ApiRequestTemplate {

    @Autowired  
    private SqlSession sqlSession;

    public beautyListUsingName(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
      if (StringUtils.isEmpty(this.reqData.get("beautyTitle"))) {
      throw new RequestParamException("id가 없습니다.");
  }
    }

    public void service() throws ServiceException {
        List<Map<String, Object>> result = sqlSession.selectList("beauty.getBeatyListByName", this.reqData);
        

        JsonArray infoArray = new JsonArray(); 
        for(int i=0;i<result.size();i++){
            JsonObject bInfo = new JsonObject();
            bInfo.addProperty("num", result.get(i).get("num").toString());
            bInfo.addProperty("title", result.get(i).get("title").toString());
            bInfo.addProperty("loc", result.get(i).get("loc").toString());
            infoArray.add(bInfo);
        }
        this.apiResult.addProperty("resultCode", "200");

        this.apiResult.add("beautyList",infoArray);
        
    }
}