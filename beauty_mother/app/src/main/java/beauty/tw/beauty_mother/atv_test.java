package beauty.tw.beauty_mother;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.SC;


/**
 * Created by kimtaewoo on 2017-01-15.
 */
public class atv_test extends AtvBase {

    // 설정 일시
    private GregorianCalendar mCalendar;
    //시작 설정 클래스
    private TimePicker mTime;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void setView() {
        setView(R.layout.atv_test);
    }

    @Override
    protected void init() {
        mTxtTopTitle.setText("기본정보 입력");
        //현재 시각을 취득
        mCalendar = new GregorianCalendar();

        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());

        //셋 버튼, 리셋버튼의 리스너를 등록
        Button b = (Button) findViewById(R.id.set);
        b.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //여기 확인 만들면 댐

            }
        });

        //일시 설정 클래스로 현재 시각을 설정
        mTime = (TimePicker) findViewById(R.id.time_picker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY) + 1);
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));

        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                //여기 시간 쓰면 된다

                Toast.makeText(atv_test.this, Integer.toString(hourOfDay)+"시 "+ Integer.toString(minute), Toast.LENGTH_SHORT).show();
            }
        });
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
                map.put("id", CUserInfo.id);
                JSONObject json = SC.Json_Messenger("users", SC.PUT, map);

                try {
                    String resultCode = json.get("resultCode").toString();

                    if(!resultCode.equals("200"))
                        return "0";

                    CBeautyInfo.num = json.get("num").toString();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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