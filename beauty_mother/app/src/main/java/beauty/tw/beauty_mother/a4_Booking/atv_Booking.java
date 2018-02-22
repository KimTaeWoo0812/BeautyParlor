package beauty.tw.beauty_mother.a4_Booking;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.SC;

/**
 * A simple {@link Fragment} subclass.
 */
public class atv_Booking extends AtvBase {
    private View heroImageView;

    public static String select_day;

    public GregorianCalendar month, itemmonth;// new_log_calendar instances.

    public adapter_Calendar adapter;// adapter instance
    // marker.
    public ArrayList<String> items; // container to store new_log_calendar items which

    private GregorianCalendar mCalendar; // 설정 일시

    private TimePicker mTime; //시작 설정 클래스
    // needs showing the event marker

    ListView list;
    TextView title;

    //전송할 데이터
    String hour, minute;
    int working_time_from_hour;
    int working_time_from_minute;
    int working_time_to_hour;
    int working_time_to_minute;

    String checkStyles="";
    String memo = "";
    boolean isCut = false;
    String today="";


    private void SetBeautyWorkingTime(){
        String t[] = CBeautyInfo.working_time_from.split(":");
        working_time_from_hour= Integer.valueOf(t[0]);
        working_time_from_minute= Integer.valueOf(t[1]);

        String t2[] = CBeautyInfo.working_time_to.split(":");
        working_time_to_hour= Integer.valueOf(t2[0]);
        working_time_to_minute= Integer.valueOf(t2[1]);
    }
    private boolean Check_workingTime(int  h, int  m){

//        Log.e("testtesttest h ",""+h);
//        Log.e("testtesttest  m",""+m);
//        Log.e("testtesttest  m",""+working_time_from_hour);
//        Log.e("testtesttest  m",""+working_time_from_minute);
//        Log.e("testtesttest  m",""+working_time_to_hour);
//        Log.e("testtesttest  m",""+working_time_to_minute);

        if(h<working_time_from_hour || (h == working_time_from_hour && m <= working_time_from_minute)) {
//            Log.e("testtesttest h ","1");
            return false;
        }
        if(h>working_time_to_hour || (h == working_time_to_hour && m >= working_time_to_minute)) {
//            Log.e("testtesttest h ","2");
            return false;
        }
        return true;
    }


    public atv_Booking() {
        // Required empty public constructor
    }


    @Override
    protected void setView()
    {
        setView(R.layout.atv_caldroider);
    }


