package beauty.tw.beauty_mother.a1_sub_class;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.widget.Toast;


public class serverCheck
{
    private static Handler mHandler;
    private static Runnable mRunnable;
    // private static ProgressDialog m_loadingDialog = null;
    private static Dialog dialog         = null;
                                                  
    public volatile static boolean canNotGet      = false;
    static Context baseContext;
                                   
    public static boolean          flag_forServer = false;
                                                  
    static long                    start;
    static long                    end;
    static final int               DELAYED_TIME   = 8000;
                                                  
                                                  
    public static void showLoading(final Context context)
    {
        
        try
        {
            baseContext = context;
            dialog = ProgressDialogUtil.show(context, null);
            flag_forServer = true;
            
            currentActivity(context);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    
    
    /**
     * 다이얼로그 없애기
     */
    public static void hideLoading()
    {
        
        if (dialog != null)
        {
            dialog.dismiss();
            dialog = null;
        }
    }
    
    
    /**
     * 다이얼로그 실행
     * 
     * @param mContext
     */
    public static void currentActivity(final Context mContext)
    {
        
        start = System.currentTimeMillis();
        end = System.currentTimeMillis();
        
        mRunnable = new Runnable()
        {
            @Override
            public void run()
            {
                end = System.currentTimeMillis();
                if (end > start + DELAYED_TIME)
                {
                    if (dialog != null)
                    {
                        if (flag_forServer)
                        {
                            Toast toast = Toast.makeText(mContext, "서버와 연결이 원활하지 않습니다.", Toast.LENGTH_SHORT);
                            toast.show();
                            canNotGet = true;
                            dialog.dismiss();
                            dialog = null;
                        }
                    }
                }
            }
        };
        
        mHandler = new Handler();
        mHandler.postDelayed(mRunnable, DELAYED_TIME);
    }
}