package beauty.tw.beauty_mother.a0_Google_FCM;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.io.IOException;
import java.util.logging.Handler;

import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a6_master_Package.atv_master;


/**
 * Created by kimtaewoo on 2017-01-23.
 */
public class Dialog_for_alarm extends Activity {

    TextView txtMsg;
    TextView txtTitle;
    Button btnOk;

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_notification);

        Log.e("### Dialog_for_alarm", "");
        // 화면이 잠겨있을 때 보여주기
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED
                // 키잠금 해제하기
                | WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD
                // 화면 켜기
                | WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);


        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtMsg = (TextView) findViewById(R.id.txtMsg);
        btnOk = (Button) findViewById(R.id.btnOk);

        txtTitle.setText("예약 알림");
        txtMsg.setText("새로운 예약이 있습니다");

        AudioManager mAudioManager = (AudioManager) Dialog_for_alarm.this.getSystemService(Context.AUDIO_SERVICE);


        //진동모드
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_VIBRATE){
            Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(1000);
        }
        //벨 모드
        if(mAudioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL){
            Uri alert = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            final MediaPlayer player = new MediaPlayer();
            try {
                player.setDataSource(this, alert);
            } catch (IllegalArgumentException e1) {
                e1.printStackTrace();
            } catch (SecurityException e1) {
                e1.printStackTrace();
            } catch (IllegalStateException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            final AudioManager audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
            if (audioManager.getStreamVolume(AudioManager.STREAM_ALARM) != 0) {
                player.setAudioStreamType(AudioManager.STREAM_ALARM);
                player.setLooping(true);
                try {
                    player.prepare();
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                player.start();
            }
            android.os.Handler hd2 = new android.os.Handler();
            hd2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    if(player.isPlaying()){
                        player.stop();
                        player.release();
                    }
                }
            }, 1250);

        }



        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


//                PackageManager pm = getPackageManager();
//                pm.setComponentEnabledSetting(new ComponentName(getApplicationContext(), atv_main.class),
//                        PackageManager.COMPONENT_ENABLED_STATE_ENABLED , 0);

                Intent intent = new Intent(Dialog_for_alarm.this, atv_master.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);

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

}