    private String getH() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("HH");
        return f.format(new Date(time));
    }
    private String getM() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("mm");
        return f.format(new Date(time));
    }
    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new Date(time));
    }
    private String getDate_time() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        return f.format(new Date(time));
    }
    private String getMonth_plus_one() {

        String today = null;
        // 포맷변경 ( 년월일 시분초)
        SimpleDateFormat sdformat = new SimpleDateFormat("yyyy-MM-dd");
        // Java 시간 더하기
        Calendar cal = Calendar.getInstance();

        // 1시간 후
        cal.add(Calendar.DAY_OF_MONTH, 14);

        today = sdformat.format(cal.getTime());
        System.out.println("1시간 후 : " + today);

        return today;
    }

    @Override
    protected void init()
    {
        SetBeautyWorkingTime();
        select_day = getDate();
        mTxtTopTitle.setText("예약하기");

        today = getDate();

        //Fragment통합 시작 - 리스트뷰 부
        /* Initialise list view, hero image, and sticky view */
        heroImageView = findViewById(R.id.heroImageView);

        /* Inflate list header layout */
        LayoutInflater inflater2 = (LayoutInflater) atv_Booking.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View listHeader = inflater2.inflate(R.layout.list_header, null);
        heroImageView = listHeader.findViewById(R.id.heroImageView);

        LinearLayout t =(LinearLayout) findViewById(R.id.calendar_view);
        t.addView(listHeader);



        //time picker 셋팅
        mCalendar = new GregorianCalendar();

        Log.i("HelloAlarmActivity", mCalendar.getTime().toString());


        //일시 설정 클래스로 현재 시각을 설정
        mTime = (TimePicker) findViewById(R.id.time_picker);
        mTime.setCurrentHour(mCalendar.get(Calendar.HOUR_OF_DAY));
        mTime.setCurrentMinute(mCalendar.get(Calendar.MINUTE));

        hour = Integer.toString(mCalendar.get(Calendar.HOUR_OF_DAY));
        minute = Integer.toString(mCalendar.get(Calendar.MINUTE));

        mTime.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute_) {
                hour = Integer.toString(hourOfDay);
                minute = Integer.toString(minute_);
//                Toast.makeText(atv_Booking.this, Integer.toString(hourOfDay) + "시 " + Integer.toString(minute_), Toast.LENGTH_SHORT).show();
            }
        });

        Button sendBtn = (Button)findViewById(R.id.btnSend);
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.e("test##", select_day + "  " + getDate() + "   " + hour + "   " + minute);
//                Log.e("test##", ""+(month.get(GregorianCalendar.MONTH)));
//                Log.e("test##", ""+(month.get(GregorianCalendar.DAY_OF_MONTH)));
//                Log.e("test## getMonth_plus", "" + getMonth_plus_one());

                if(!Check_workingTime(Integer.valueOf(hour), Integer.valueOf(minute))){
                    Toast.makeText(atv_Booking.this, CBeautyInfo.title+"의 근무시간은 "+CBeautyInfo.working_time_from+"~"+CBeautyInfo.working_time_to+"까지 입니다. "+hour+"시"+minute+"분을 선택하셨습니다.\n 다른 시간을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(today.equals(select_day)){
                    if(hour.compareTo(getH()) < 0 || (getH().compareTo(hour) == 0 && minute.compareTo(getM()) < 0)){
                        Toast.makeText(atv_Booking.this, "지난 시간 입니다. 다른 시간을 선택해주세요", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    String tempH = Integer.toString(Integer.valueOf(getH()) + 1);

                    if(hour.compareTo(tempH) < 0 || (tempH.compareTo(hour) == 0 && minute.compareTo(getM()) < 0)){
                        Toast.makeText(atv_Booking.this, "현재 시간으로부터 1시간 이후로 예약하실수 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                }



                DialogSimple();
            }
        });




        // Inflate the layout for this fragment
        Locale.setDefault(Locale.US);

        month = (GregorianCalendar) GregorianCalendar.getInstance();
        itemmonth = (GregorianCalendar) month.clone();

        items = new ArrayList<String>();

        adapter = new adapter_Calendar(atv_Booking.this, month);

        GridView gridview = (GridView) listHeader.findViewById(R.id.gridview);
        gridview.setAdapter(adapter);


        title = (TextView) listHeader.findViewById(R.id.title);
        title.setText(android.text.format.DateFormat.format("yyyy-MM", month));

        RelativeLayout previous = (RelativeLayout) listHeader.findViewById(R.id.previous);
        //DB테스트
        list = (ListView) atv_Booking.this.findViewById(R.id.listView);

        //달 이동 버튼
        previous.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("##cal  달 이동 버튼", "#111 ");
                setPreviousMonth();
                refreshCalendar();
            }
        });

        RelativeLayout next = (RelativeLayout) listHeader.findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.e("##cal  달 이동 버튼", "#222 ");
                setNextMonth();
                refreshCalendar();

            }
        });

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                // removing the previous view if added

                if(getDate().compareTo(adapter_Calendar.dayString.get(position)) > 0){
                    Toast.makeText(atv_Booking.this, "지난 날짜입니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(getMonth_plus_one().compareTo(adapter_Calendar.dayString.get(position)) == -1){
                    Toast.makeText(atv_Booking.this, "2주 이내에만 예약하실수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                ((adapter_Calendar) parent.getAdapter()).setSelected(v);

                Log.i("##cal  onItemClick", "# " + position);
                Log.i("##cal  onItemClick", "# " + adapter_Calendar.dayString.get(position));

                select_day = adapter_Calendar.dayString.get(position);

                String selectedGridDate = adapter_Calendar.dayString
                        .get(position);


                String[] separatedTime = selectedGridDate.split("-");
                String gridvalueString = separatedTime[2].replaceFirst("^0*",
                        "");// taking last part of date. ie; 2 from 2012-12-02.
                int gridvalue = Integer.parseInt(gridvalueString);
                // navigate to next or previous month on clicking offdays.


                refreshCalendar();

                if ((gridvalue > 10) && (position < 8)) {
                    setPreviousMonth();
                    refreshCalendar();
                } else if ((gridvalue < 7) && (position > 28)) {
                    setNextMonth();
                    refreshCalendar();
                }
                //((Reallog_CalendarAdapter) parent.getAdapter()).setSelected(v);


            }

        });


    }
    private void Dialog_reOk()
    {
        String clause = "예약시간: "+hour+":"+minute+"\n예약 후 통보 없이 가지 않으시면 앱 사용을 제한합니다.\n기다리는 사람들을 위해 약속을 꼭 지켜주세요^^";

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(clause).setCancelable(false).setPositiveButton("취소", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id_)
            {
                // do something
                dialog.cancel();
            }
        }).setNegativeButton("확인", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id_) {
                new SendMsgTask().execute("1");

                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("너와 나의 promise");
        // Title for AlertDialog
        // Icon for AlertDialog
        alert.show();
        TextView msgView = (TextView) alert.findViewById(android.R.id.message);
        msgView.setTextSize(16);
    }

    private void DialogSimple(){
        final Dialog ratingDialog = new Dialog(atv_Booking.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_booking);
        ratingDialog.setCancelable(true);

        final EditText editCom = (EditText) ratingDialog.findViewById(R.id.Text1);

        Button button7 = (Button) ratingDialog.findViewById(R.id.button7); // 전송
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String commentText = editCom.getText().toString();

                // databases.add(new CommentDatabase("0", commentText, CUserInfo.id,
                // CUserInfo.name, stringRating, "0", "0"));

                Log.i("###", "###" + CUserInfo.name);

                if (commentText.length() == 0) {
                    commentText = " ";
                }

                // JSON 형식에 맞게 " 없앤다.
                memo = commentText.replace("\"", " ");

                CheckBox box_cut = (CheckBox) ratingDialog.findViewById(R.id.checkBox_cut);
                CheckBox box_perm = (CheckBox) ratingDialog.findViewById(R.id.checkBox_perm);
                CheckBox box_dyeing = (CheckBox) ratingDialog.findViewById(R.id.checkBox_dyeing);
                CheckBox box_else = (CheckBox) ratingDialog.findViewById(R.id.checkBox_else);

                checkStyles = "";
                {
                    boolean isChecked = false;
                    if (box_cut.isChecked()) {
                        checkStyles += getString(R.string.checkBox_cut) + " ";
                        isCut = true;
                        isChecked = true;
                    }
                    if (box_perm.isChecked()) {
                        checkStyles += getString(R.string.checkBox_perm) + " ";
                        isCut = false;
                        isChecked = true;
                    }
                    if (box_dyeing.isChecked()) {
                        checkStyles += getString(R.string.checkBox_dyeing) + " ";
                        isCut = false;
                        isChecked = true;
                    }
                    if (box_else.isChecked()) {
                        checkStyles += getString(R.string.checkBox_else) + " ";
                        isCut = false;
                        isChecked = true;
                    }
                    if (!isChecked)
                        checkStyles += getString(R.string.checkBox_else) + " ";
                }


                Dialog_reOk();
                ratingDialog.dismiss();
            }
        });

        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();


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

                int temp;
                {
                    if (isCut) {
                        temp = Integer.valueOf(minute) + Integer.valueOf(CBeautyInfo.booking_delay_time_for_cut);
                    } else {
                        temp = Integer.valueOf(minute) + Integer.valueOf(CBeautyInfo.booking_delay_time);
                    }
                }
                String endTime_minute;
                String endTime_hour = hour;
                {
                    if (temp >= 60) {
                        endTime_minute = Integer.toString(temp - 60);
                        endTime_hour = Integer.toString(Integer.valueOf(endTime_hour) + 1);
                    } else {
                        endTime_minute = Integer.toString(temp);
                    }
                }
                Map<String, String> map = new HashMap<>();
                map.put("beauty_num", CBeautyInfo.num);
                map.put("beauty_title", CBeautyInfo.title);
                map.put("user_id", CUserInfo.id);
                map.put("user_name", CUserInfo.name);
                map.put("user_sex", CUserInfo.sex);
                map.put("booking_date", select_day);
                map.put("booking_time", hour+":"+minute);
                map.put("end_time", endTime_hour+":"+endTime_minute);
                map.put("create_date", getDate_time());
                map.put("memo", memo);
                map.put("hair_style", checkStyles);
                map.put("booking_method", CBeautyInfo.booking_method);

                JSONObject json = SC.Json_Messenger("booking", SC.POST, map);

                try {
                    String resultCode = json.get("resultCode").toString();
                    String value = json.get("value").toString();

                    if(!resultCode.equals("200"))
                        return "0";

                    if(value.equals("1")) //예약 성공
                        return "1";
                    if(value.equals("2")) //중복
                        return "2";
                    if(value.equals("3")) //이 아이디로 다른 예약한 것이 있다
                        return "3";


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return "0";
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                Toast.makeText(atv_Booking.this, "예약 성공!", Toast.LENGTH_SHORT).show();
                finish();
            }
            else if (result.compareTo("2") == 0) {
                Toast.makeText(atv_Booking.this, "이미 예약이 있는 시간입니다. 다른 시간을 선택해 주세요.", Toast.LENGTH_SHORT).show();
            }
            else if (result.compareTo("3") == 0) {
                Toast.makeText(atv_Booking.this, "예약 내역이 있습니다. 두개 이상 예약할 수 없습니다", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(atv_Booking.this, "예약에 실패했습니다. 잠시 후 다시 시도해주세요. 이 문제가 지속될 경우 앱을 재시작 해보세요.", Toast.LENGTH_SHORT).show();

            }
        }
    }


    protected void setNextMonth() {
        Log.e("##cal  setNextMonth", "# ");
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMaximum(GregorianCalendar.MONTH)) {
            Log.e("##cal  setNextMonth", "#11111 ");
            month.set((month.get(GregorianCalendar.YEAR) + 1),
                    month.getActualMinimum(GregorianCalendar.MONTH), 1);
        } else {
            Log.e("##cal  setNextMonth", "#22222 ");
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) + 1);
        }

    }

    protected void setPreviousMonth() {
        Log.e("##cal  setPreviousMonth", "# ");
        if (month.get(GregorianCalendar.MONTH) == month
                .getActualMinimum(GregorianCalendar.MONTH)) {
            Log.e("##cal  setPreviousMonth", "#11111 ");
            month.set((month.get(GregorianCalendar.YEAR) - 1),
                    month.getActualMaximum(GregorianCalendar.MONTH), 1);
        } else {
            Log.e("##cal  setPreviousMonth", "#22222 ");
            month.set(GregorianCalendar.MONTH,
                    month.get(GregorianCalendar.MONTH) - 1);
        }

    }

    protected void showToast(String string) {
        Toast.makeText(atv_Booking.this, string, Toast.LENGTH_SHORT).show();

    }

    public void refreshCalendar() {

        adapter.refreshDays();
        adapter.notifyDataSetChanged();

        Log.e("##cal  refreshCalendar", "# " + android.text.format.DateFormat.format("yyyy MM", month));
        title.setText(android.text.format.DateFormat.format("yyyy-MM", month));
    }

}
