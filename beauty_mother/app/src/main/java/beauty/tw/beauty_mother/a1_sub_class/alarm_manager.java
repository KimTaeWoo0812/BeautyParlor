package beauty.tw.beauty_mother.a1_sub_class;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.Calendar;

import beauty.tw.beauty_mother.a0_Google_FCM.Dialog_for_alarm;


/**
 * Created by kimtaewoo on 2017-01-31.
 */
public class alarm_manager {

    public void alarm_set(Context context){
        AlarmManager mAlarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Dialog_for_alarm.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // 알람이 발생할 정확한 시간을 지정
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.add(Calendar.SECOND, 10);
        // 반복 알람 시작
        mAlarmMgr.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), 1000 * 30, pIntent);
        Log.e("###알람", "시작");
    } // 알람 중지

    public void alarm_stop(Context context) {
        // 수행할 동작을 생성
        AlarmManager mAlarmMgr = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, Dialog_for_alarm.class);
        PendingIntent pIntent = PendingIntent.getActivity(context, 0, intent, 0);
        // 알람 중지
        mAlarmMgr.cancel(pIntent);
        Log.e("###알람", "중지");
    }
}
