package beauty.tw.beauty_mother.a0_Google_FCM;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.logging.Handler;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a3_mainpage.atv_main;
import beauty.tw.beauty_mother.a6_master_Package.atv_master;


/**
 * Created by kimtaewoo on 2017-01-23.
 */
public class Dialog_Notification extends Activity {

    TextView txtMsg;
    TextView txtTitle;
    Button btnOk;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notification);

        // 화면이 잠겨있을 때 보여주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        btnOk = (Button) findViewById(R.id.btnOk);

        txtTitle.setText(MyFirebaseMessagingService.title);
        txtMsg.setText(MyFirebaseMessagingService.message);

        Log.e("###test노티",""+MyFirebaseMessagingService.isMaster);
        //알람 등록
        if(MyFirebaseMessagingService.isMaster.equals("1")){
            Log.e("###test노티2", "" + MyFirebaseMessagingService.isMaster);
            alarm_set();
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                PackageManager pm = getPackageManager();
//                pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), atv_main.class),
//                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED , 0);
                if(MyFirebaseMessagingService.isMaster.equals("1")){
//                    Intent intent = new Intent(Dialog_Notification.this, atv_master.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    startActivity(intent);
                    alarm_stop();
                }
                else{
                    Intent intent = new Intent(Dialog_Notification.this, atv_main.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                finish();
            }
        });
//        Log.e("### message",MyFirebaseMessagingService.message);


        //화면 끄기
        android.os.Handler hd = new android.os.Handler();
        hd.postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 5000);

    }

    protected final int alarm_delay_time = 1000 * 60 * 50; // 50분
    public void alarm_set(){

        AlarmManager mAlarmMgr = (AlarmManager)Dialog_Notification.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Dialog_Notification.this, Dialog_for_alarm.class);
        PendingIntent pIntent = PendingIntent.getActivity(Dialog_Notification.this, 0, intent, 0);
        // 알람이 발생할 정확한 시간을 지정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.MINUTE, 50);
        // 반복 알람 시작
        mAlarmMgr.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarm_delay_time, pIntent);
        Log.e("###알람", "시작");
    }
    public void alarm_stop() {
        // 수행할 동작을 생성
        AlarmManager mAlarmMgr = (AlarmManager)Dialog_Notification.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(Dialog_Notification.this, Dialog_for_alarm.class);
        PendingIntent pIntent = PendingIntent.getActivity(Dialog_Notification.this, 0, intent, 0);
        // 알람 중지
        mAlarmMgr.cancel(pIntent);
        Log.e("###알람", "중지");
    }

}

