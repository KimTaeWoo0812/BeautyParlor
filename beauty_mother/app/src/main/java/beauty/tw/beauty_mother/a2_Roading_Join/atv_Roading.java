package beauty.tw.beauty_mother.a2_Roading_Join;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextThemeWrapper;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.Alert;
import beauty.tw.beauty_mother.a1_sub_class.CSharedPreferences;
import beauty.tw.beauty_mother.a1_sub_class.MarketVersionChecker;
import beauty.tw.beauty_mother.a1_sub_class.SC;
import beauty.tw.beauty_mother.a3_mainpage.atv_main;
import beauty.tw.beauty_mother.a6_master_Package.atv_master;

/**
 * Created by kimtaewoo on 2017-01-13.
 */
public class atv_Roading extends Activity {

    CSharedPreferences sharedPreferences;
    int isLogin;
    Context context = this;
    String isMaster;
    boolean gotoUpdate = false;
    @Override
    protected void onResume() {
        super.onResume();

    }



    String deviceVersion;
    String storeVersion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roading);

        sharedPreferences = new CSharedPreferences(this);
        isLogin = sharedPreferences.IsLogin();

        BackgroundThread mBackgroundThread = new BackgroundThread();
        mBackgroundThread.start();


        Handler hd = new Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {

                if(!gotoUpdate) {
                    if (isLogin != 1) {
                        //처음 접속
                        Intent intent2 = new Intent(atv_Roading.this, atv_Join.class);
                        startActivity(intent2);
                        finish();// 이 화면 종료
                    } else //로그인 정보 있음.
                        new SendMsgTask().execute("1"); //로그인 시도
                }
            }
        }, 1200);
    }

    public class BackgroundThread extends Thread {
        @Override
        public void run() {

            // 패키지 네임 전달
            storeVersion = MarketVersionChecker.getMarketVersion(getPackageName());

            // 디바이스 버전 가져옴
            try {
                deviceVersion = getPackageManager().getPackageInfo(getPackageName(), 0).versionName;
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
            deviceVersionCheckHandler.sendMessage(deviceVersionCheckHandler.obtainMessage());
            // 핸들러로 메세지 전달
        }
    }
    private final DeviceVersionCheckHandler deviceVersionCheckHandler = new DeviceVersionCheckHandler(this);
    // 핸들러 객체 만들기
    private static class DeviceVersionCheckHandler extends Handler {
        private final WeakReference<atv_Roading> mainActivityWeakReference;
        public DeviceVersionCheckHandler(atv_Roading mainActivity) {
            mainActivityWeakReference = new WeakReference<atv_Roading>(mainActivity);
        }
        @Override
        public void handleMessage(Message msg) {
            atv_Roading activity = mainActivityWeakReference.get();
            if (activity != null) {
                activity.handleMessage();
                // 핸들메세지로 결과값 전달
            }
        }
    }


    private void handleMessage() {
        //핸들러에서 넘어온 값 체크

        Log.e("###버전확인",""+storeVersion);
        Log.e("###버전확인",""+deviceVersion);


        if (storeVersion.compareTo(deviceVersion) > 0) {
            // 업데이트 필요
        gotoUpdate = true;
            AlertDialog.Builder alertDialogBuilder =
                    new AlertDialog.Builder(new ContextThemeWrapper(this, android.R.style.Theme_DeviceDefault_Light));
            alertDialogBuilder.setTitle("업데이트");alertDialogBuilder
                    .setMessage("새로운 버전이 있습니다.\n보다 나은 사용을 위해 업데이트 해 주세요.")
                    .setPositiveButton("업데이트 바로가기", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 구글플레이 업데이트 링크
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=tw.com.beauty_parlor"));
                            startActivity(intent);

                        }
                    });
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.setCanceledOnTouchOutside(true);
            alertDialog.show();

        } else {
            // 업데이트 불필요

        }
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


                CUserInfo.id = sharedPreferences.getPreferences("id");
                CUserInfo.name = sharedPreferences.getPreferences("name");
                CUserInfo.sex = sharedPreferences.getPreferences("sex");

                Map<String, String> map = new HashMap<>();
                map.put("id",CUserInfo.id);

                JSONObject json = SC.Json_Messenger("users", SC.GET, map);

                try {

                    String resultCode = json.get("resultCode").toString();
                    Log.i("SC_GET MSG 1", "" + json.get("resultCode"));

                    if(resultCode.equals("404")){
                        return "0";
                    }
                    if(resultCode.equals("200")){
                        //로그인 성공. 정보 받기

                        if(json.get("penalty_count").toString().equals("out")) //불량사용자
                            return "2";

                        isMaster = json.get("isMaster").toString();
                        CUserInfo.isMaster = json.get("isMaster").toString();
                        CBeautyInfo.num = json.get("num").toString();
                        CBeautyInfo.title = json.get("title").toString();
                        CBeautyInfo.working_time_from = json.get("working_time_from").toString();
                        CBeautyInfo.working_time_to = json.get("working_time_to").toString();
                        CBeautyInfo.loc = json.get("loc").toString();
                        CBeautyInfo.booking_method = json.get("booking_method").toString();
                        CBeautyInfo.booking_delay_time = json.get("booking_delay_time").toString();
                        CBeautyInfo.booking_delay_time_for_cut = json.get("booking_delay_time_for_cut").toString();
                        CBeautyInfo.phone = json.get("phone").toString();
                        CBeautyInfo.dayOff = json.get("dayOff").toString();
                        CBeautyInfo.master_id = json.get("master_id").toString();
                        String strIsNotice = json.get("isNotice").toString();
                        if(strIsNotice.equals("1")){
                            CBeautyInfo.isNotice = true;
                            CBeautyInfo.notice = json.get("notice").toString();
                        }
                        else{
                            CBeautyInfo.isNotice = false;
                            CBeautyInfo.notice = json.get("notice").toString();
                        }


                        if (CBeautyInfo.num.equals("") || CBeautyInfo.num.equals(null)) {

                            return "3";
                        }

                        return "1";
                    }
                    else{
                        return "100";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }



                return "1";
            }
            return null;
        }



        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                //로그인 성공
                if(isMaster.equals("0")) { //일반 사용자
                    Intent intent2 = new Intent(atv_Roading.this, atv_main.class);
                    startActivity(intent2);
                }
                else if(isMaster.equals("1")) { //원장님
                    Intent intent2 = new Intent(atv_Roading.this, atv_master.class);
                    startActivity(intent2);
                }

                finish();// 이 화면 종료

            }
            else if(result.compareTo("0") == 0){
                //실패. 회원가입 페이지로
                Intent intent2 = new Intent(atv_Roading.this, atv_Join.class);
                startActivity(intent2);
                finish();// 이 화면 종료
            }
            else if(result.compareTo("3") == 0) {

                Alert alert = new Alert();
                alert.showAlert(context, "미용실을 설정해주세요.");
                Intent intent2 = new Intent(atv_Roading.this, atv_Seach_beauty.class);
                startActivity(intent2);
                finish();// 이 화면 종료

            }
            else if(result.compareTo("2") == 0){
                //계정 정지 상태 다이얼로그 뛰우고 앱 끄기
                //이건 아직 쓰지 않음
                Alert alert = new Alert();
                alert.showAlert(context, "당신은 불량 사용자로 지정되었습니다. \n앞으로 이 서비스를 이용하실 수 없습니다.");
            }
            else{
                //시스템 오류
                Alert alert = new Alert();
                alert.showAlert(context, "오류 발생. 다시 시도해주세요.");
                finish();// 이 화면 종료
            }
        }
    }

}
