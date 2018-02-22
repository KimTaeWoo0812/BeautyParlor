package beauty.tw.beauty_mother.a3_mainpage;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.BackPressCloseHandler;
import beauty.tw.beauty_mother.a1_sub_class.SC;
import beauty.tw.beauty_mother.a1_sub_class.serverCheck;
import beauty.tw.beauty_mother.a2_Roading_Join.atv_Roading;
import beauty.tw.beauty_mother.a4_Booking.atv_Booking;


/**
 * Created by kimtaewoo on 2017-01-13.
 */
public class atv_main extends AtvBase {
    private BackPressCloseHandler backPressCloseHandler;
    Context mContext = this;

    LinearLayout L1;
    LinearLayout L2;

    Button btn1;
    Button btnTel;
    LinearLayout btnRefresh;
    TextView txt1;
    TextView txtWorkingTime;
    TextView txtDayOff;
    GetData getData = new GetData();
    boolean boolServerCheck = false;

    protected final int REFRESH_DELAY_TIME = 5000;
    long startTime = System.currentTimeMillis() + REFRESH_DELAY_TIME;
    long endTime = System.currentTimeMillis();

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();

        new SendMsgTask().execute("1");
    }
    @Override
    protected void onPause() {
        super.onPause();
    }
    @Override
    protected void setView() {
        setView(R.layout.atv_main);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
        //moveTaskToBack(true);
    }

    @Override
    protected void init() {

        if(CBeautyInfo.num == null){
            Intent intent = new Intent(atv_main.this, atv_Roading.class);
            startActivity(intent);
            finish();
        }



        backPressCloseHandler = new BackPressCloseHandler(this);

        mTxtTopTitle.setText(CBeautyInfo.title);

        L1 = (LinearLayout) findViewById(R.id.L1);
        L2 = (LinearLayout) findViewById(R.id.L2);
        btn1 = (Button) findViewById(R.id.btn1);
        btnTel = (Button) findViewById(R.id.btnTel);
        btnRefresh = (LinearLayout) findViewById(R.id.btnRefresh);
        txt1 = (TextView) findViewById(R.id.txt1);
        txtWorkingTime = (TextView) findViewById(R.id.txtWorkingTime);
        txtDayOff = (TextView) findViewById(R.id.txtDayOff);


        txtWorkingTime.setText(CBeautyInfo.working_time_from+" ~ "+CBeautyInfo.working_time_to);
        txtDayOff.setText(CBeautyInfo.dayOff);




        btnTel.setText("미용실로 전화걸기\n("+CBeautyInfo.phone+")");


        //FCM code
//        FirebaseMessaging.getInstance().subscribeToTopic("news");
//        FirebaseInstanceId.getInstance().getToken();

        /**
         * FCM 날리는 부분
         */
//        MyFirebaseInstanceIDService a = new MyFirebaseInstanceIDService();
//        String token = FirebaseInstanceId.getInstance().getToken();
//        a.sendRegistrationToServer(token);


        L2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //예약 취소
                DialogSimple("예약 취소", "예약을 취소하시겠습니까?");

            }
        });

        btnTel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //전화걸기
                Dialog_tel(CBeautyInfo.phone);


            }
        });



        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(atv_main.this, atv_Booking.class);
                startActivity(intent2);
            }
        });
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                endTime = System.currentTimeMillis();

                if(endTime - startTime < 0){
                    Toast.makeText(atv_main.this,"최소 5초에 한번 갱신 가능합니다", Toast.LENGTH_SHORT).show();
                    return;
                }
                startTime = System.currentTimeMillis() + REFRESH_DELAY_TIME;

                Toast.makeText(atv_main.this,"갱신 완료!", Toast.LENGTH_SHORT).show();
                serverCheck.showLoading(atv_main.this);
                boolServerCheck=true;

                new SendMsgTask().execute("1");
            }
        });



        if(CBeautyInfo.isNotice){
            Dialog_notice(CBeautyInfo.notice);
        }

    }


    private void Dialog_notice(final String text) {
        final Dialog ratingDialog = new Dialog(atv_main.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_nomal);
        ratingDialog.setCancelable(true);

        TextView txtTitle = (TextView) ratingDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = (TextView) ratingDialog.findViewById(R.id.txtMsg);

        txtTitle.setText("공지사항");
        txtMsg.setText(text);

        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk); // 승인
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.dismiss();
            }
        });

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo); // 거부
        button.setVisibility(button.GONE);

        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();
    }

    private void Dialog_tel(final String num) {
        final Dialog ratingDialog = new Dialog(atv_main.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_nomal);
        ratingDialog.setCancelable(true);

        TextView txtTitle = (TextView) ratingDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = (TextView) ratingDialog.findViewById(R.id.txtMsg);

        txtTitle.setText("전화걸기");
        txtMsg.setText("미용실로 전화를 거시겠습니까?");

        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk); // 승인
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + num));
                if (ActivityCompat.checkSelfPermission(atv_main.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    return;
                }
                startActivity(intent2);

                ratingDialog.dismiss();
            }
        });
        button7.setText("전화걸기");

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo); // 거부
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingDialog.dismiss();
            }
        });
        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();
    }


    private void Dialog_reCheck()
    {
        String clause = "정말 예약을 취소하시겠습니까?";

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(clause).setCancelable(false).setPositiveButton("예약 취소하기", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id_)
            {
                new SendMsgTask().execute("3");
                dialog.cancel();
            }
        }).setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id_) {
                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        // Icon for AlertDialog
        alert.show();
        TextView msgView = (TextView) alert.findViewById(android.R.id.message);
        msgView.setTextSize(16);
    }


    private void DialogSimple(String title, String msg){
        final Dialog ratingDialog = new Dialog(atv_main.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_nomal);
        ratingDialog.setCancelable(true);

        TextView txtTitle = (TextView) ratingDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = (TextView) ratingDialog.findViewById(R.id.txtMsg);

        txtTitle.setText(title);
        txtMsg.setText(msg);

        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk); // 승인
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Dialog_reCheck();
                ratingDialog.dismiss();
            }
        });

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo); // 거부
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingDialog.dismiss();
            }
        });
        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();
    }




    private class GetData{
        String num;
        String beauty_title;
        String booking_date;
        String booking_time;
        String hair_style;
        String memo;

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
                map.put("user_id", CUserInfo.id);
                map.put("current_date", getDate());
                JSONObject json = SC.Json_Messenger("Booking_current", SC.GET, map);

                try {

                    String resultCode = json.get("resultCode").toString();

                    if(resultCode.equals("404"))
                        return "2";

                    if(resultCode.equals("201")){
                        //아직 승인 대기중
                        getData.num = json.get("num").toString();
                        getData.beauty_title = json.get("beauty_title").toString();
                        getData.booking_date = json.get("booking_date").toString();
                        getData.booking_time = json.get("booking_time").toString();
                        getData.hair_style = json.get("hair_style").toString();
                        getData.memo = json.get("memo").toString();
                        return "1_2";
                    }

                    getData.num = json.get("num").toString();
                    getData.beauty_title = json.get("beauty_title").toString();
                    getData.booking_date = json.get("booking_date").toString();
                    getData.booking_time = json.get("booking_time").toString();
                    getData.hair_style = json.get("hair_style").toString();
                    getData.memo = json.get("memo").toString();



                    return "1";
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            }
            else if (type.compareTo("3") == 0) {

                String phone_id = FirebaseInstanceId.getInstance().getToken();
                Map<String, String> map = new HashMap<>();
                map.put("num", getData.num);
                map.put("beauty_num", CBeautyInfo.num);
                map.put("phone_id", phone_id);
                map.put("user_name", CUserInfo.name);
                map.put("user_id", CBeautyInfo.master_id);
                map.put("beauty_title", getData.beauty_title);
                map.put("date", getData.booking_date+":"+getData.booking_time);



                JSONObject json = SC.Json_Messenger("user_booking_cancel", SC.DELETE, map);
                try {

                    String resultCode = json.get("resultCode").toString();


                    if(resultCode.equals("200")){
                        return "3";
                    }

                    return "0";


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
                //예약된게 있다
                Log.e("###","예약");
                L1.setVisibility(L1.GONE);
                L2.setVisibility(L2.VISIBLE);
                txt1.setText(getData.booking_date + "일 " + getData.booking_time + "분\n" + CUserInfo.name + "님의 예약 신청이 완료되었습니다.\n" + getData.memo + "\n#" + getData.hair_style);
            }
            else if (result.compareTo("1_2") == 0) {
                //아직 승인대기중
                L1.setVisibility(L1.GONE);
                L2.setVisibility(L2.VISIBLE);
                txt1.setText(" -- 예약 승인 대기중입니다 --"+"\n예약시간: "+getData.booking_date + " " + getData.booking_time);
            }
            else if (result.compareTo("2") == 0) {
                //예약된게 없다
                L2.setVisibility(View.GONE);
                L1.setVisibility(View.VISIBLE);

            }
            else if (result.compareTo("3") == 0) {
                Toast.makeText(mContext,"예약이 취소되었습니다", Toast.LENGTH_SHORT).show();
                L2.setVisibility(View.GONE);
                L1.setVisibility(View.VISIBLE);

            }
            else {
                Toast.makeText(mContext,"네트워크 오류입니다. 현재 예약 정보를 받는데 실패했습니다.", Toast.LENGTH_LONG).show();
            }

            if(boolServerCheck){
                serverCheck.hideLoading();
                boolServerCheck = false;
            }
        }
    }
    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new Date(time));
    }
}

