package beauty.tw.beauty_mother.a0_Google_FCM;

import android.os.AsyncTask;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.a1_sub_class.SC;


/**
 * Created by kimtaewoo on 2017-01-22.
 */

//주는곳
public class MyFirebaseInstanceIDService  extends FirebaseInstanceIdService {
    String token="";
    private static final String TAG = "MyFirebaseIIDService";

    // [START refresh_token]
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + token);


        /**
         * 이건 안쓴다
         */

        // 생성등록된 토큰을 개인 앱서버에 보내 저장해 두었다가 추가 뭔가를 하고 싶으면 할 수 있도록 한다.
        sendRegistrationToServer(token);
    }

    public void sendRegistrationToServer(String token2) {
        // Add custom implementation, as needed.

        this.token = token2;
//        new SendMsgTask().execute("1");
    }


    public class SendMsgTask extends AsyncTask<String, Void, String> {
        String msg = "";


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            if (type.compareTo("1") == 0) {


                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                JSONObject json = SC.Json_Messenger("test", SC.POST, map);


                return "1";
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {

            }
            else if (result.compareTo("0") == 0) {

            }
        }
    }
}