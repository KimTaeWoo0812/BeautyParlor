

import java.util.Iterator;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import com.beautyparlor.a2_core.ApiRequestTemplate;
import com.beautyparlor.service.RequestParamException;
import com.beautyparlor.service.ServiceException;

@Service("test")
@Scope("prototype")
public class temp extends ApiRequestTemplate {

    @Autowired
    private SqlSession sqlSession;

    public temp(Map<String, String> reqData) {
        super(reqData);
    }

    public void requestParamValidation() throws RequestParamException {
//        if (StringUtils.isEmpty(this.reqData.get("id"))) {
//            throw new RequestParamException("id가 없습니다.");
//        }
    }

    public void service() throws ServiceException {

        reqData.put("contact_date", "");
        try {
            
            
            
            this.apiResult.addProperty("Content-Type", "application/json");
            this.apiResult.addProperty("Authorization", "AAAAKzHekBg:APA91bEjHDFR26MNmp_hwCpAzu-NOmJynXS4v3rp2Lpjf6yktnW7_b27Pv5gln2vbPIeLMBdK0GHqwGtLHCXYmB6D9b9CoCYHSxSA-n3-MpCvmr2mKflPxgd3OXtOGP67gGbHK6SA9Ob");
            this.apiResult.addProperty("Content-Type", "200");
            this.apiResult.addProperty("Content-Type", "200");
            this.apiResult.addProperty("Content-Type", "200");
            this.apiResult.addProperty("Content-Type", "200");
            
        }
        catch (Exception e) {
            this.apiResult.addProperty("resultCode", "404");
            e.printStackTrace();
        }
    }

}