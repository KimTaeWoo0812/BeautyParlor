package beauty.tw.beauty_mother;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.Alert;
import beauty.tw.beauty_mother.a1_sub_class.InflateUtil;
import beauty.tw.beauty_mother.a5_Setting.atv_Setting;
import beauty.tw.beauty_mother.a5_Setting.atv_Setting_Master;


public abstract class AtvBase extends FragmentActivity
{
    
    protected LinearLayout mBaseTop         = null;
    protected RelativeLayout mBaseTopTitle    = null;
    protected TextView mTxtTopTitle     = null;
    private LinearLayout mBaseLinear      = null;
                                              
    protected int[]          mDeviceSize      = null;
                                              
    protected int            mMenuIndex       = 0;
    // protected DisplayImageOptions mImageOptions;
    
    private boolean          mIsCompletedInit = false;
    public ImageButton setting_btn;

                                              
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.atv_base);
        mBaseTop = (LinearLayout) findViewById(R.id.baseTop);
        mBaseTopTitle = (RelativeLayout) findViewById(R.id.baseTopTitle);
        mTxtTopTitle = (TextView) findViewById(R.id.txtTopTitle);
        mBaseLinear = (LinearLayout) findViewById(R.id.baseLinear);

        setting_btn = (ImageButton) findViewById(R.id.setting_btn);

        setting_btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent i = new Intent();


                if(CUserInfo.isMaster.equals("0")) { //일반 사용자
                    i.setClass(getContext(), atv_Setting.class);
                }
                else {
                    i.setClass(getContext(), atv_Setting_Master.class);
                }
                startActivity(i);
            }
        });


        getDisplaySize();
        
        setView();
        
        if (getBundle())
        {
            init();
        }
        else
        {
            getBundleFail();
        }

        Handler h = new Handler();
        h.postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                mIsCompletedInit = true;
            }
        }, 1000);
    }

    protected void getDisplaySize()
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        Display display = ((WindowManager) getSystemService(WINDOW_SERVICE)).getDefaultDisplay();
        display.getMetrics(displayMetrics);
        
        mDeviceSize = new int[2];
        mDeviceSize[0] = displayMetrics.widthPixels;
        mDeviceSize[1] = displayMetrics.heightPixels;
    }
    
    
    /**
     * 컨텐츠 화면 구성
     * 
     * @param layoutResId
     */
    protected void setView(int layoutResId)
    {
        
        if (layoutResId > 0)
        {
            {
                if (R.layout.atv_main == layoutResId) {
                    setting_btn.setVisibility(setting_btn.VISIBLE);
                    setting_btn.setBackgroundResource(R.drawable.s_btn_setting);
                }
                else if(R.layout.atv_master == layoutResId){
                   setting_btn.setVisibility(setting_btn.VISIBLE);
                    setting_btn.setBackgroundResource(R.drawable.s_btn_setting_for_master);
                }

                else {
                   setting_btn.setVisibility(setting_btn.GONE);
                }

            }

            View view = InflateUtil.inflate(this, layoutResId, null);
            
            if (view != null)
            {
                LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
                mBaseLinear.addView(view, layoutParams);
            }
        }
    }
    

    @SuppressLint("NewApi")
    protected void logout(Intent intent)
    {
        
        // SPUtil.getInstance().logOut(getContext());
        
        Intent newIntent = new Intent();
        
        // newIntent.setClass(getContext(), AtvLogin.class);
        newIntent.setAction("LOGOUT");
        if (intent != null)
        {
            if (intent.getExtras() != null) newIntent.putExtras(intent.getExtras());
        }
        newIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(newIntent);
        // finish();
        finishAffinity();
    }
    
    
    protected abstract void setView();
    
    
    /**
     * 리소스 찾기
     */
    // protected abstract void findView();
    
    protected boolean getBundle()
    {
        return true;
    }
    
    
    protected void getBundleFail()
    {
        Alert.toastShort(this, "다시 시도해 주세요.");
        finish();
    }
    
    
    /**
     * 화면 초기화
     */
    protected abstract void init();
    
    
    protected boolean isCompletedInit()
    {
        return mIsCompletedInit;
    }
    
    
    /**
     * 각종 이벤트 리스너 추가
     */
    // protected abstract void configureListener();
    
    protected Context getContext()
    {
        return this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
    }
    
    
    private boolean  IS_READY_KILL       = false;
    private Toast mFinishStopToast    = null;
    private Handler mFinishStopHandler  = new Handler();
    private Runnable mFinishStopRunnable = new Runnable()
                                         {
                                             
                                             @Override
                                             public void run()
                                             {
                                                 IS_READY_KILL = false;
                                             }
                                         };
                                         
                                         
    protected void onSuperBackPressed()
    {
        if (IS_READY_KILL)
        {
            if (mFinishStopToast != null) mFinishStopToast.cancel();
            mFinishStopHandler.removeCallbacks(mFinishStopRunnable);
            IS_READY_KILL = false;
            finish();
        }
        else
        {
            IS_READY_KILL = true;
            mFinishStopHandler.removeCallbacks(mFinishStopRunnable);
            mFinishStopHandler.postDelayed(mFinishStopRunnable, 2000);
            
            if (mFinishStopToast == null)
            {
                mFinishStopToast = Toast.makeText(this, "'뒤로' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
            }
            
            mFinishStopToast.show();
        }
    }
}
