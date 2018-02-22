/*
 * Copyright (C) 2014 Mukesh Y authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package beauty.tw.beauty_mother.a4_Booking;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import beauty.tw.beauty_mother.R;


/**
 * @author Mukesh Y
 */
public class adapter_Calendar extends BaseAdapter {
	private Context mContext;

	private java.util.Calendar month;
	public GregorianCalendar pmonth; // new_log_calendar instance for previous month
	/**
	 * new_log_calendar instance for previous month for getting complete view
	 */
	public GregorianCalendar pmonthmaxset;
	private GregorianCalendar selectedDate;
	int firstDay;
	int maxWeeknumber;
	int maxP;
	int calMaxP;
	int lastWeekDay;
	int leftDays;
	int mnthlength;
	String itemvalue, curentDateString;
	DateFormat df;

	private ArrayList<String> items;
	public static List<String> dayString;
	private View previousView;
	//DB테스트
	private static final String TAG_NAME = "name";
	private static final String TAG_PHONE = "phone";
	private final String dbName = "yellowbus";
	private final String tableName = "log";
	private final String[] date = new String[]{"2016-05-20", "2016-05-20", "2016-05-21"};
	private final String[] academy = new String[]{"동은학원", "삼성학원", "LG학원"};
	ArrayList<HashMap<String, String>> personList;
	ListView list;
	SQLiteDatabase sampleDB = null;
	ListAdapter adapter;
	//DB테스트끝

	public adapter_Calendar(Context c, GregorianCalendar monthCalendar) {
		adapter_Calendar.dayString = new ArrayList<String>();
		Locale.setDefault(Locale.US);
		month = monthCalendar;
		selectedDate = (GregorianCalendar) monthCalendar.clone();
		mContext = c;

		month.set(GregorianCalendar.DAY_OF_MONTH, 1);
		this.items = new ArrayList<String>();
		df = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
		curentDateString = df.format(selectedDate.getTime());
		refreshDays();
	}

	public void setItems(ArrayList<String> items) {
		for (int i = 0; i != items.size(); i++) {
			if (items.get(i).length() == 1) {
				items.set(i, "0" + items.get(i));
			}
		}
		this.items = items;
	}

	public int getCount() {
		return dayString.size();
	}

	public Object getItem(int position) {
		return dayString.get(position);
	}

	public long getItemId(int position) {
		return 0;
	}

	// create a new view for each item referenced by the Adapter
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView dayView;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater vi = (LayoutInflater) mContext
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.new_log_calendar_item, null);

		}
		dayView = (TextView) v.findViewById(R.id.date);
		// separates daystring into parts.
		String[] separatedTime = dayString.get(position).split("-");
		// taking last part of date. ie; 2 from 2012-12-02
		String gridvalue = separatedTime[2].replaceFirst("^0*", "");
		// checking whether the day is in current month or not.
		if ((Integer.parseInt(gridvalue) > 1) && (position < firstDay)) {
			// setting offdays to white color.
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else if ((Integer.parseInt(gridvalue) < 7) && (position > 28)) {
			dayView.setTextColor(Color.GRAY);
			dayView.setClickable(false);
			dayView.setFocusable(false);
		} else {
			// setting curent month's days in blue color.
			dayView.setTextColor(Color.BLUE);
		}
		{
//			if (dayString.get(position).equals(curentDateString)) {
			if(dayString.get(position).equals(atv_Booking.select_day)){
				setToday(v);
			}
			else{
				set_Not_Today(v);
			}
		}

		dayView.setText(gridvalue);

		// create date string for comparison
//		String date = dayString.get(position);

//		if (date.length() == 1) {
//			date = "0" + date;
//		}
//		String monthStr = "" + (month.get(GregorianCalendar.MONTH) + 1);
//		if (monthStr.length() == 1) {
//			monthStr = "0" + monthStr;
//		}
//
//		// show icon if date is not empty and it exists in the items array
//		ImageView iw = (ImageView) v.findViewById(R.id.date_icon);
//		if (date.length() > 0 && items != null && items.contains(date)) {
//			iw.setVisibility(View.VISIBLE);
//		} else {
//			iw.setVisibility(View.INVISIBLE);
//		}
		return v;
	}
	public void setToday(View view){

		view.setBackgroundResource(R.drawable.dot);
//		view.setBackgroundColor(Color.argb(255, 255, 99, 71));
	}
	public void set_Not_Today(View view){
		view.setBackgroundColor(Color.argb(255, 255, 255, 255));
//		view.setBackgroundResource(R.drawable.calendar_cell);
	}

	public View setSelected(View view) {
		if (previousView != null) {
			Log.i("##cal setSelected null", "# ");
		}
		previousView = view;
		return view;
	}

	public void refreshDays() {
		// clear items
		items.clear();
		dayString.clear();
		Locale.setDefault(Locale.US);
		pmonth = (GregorianCalendar) month.clone();
		// month start day. ie; sun, mon, etc
		firstDay = month.get(GregorianCalendar.DAY_OF_WEEK);
		// finding number of weeks in current month.
		maxWeeknumber = month.getActualMaximum(GregorianCalendar.WEEK_OF_MONTH);
		// allocating maximum row number for the gridview.
		mnthlength = maxWeeknumber * 7;
		maxP = getMaxP(); // previous month maximum day 31,30....
		calMaxP = maxP - (firstDay - 1);// new_log_calendar offday starting 24,25 ...
		/**
		 * Calendar instance for getting a complete gridview including the three
		 * month's (previous,current,next) dates.
		 */
		pmonthmaxset = (GregorianCalendar) pmonth.clone();
		/**
		 * setting the start date as previous month's required date.
		 */
		pmonthmaxset.set(GregorianCalendar.DAY_OF_MONTH, calMaxP + 1);

		/**
		 * filling new_log_calendar gridview.
		 */
		for (int n = 0; n < mnthlength; n++) {

			itemvalue = df.format(pmonthmaxset.getTime());
			pmonthmaxset.add(GregorianCalendar.DATE, 1);
//			Log.i("##cal  dayString", "# " + itemvalue);
			dayString.add(itemvalue);

		}
	}

	private int getMaxP() {
		int maxP;
		if (month.get(GregorianCalendar.MONTH) == month
				.getActualMinimum(GregorianCalendar.MONTH)) {
			pmonth.set((month.get(GregorianCalendar.YEAR) - 1),
					month.getActualMaximum(GregorianCalendar.MONTH), 1);
		} else {
			pmonth.set(GregorianCalendar.MONTH,
					month.get(GregorianCalendar.MONTH) - 1);
		}
		maxP = pmonth.getActualMaximum(GregorianCalendar.DAY_OF_MONTH);

		return maxP;
	}

}