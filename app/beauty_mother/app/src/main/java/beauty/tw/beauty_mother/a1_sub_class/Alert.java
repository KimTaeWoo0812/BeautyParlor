package beauty.tw.beauty_mother.a1_sub_class;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.Toast;


public class Alert
{
    public final static int CANCEL  = -1;
    public final static int BUTTON1 = 1;
    public final static int BUTTON2 = 2;
    public final static int BUTTON3 = 3;
    public final static int BUTTON4 = 4;
                                    
    protected static Toast mToast;
    protected static int    mToastTime;
                            
    private Dialog mDialog = null;
                                    
                                    
    public static void toastShort(Context context, String msg)
    {
        toast(context, msg, 1);
    }
    
    
    public static void toastShort(Context context, int msgRes)
    {
        String msg = context.getString(msgRes);
        toast(context, msg, 1);
    }
    
    
    public static void toastLong(Context context, String msg)
    {
        toast(context, msg, 2);
    }
    
    
    public static void toastLong(Context context, int msgRes)
    {
        String msg = context.getString(msgRes);
        toast(context, msg, 2);
    }
    
    
    private static void toast(Context context, String msg, int lengthType)
    {
        if (mToast == null)
        {
            switch (lengthType)
            {
                case 1:
                    mToastTime = Toast.LENGTH_SHORT;
                    break;
                    
                case 2:
                    mToastTime = Toast.LENGTH_LONG;
                    break;
            }
            mToast = Toast.makeText(context.getApplicationContext(), msg, mToastTime);
            mToast.show();
            return;
        }
        mToast.setText(msg);
        mToast.show();
    }
    
    
    /**
     * 기본 얼럿 (확인버튼)
     * 
     * @param message
     * @return
     */
    public Builder showAlert(Context context, String message)
    {
        return showAlert(context, null, message, false, "확인");
    }
    
    
    public Builder showAlert(Context context, String message, boolean cancelable)
    {
        return showAlert(context, null, message, cancelable, "확인");
    }
    
    
    /**
     * 일반 팝업 메시지만적용(버튼은 최대 두개까지)
     * 
     * @param message
     * @return
     */
    public Builder showAlert(Context context, String message, boolean cancelable, String... btns)
    {
        return showAlert(context, null, message, cancelable, btns);
    }
    
    
    /**
     * 일반 팝업 메시지,타이틀적용(버튼은 최대 두개까지)
     */
    public Builder showAlert(Context context, String title, String message, boolean cancelable, String... btns)
    {
        
        Builder builder = new Builder(context);
        builder.setCancelable(cancelable);
        builder.setTitle(title);
        builder.setMessage(message);
        switch (btns.length)
        {
            case 2:
                builder.setNegativeButton(btns[1], new OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (mListener != null) mListener.onClose(dialog, BUTTON2, null);
                    }
                });
            case 1:
                builder.setPositiveButton(btns[0], new OnClickListener()
                {
                    
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        if (mListener != null) mListener.onClose(dialog, BUTTON1, null);
                    }
                });
                break;
        }
        
        builder.setOnCancelListener(new DialogInterface.OnCancelListener()
        {
            
            @Override
            public void onCancel(DialogInterface dialog)
            {
                if (mListener != null) mListener.onClose(dialog, CANCEL, null);
            }
        });
        
        builder.show();
        return null;
        
    }
    
    
    protected OnCloseListener mListener = null;
    
    
    public void setOnCloseListener(OnCloseListener listener)
    {
        mListener = listener;
    }
    
    
    public interface OnCloseListener
    {
        public void onClose(DialogInterface dialog, int which, Object data);
    }
    
}