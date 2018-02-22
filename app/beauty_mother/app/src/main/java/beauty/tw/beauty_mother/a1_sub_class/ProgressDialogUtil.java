package beauty.tw.beauty_mother.a1_sub_class;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnDismissListener;
import android.graphics.Color;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


public class ProgressDialogUtil
{
    
    public static Dialog show(Context context, Dialog dialog)
    {
        return show(context, dialog, null, null);
    }
    
    
    public static Dialog show(Context context, Dialog dialog, OnDismissListener dismissListener)
    {
        return show(context, dialog, dismissListener, null);
    }
    
    
    public static Dialog show(Context context, Dialog dialog, OnCancelListener cancelListener)
    {
        return show(context, dialog, null, cancelListener);
    }
    
    
    public static Dialog show(Context context, Dialog dialog, OnDismissListener dismissListener, OnCancelListener cancelListener)
    {
        try
        {
            if (dialog != null)
            {
                if (!dialog.isShowing())
                {
                    dialog.show();
                }
                return dialog;
            }
            
            dialog = new Dialog(context, android.R.style.Theme_Translucent_NoTitleBar);
            
            LinearLayout layout = new LinearLayout(context);
            
            ProgressBar progressBar = new ProgressBar(context);
            
            layout.setOrientation(LinearLayout.VERTICAL);
            layout.setGravity(Gravity.CENTER);
            layout.setBackgroundColor(0x00000000); // 백그라운드색
            layout.setBackgroundColor(0xc0000000);
            layout.addView(progressBar, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            
            TextView txt = new TextView(context);
            txt.setText("데이터 수신중입니다.");
            txt.setTextSize(16);
            txt.setTextColor(Color.parseColor("#ffd5d5d5"));
            //txt.setTextColor(R.drawable.color_black);
            
            layout.addView(txt, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            
            dialog.addContentView(layout, new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            
            dialog.setCancelable(true);
            
            if (cancelListener != null) dialog.setOnCancelListener(cancelListener);
            
            if (dismissListener != null) dialog.setOnDismissListener(dismissListener);
            
            dialog.show();
            
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
            
        }
        
        return dialog;
    }
    
    
    public static void dismiss(Dialog dialog)
    {
        try
        {
            if (dialog != null && dialog.isShowing()) dialog.dismiss();
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
