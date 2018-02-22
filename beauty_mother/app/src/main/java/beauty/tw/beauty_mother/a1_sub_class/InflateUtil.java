package beauty.tw.beauty_mother.a1_sub_class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class InflateUtil {

	public static View inflate(Context context, int layoutResId, ViewGroup root)
	{
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		return inflater.inflate(layoutResId, root) ;
	}
	
}
