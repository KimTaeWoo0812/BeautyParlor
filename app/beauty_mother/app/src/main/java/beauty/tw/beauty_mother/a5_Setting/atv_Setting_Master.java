package beauty.tw.beauty_mother.a5_Setting;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_sub_class.Alert;
import beauty.tw.beauty_mother.a1_sub_class.SC;


/**
 * Created by kimtaewoo on 2017-01-15.
 */
public class atv_Setting_Master extends Activity {

    Context context = this;

    EditText txt_booking_time_hour_from;
    EditText txt_booking_time_minute_from;
    EditText txt_booking_time_hour_to;
    EditText txt_booking_time_minute_to;
    EditText txt_loc;
    RadioButton radioBtn1;
    RadioButton radioBtn2;
    CheckBox check_hadNotice;
    EditText txtBooking_delay_time;
    EditText txtBooking_delay_time_for_cut;
    EditText txtBooking_dayOff;
    EditText txtNotice;
    Button btn_Ok;
    LinearLayout layout_booking_delay_time;
    LinearLayout layout_booking_delay_time2;

    private String working_time_from="";
    private String working_time_to="";
    private String loc="";
    private String booking_method="";
    private String booking_delay_time="";
    private String booking_delay_time_for_cut="";
    private String booking_dayOff="";
    private String isNotice="";
    private String notice="";


    @Override
    protected void onResume(){
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.atv_setting_master);

        //id연결
        txt_booking_time_hour_from = (EditText)findViewById(R.id.txt_booking_time_hour_from);
        txt_booking_time_minute_from = (EditText)findViewById(R.id.txt_booking_time_minute_from);
        txt_booking_time_hour_to = (EditText)findViewById(R.id.txt_booking_time_hour_to);
        txt_booking_time_minute_to = (EditText)findViewById(R.id.txt_booking_time_minute_to);
        txtBooking_dayOff = (EditText)findViewById(R.id.txtBooking_dayOff);
        txt_loc = (EditText)findViewById(R.id.txt_loc);
        radioBtn1 = (RadioButton)findViewById(R.id.radioBtn1);
        radioBtn2 = (RadioButton)findViewById(R.id.radioBtn2);
        txtBooking_delay_time = (EditText)findViewById(R.id.booking_delay_time);
        txtBooking_delay_time_for_cut = (EditText)findViewById(R.id.txtBooking_delay_time_for_cut);
        btn_Ok = (Button)findViewById(R.id.btn_Ok);
        layout_booking_delay_time = (LinearLayout)findViewById(R.id.layout_booking_delay_time);
        layout_booking_delay_time2 = (LinearLayout)findViewById(R.id.layout_booking_delay_time2);

        check_hadNotice = (CheckBox)findViewById(R.id.check_hadNotice);
        txtNotice = (EditText)findViewById(R.id.txtNotice);



        //처음 보여줄 변수 값 셋팅
        String time_from[] = CBeautyInfo.working_time_from.split(":");
        txt_booking_time_hour_from.setText(time_from[0]);
        txt_booking_time_minute_from.setText(time_from[1]);
        String time_to[] = CBeautyInfo.working_time_to.split(":");
        txt_booking_time_hour_to.setText(time_to[0]);
        txt_booking_time_minute_to.setText(time_to[1]);

        txtBooking_dayOff.setText(CBeautyInfo.dayOff);

        txt_loc.setText(CBeautyInfo.loc);
        txtBooking_delay_time.setText(CBeautyInfo.booking_delay_time);
        txtBooking_delay_time_for_cut.setText(CBeautyInfo.booking_delay_time_for_cut);
        txtNotice.setText(CBeautyInfo.notice);

        if(CBeautyInfo.booking_method.equals("1")){ //중복예약 허용
            radioBtn1.setChecked(true);
        }
        else{ //중복예약 거부
            radioBtn2.setChecked(true);
            layout_booking_delay_time.setVisibility(layout_booking_delay_time.VISIBLE);
            layout_booking_delay_time2.setVisibility(layout_booking_delay_time2.VISIBLE);
        }
        radioBtn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_booking_delay_time.setVisibility(layout_booking_delay_time.GONE);
                layout_booking_delay_time2.setVisibility(layout_booking_delay_time2.GONE);
            }
        });
        radioBtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout_booking_delay_time.setVisibility(layout_booking_delay_time.VISIBLE);
                layout_booking_delay_time2.setVisibility(layout_booking_delay_time2.VISIBLE);
            }
        });
        check_hadNotice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(check_hadNotice.isChecked()){
                    txtNotice.setVisibility(txtNotice.VISIBLE);
                    txtNotice.setText(CBeautyInfo.notice);
                }
                else{
                    txtNotice.setVisibility(txtNotice.GONE);
                }
            }
        });


        if(CBeautyInfo.isNotice){
            check_hadNotice.setChecked(true);
            txtNotice.setVisibility(txtNotice.VISIBLE);
            txtNotice.setText(CBeautyInfo.notice);
        }


        btn_Ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //보내기 전 String 변수에 셋팅
                working_time_from = txt_booking_time_hour_from.getText().toString() + ":" + txt_booking_time_minute_from.getText().toString();
                working_time_to = txt_booking_time_hour_to.getText().toString() + ":" + txt_booking_time_minute_to.getText().toString();
                loc = txt_loc.getText().toString();
                booking_delay_time = txtBooking_delay_time.getText().toString();
                booking_delay_time_for_cut = txtBooking_delay_time_for_cut.getText().toString();
                booking_dayOff = txtBooking_dayOff.getText().toString();

                {
                    if (check_hadNotice.isChecked()) {
                        isNotice = "1";
                        notice = txtNotice.getText().toString();
                    }
                    else
                        isNotice = "0";

                }

                if (radioBtn1.isChecked()) { //중복예약 허용
                    booking_method = "1";
                } else { //중복예약 거부
                    booking_method = "2";
                }
                new SendMsgTask().execute("1");
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
                map.put("num", CBeautyInfo.num);
                map.put("working_time_from", working_time_from);
                map.put("working_time_to", working_time_to);
                map.put("loc", loc);
                map.put("booking_method", booking_method);
                map.put("booking_delay_time", booking_delay_time);
                map.put("booking_delay_time_for_cut", booking_delay_time_for_cut);
                map.put("booking_dayOff", booking_dayOff);
                map.put("isNotice", isNotice);
                map.put("notice", notice);

                JSONObject json = SC.Json_Messenger("setting_beauty", SC.PUT, map);

                try {
                    String resultCode = json.get("resultCode").toString();

                    if(resultCode.equals("200"))
                        return "1";


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                Alert alert = new Alert();
                alert.showAlert(context,"정보 수정 완료");
            }
            else if (result.compareTo("0") == 0) {
                Alert alert = new Alert();
                alert.showAlert(context,"정보 수정에 실패했습니다. 다시 시도해주세요");
            }
        }
    }
}