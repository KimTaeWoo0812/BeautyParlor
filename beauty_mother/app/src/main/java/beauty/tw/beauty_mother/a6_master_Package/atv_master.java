package beauty.tw.beauty_mother.a6_master_Package;

import android.Manifest;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a0_Google_FCM.Dialog_for_alarm;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.Alert;
import beauty.tw.beauty_mother.a1_sub_class.SC;


/**
 * Created by kimtaewoo on 2017-01-19.
 */
public class atv_master extends AtvBase {

    Context mContext = this;
    private ListView listView;
    private boolean btn_Case = true;
    private ArrayList<adapter_master_bookingList> adapter_waitting_permission = new ArrayList<adapter_master_bookingList>();
    private ArrayList<adapter_master_bookingList> adapter_booking_list = new ArrayList<adapter_master_bookingList>();
    String strSeach;

    Button btn1;
    Button btn2;
    Button btn_choose;
    ImageView img1;
    ImageView img2;
    LinearLayout baseSeach;
    EditText txtSeach;
    ImageButton btnSearch;
    int booking_position;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public void alarm_stop() {
        // 수행할 동작을 생성
        AlarmManager mAlarmMgr = (AlarmManager)atv_master.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(atv_master.this, Dialog_for_alarm.class);
        PendingIntent pIntent = PendingIntent.getActivity(atv_master.this, 0, intent, 0);
        // 알람 중지
        mAlarmMgr.cancel(pIntent);
        Log.e("###알람", "중지");
    }
    @Override
    protected void onResume() {
        super.onResume();

        //알람 제거
        if(CUserInfo.isMaster.equals("1")){
            alarm_stop();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void setView() {
        setView(R.layout.atv_master);
    }

    @Override
    protected void init() {
        mTxtTopTitle.setText(CBeautyInfo.title);


        btn1 = (Button) findViewById(R.id.Btn1);
        btn2 = (Button) findViewById(R.id.Btn2);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        listView = (ListView) findViewById(R.id.listView);


        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로고침 코드
                if (btn_Case) {
                    new SendMsgTask().execute("1"); // 내 사용 내역 셋팅
                } else
                    new SendMsgTask().execute("4"); // 내 사용 내역 셋팅

                mSwipeRefreshLayout.setRefreshing(false); // 새로고침 완료

            }
        });

//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                Log.e("#####11111 ", "setOnItemClickListener" + position);
//                if (btn_Case) { // 예약 신청 목록
//                    Log.e("#####11111 ", "" + position);
//                    booking_position = position;
//                    DialogSimple();
//                } else { // 예약된 것들 목록
//                    Log.e("#####222222 ", "" + position);
//                    booking_position = position;
//                    Log.e("#####booking_position ", "" + position);
//                    Log.e("#####booking_position ", "" + booking_position);
//                    DialogSimple_forMaster();
//
//
//                }
//
//            }
//        });
        new SendMsgTask().execute("1"); // 내 사용 내역 셋팅

    }


    private void Dialog_reCheck()
    {
        String clause = "정말 예약을 취소하시겠습니까?";

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
                new SendMsgTask().execute("3_2");

                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.setTitle("예약 취소");
        // Title for AlertDialog
        // Icon for AlertDialog
        alert.show();
        TextView msgView = (TextView) alert.findViewById(android.R.id.message);
        msgView.setTextSize(16);
    }
    private void DialogSimple_forMaster() {
        final Dialog ratingDialog = new Dialog(atv_master.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_nomal);
        ratingDialog.setCancelable(true);

        TextView txtTitle = (TextView) ratingDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = (TextView) ratingDialog.findViewById(R.id.txtMsg);

        txtTitle.setText("예약 관리");
        txtMsg.setText("예약 거부, 혹은 예약한 손님이 오지 않았을 경우  오른쪽 버튼을 누르면 해당 손님의 앱 사용을 제한합니다.");

        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk);
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dialog_reCheck();
                ratingDialog.dismiss();
            }
        });
        button7.setText("예약 취소");

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SendMsgTask().execute("5");

                ratingDialog.dismiss();
            }
        });
        button.setText("예약 후 오지않은 사용자로 등록");
        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();
    }

    private void DialogSimple() {
        final Dialog ratingDialog = new Dialog(atv_master.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_master_booking_ok);
        ratingDialog.setCancelable(true);

        TextView txtDate = (TextView) ratingDialog.findViewById(R.id.txtDate);
        TextView txtUserId = (TextView) ratingDialog.findViewById(R.id.txtUserId);
        TextView txtName = (TextView) ratingDialog.findViewById(R.id.txtName);
        TextView txtSex = (TextView) ratingDialog.findViewById(R.id.txtSex);
        TextView txtStyle = (TextView) ratingDialog.findViewById(R.id.txtStyle);
        TextView txtMemo = (TextView) ratingDialog.findViewById(R.id.txtMemo);

        txtDate.setText(adapter_waitting_permission.get(booking_position).getBooking_date() + ":" + adapter_waitting_permission.get(booking_position).getBooking_time());
        txtUserId.setText(adapter_waitting_permission.get(booking_position).getUser_id());
        txtName.setText(adapter_waitting_permission.get(booking_position).getUser_name());
        txtSex.setText(adapter_waitting_permission.get(booking_position).getUser_sex());
        txtStyle.setText(adapter_waitting_permission.get(booking_position).getHair_style());
        txtMemo.setText(adapter_waitting_permission.get(booking_position).getMemo());


        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk); // 승인
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new SendMsgTask().execute("2");

                ratingDialog.dismiss();
            }
        });

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo); // 거부
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Log.e("#####booking_position ", "" + "22");
                Log.e("#####booking_position ", "" + "22");
                new SendMsgTask().execute("3");

                ratingDialog.dismiss();
            }
        });

        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();


    }

    public void onClick(View view) {// 버튼 눌렀을때
        Log.e("#####11111 ", "" + btn_Case);
        switch (view.getId()) {
            case R.id.Btn1:// 예약 대기 중인 것들

                if (btn_Case) break;

                btn_Case = true;
                Log.e("#####11111 ", "" + btn_Case);
                btn1.setTextColor(Color.parseColor("#ff000000"));
                btn2.setTextColor(Color.parseColor("#ffaaaaaa"));
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.INVISIBLE);


                if (adapter_waitting_permission.size() > 0) {
                    ListAdapter_waitting_permission adapter = new ListAdapter_waitting_permission(mContext, 0, adapter_waitting_permission);
                    listView.setAdapter(adapter);
                    break;
                } else {
                    new SendMsgTask().execute("1"); // 내 사용 내역 셋팅
                }

                break;

            case R.id.Btn2:// 이미 예약 된 목록들
                if (!btn_Case) break;

                btn_Case = false;
                Log.e("#####11111 ", "" + "33333");
                btn1.setTextColor(Color.parseColor("#ffaaaaaa"));
                btn2.setTextColor(Color.parseColor("#ff000000"));
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);

                if (adapter_booking_list.size() > 0) {
                    ListAdapter_booking_list adapter = new ListAdapter_booking_list(mContext, 0, adapter_booking_list);
                    listView.setAdapter(adapter);
                    break;
                } else {
                    new SendMsgTask().execute("4"); // 내 사용 내역 셋팅
                }

                break;


        }
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

                Map<String, String> map = new HashMap<>();
                map.put("beauty_num", CBeautyInfo.num);
                JSONObject json = SC.Json_Messenger("master_newBookingList", SC.GET, map);

                try {
                    JSONArray jsonArray = json.getJSONArray("bookingyList");

                    adapter_waitting_permission.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter_waitting_permission.add(new adapter_master_bookingList(jsonObject.get("num").toString(),
                                jsonObject.get("user_id").toString(),
                                jsonObject.get("user_name").toString(),
                                jsonObject.get("user_sex").toString(),
                                jsonObject.get("booking_date").toString(),
                                jsonObject.get("booking_time").toString(),
                                jsonObject.get("end_time").toString(),
                                jsonObject.get("create_date").toString(),
                                jsonObject.get("memo").toString(),
                                jsonObject.get("hair_style").toString()));
                    }


                    return "1";

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return "1";
            } else if (type.compareTo("2") == 0) {

                Map<String, String> map = new HashMap<>();
                map.put("num", adapter_waitting_permission.get(booking_position).getNum());
                map.put("title", CBeautyInfo.title);
                map.put("beauty_num", CBeautyInfo.num);
                map.put("user_id", adapter_waitting_permission.get(booking_position).getUser_id());
                map.put("date", adapter_waitting_permission.get(booking_position).getBooking_date());
                JSONObject json = SC.Json_Messenger("master_newBookingList", SC.POST, map);
                try {

                    String resultCode = json.get("resultCode").toString();


                    if (resultCode.equals("200")) {
                        return "2";
                    }

                    return "0";


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (type.compareTo("3") == 0) {

                Log.e("#####booking_position ", "" + "1111111111111111111111111");
                Log.e("#####booking_position ", "" + booking_position);
                Map<String, String> map = new HashMap<>();
                map.put("num", adapter_waitting_permission.get(booking_position).getNum());
                map.put("title", CBeautyInfo.title);
                map.put("user_id", adapter_waitting_permission.get(booking_position).getUser_id());
                map.put("date", adapter_waitting_permission.get(booking_position).getBooking_date());
                JSONObject json = SC.Json_Messenger("master_newBookingList", SC.DELETE, map);
                try {

                    String resultCode = json.get("resultCode").toString();


                    if (resultCode.equals("200")) {
                        return "3";
                    }

                    return "0";


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else if (type.compareTo("3_2") == 0) {

                Log.e("#####booking_position ", "" + booking_position);
                Map<String, String> map = new HashMap<>();
                map.put("num", adapter_booking_list.get(booking_position).getNum());
                map.put("beauty_num", CBeautyInfo.num);
                map.put("title", CBeautyInfo.title);
                map.put("user_id", adapter_booking_list.get(booking_position).getUser_id());
                map.put("user_name", adapter_booking_list.get(booking_position).getUser_name());
                map.put("date", adapter_booking_list.get(booking_position).getBooking_date());
                JSONObject json = SC.Json_Messenger("master_booking_cancel_DELETE", SC.DELETE, map);
                try {

                    String resultCode = json.get("resultCode").toString();


                    if (resultCode.equals("200")) {
                        return "3_2";
                    }

                    return "0";


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }

            } else if (type.compareTo("4") == 0) {

                Map<String, String> map = new HashMap<>();
                map.put("beauty_num", CBeautyInfo.num);
                map.put("booking_date", getDate());
                JSONObject json = SC.Json_Messenger("master_bookingList", SC.GET, map);

                try {
                    JSONArray jsonArray = json.getJSONArray("bookingList");
                    adapter_booking_list.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter_booking_list.add(new adapter_master_bookingList(jsonObject.get("num").toString(),
                                jsonObject.get("user_id").toString(),
                                jsonObject.get("user_name").toString(),
                                jsonObject.get("user_sex").toString(),
                                jsonObject.get("booking_date").toString(),
                                jsonObject.get("booking_time").toString(),
                                jsonObject.get("end_time").toString(),
                                jsonObject.get("create_date").toString(),
                                jsonObject.get("memo").toString(),
                                jsonObject.get("hair_style").toString()));
                    }
                    return "4";

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (type.compareTo("5") == 0) {

                Map<String, String> map = new HashMap<>();
                map.put("user_id", adapter_booking_list.get(booking_position).getUser_id());
                map.put("penalty_create_date", adapter_booking_list.get(booking_position).getUser_id());
                JSONObject json = SC.Json_Messenger("master_make_penalty", SC.PUT, map);

                try {
                    String resultCode = json.get("resultCode").toString();


                    if (resultCode.equals("200")) {
                        return "5";
                    }

                    return "0";


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                ListAdapter_waitting_permission adapter = new ListAdapter_waitting_permission(mContext, 0, adapter_waitting_permission);
                listView.setAdapter(adapter);
            } else if (result.compareTo("2") == 0) {
                Toast.makeText(mContext, "예약 승인 완료", Toast.LENGTH_SHORT).show();
                adapter_waitting_permission.remove(booking_position);
                ListAdapter_waitting_permission adapter = new ListAdapter_waitting_permission(mContext, 0, adapter_waitting_permission);
                listView.setAdapter(adapter);
            } else if (result.compareTo("3") == 0) {
                Toast.makeText(mContext, "예약 거절 완료", Toast.LENGTH_SHORT).show();
                adapter_waitting_permission.remove(booking_position);
                ListAdapter_waitting_permission adapter = new ListAdapter_waitting_permission(mContext, 0, adapter_waitting_permission);
                listView.setAdapter(adapter);
            } else if (result.compareTo("3_2") == 0) {
                Toast.makeText(mContext, "예약 거절 완료", Toast.LENGTH_SHORT).show();
                adapter_booking_list.remove(booking_position);
                ListAdapter_booking_list adapter = new ListAdapter_booking_list(mContext, 0, adapter_booking_list);
                listView.setAdapter(adapter);
            } else if (result.compareTo("4") == 0) {
                ListAdapter_booking_list adapter = new ListAdapter_booking_list(mContext, 0, adapter_booking_list);
                listView.setAdapter(adapter);
            } else if (result.compareTo("5") == 0) {
                Alert alert = new Alert();
                alert.showAlert(getContext(), "소중한 정보 감사합니다.");


                Toast.makeText(mContext, "처리되었습니다", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "오류가 발생했습니다. 다시 시도해주세요", Toast.LENGTH_LONG).show();
            }
        }
    }

    private String getDate() {
        long time = System.currentTimeMillis();
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        return f.format(new Date(time));
    }

    private class ListAdapter_waitting_permission extends ArrayAdapter<adapter_master_bookingList> {

        private ArrayList<adapter_master_bookingList> mNoticeData;


        public ListAdapter_waitting_permission(Context context, int resource, ArrayList<adapter_master_bookingList> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            rowView = inflater.inflate(R.layout.adapter_master_booking_list, null, true);

            TextView txtDate = (TextView) rowView.findViewById(R.id.txtDate);
            TextView txtUserId = (TextView) rowView.findViewById(R.id.txtUserId);
            TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
            TextView txtStyle = (TextView) rowView.findViewById(R.id.txtStyle);
            TextView txtMemo = (TextView) rowView.findViewById(R.id.txtMemo);
            TextView txtCreateDate = (TextView) rowView.findViewById(R.id.txtCreateDate);
            Button btnTel = (Button) rowView.findViewById(R.id.btnTel);
            Button btnManager = (Button) rowView.findViewById(R.id.btnManager);


            txtDate.setText(mNoticeData.get(position).getBooking_date() + ". " + mNoticeData.get(position).getBooking_time());
            txtUserId.setText(mNoticeData.get(position).getUser_id());
            txtName.setText(mNoticeData.get(position).getUser_name() + " / " + mNoticeData.get(position).getUser_sex());
            txtStyle.setText(mNoticeData.get(position).getHair_style());
            txtMemo.setText(mNoticeData.get(position).getMemo());
            txtCreateDate.setText(mNoticeData.get(position).getCreate_date());

            btnTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num = mNoticeData.get(position).getUser_id();
                    if (num.charAt(0) == '+') {
                        num = "0" + num.substring(3);
                    }

                    Dialog_tel(num);

                }
            });
            btnManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        booking_position = position;
                    Log.e("#####booking_position ", "" + "333");
                        DialogSimple();
                }
            });


            return rowView;
        }
    }

    private class ListAdapter_booking_list extends ArrayAdapter<adapter_master_bookingList> {

        private ArrayList<adapter_master_bookingList> mNoticeData;


        public ListAdapter_booking_list(Context context, int resource, ArrayList<adapter_master_bookingList> noticeData) {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            rowView = inflater.inflate(R.layout.adapter_master_booking_list, null, true);

            if (mNoticeData.get(position).getBooking_date().equals(getDate())) {
                RelativeLayout morther_layout = (RelativeLayout) rowView.findViewById(R.id.morther_layout);

                morther_layout.setBackgroundColor(Color.rgb(143, 204, 133));

            }
            TextView txtDate = (TextView) rowView.findViewById(R.id.txtDate);
            TextView txtUserId = (TextView) rowView.findViewById(R.id.txtUserId);
            TextView txtName = (TextView) rowView.findViewById(R.id.txtName);
            TextView txtStyle = (TextView) rowView.findViewById(R.id.txtStyle);
            TextView txtMemo = (TextView) rowView.findViewById(R.id.txtMemo);
            TextView txtCreateDate = (TextView) rowView.findViewById(R.id.txtCreateDate);
            Button btnTel = (Button) rowView.findViewById(R.id.btnTel);
            Button btnManager = (Button) rowView.findViewById(R.id.btnManager);


            txtDate.setText(mNoticeData.get(position).getBooking_date() + ". " + mNoticeData.get(position).getBooking_time());
            txtUserId.setText(mNoticeData.get(position).getUser_id());
            txtName.setText(mNoticeData.get(position).getUser_name() + " / " + mNoticeData.get(position).getUser_sex());
            txtStyle.setText(mNoticeData.get(position).getHair_style());
            txtMemo.setText(mNoticeData.get(position).getMemo());
            txtCreateDate.setText(mNoticeData.get(position).getCreate_date());

            btnTel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String num = mNoticeData.get(position).getUser_id();
                    if (num.charAt(0) == '+') {
                        num = "0" + num.substring(3);
                    }

                    Dialog_tel(num);

                }
            });
            btnManager.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                        booking_position = position;
                    Log.e("#####booking_position ", "" + "333");
                        DialogSimple_forMaster();
                }
            });


            return rowView;
        }
    }
    private void Dialog_tel(final String num) {
        final Dialog ratingDialog = new Dialog(atv_master.this, android.R.style.Theme_Translucent_NoTitleBar);
        ratingDialog.setContentView(R.layout.dialog_nomal);
        ratingDialog.setCancelable(true);

        TextView txtTitle = (TextView) ratingDialog.findViewById(R.id.txtTitle);
        TextView txtMsg = (TextView) ratingDialog.findViewById(R.id.txtMsg);

        txtTitle.setText("전화걸기");
        txtMsg.setText(num+"으로 전화를 거시겠습니까?");

        Button button7 = (Button) ratingDialog.findViewById(R.id.btnOk); // 승인
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent2 = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"+num));
                if (ActivityCompat.checkSelfPermission(atv_master.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(intent2);

                ratingDialog.dismiss();
            }
        });
        button7.setText("전화걸기");

        Button button = (Button) ratingDialog.findViewById(R.id.btnNo); // 거부
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ratingDialog.dismiss();
            }
        });
        ImageButton btnClose = (ImageButton) ratingDialog.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ratingDialog.cancel();
            }
        });
        ratingDialog.show();
    }
}