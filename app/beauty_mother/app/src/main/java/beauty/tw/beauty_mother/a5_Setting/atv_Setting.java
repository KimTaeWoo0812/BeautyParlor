package beauty.tw.beauty_mother.a5_Setting;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.SC;
import beauty.tw.beauty_mother.a2_Roading_Join.adapter_Seach_beauty;


/**
 * Created by kimtaewoo on 2017-01-15.
 */
public class atv_Setting extends AtvBase {


    Context mContext            = this;
    private ListView listView;
    private boolean                             btn_Case      = true;
    private ArrayList<adapter_setting> adapter_record        = new ArrayList<adapter_setting>();
    private ArrayList<adapter_Seach_beauty> adapter_seach        = new ArrayList<adapter_Seach_beauty>();
    String strSeach;
    String strTempBeautyName="";
    String strTempBeautyNum="";

    Button btn1;
    Button btn2;
    Button btn_choose;
    ImageView img1;
    ImageView img2;
    LinearLayout baseSeach;
    EditText txtSeach;
    ImageButton btnSearch;


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void setView() {
        setView(R.layout.atv_setting);
    }

    @Override
    protected void init() {
        mTxtTopTitle.setText("나의 예약 내역");

        /**
         * 검색 부분이 어딧는지 못찾겠다. 앱 지웠다가 깔아서 처음 미용실 선택할 때 어떻게 동작하는지 찾아야한다.
         * 내 사용내역에서, 처음 셋팅하는 부분, 더보기 부분을 만들어야 한다.(소켓)
         * 미용실 변경에서,  검색 기능과 선택 후(리스트뷰 클릭 리스너에서 구현) CBeauty_info 수정까지 만들어야 한다. (소켓)
         * 미용실 검색은 검색하면 서버에서 받아오는 걸로 만들기
         *
         */



        baseSeach = (LinearLayout)findViewById(R.id.baseSearch);

        btn_choose = (Button) findViewById(R.id.btn_choose);
        btn1 = (Button) findViewById(R.id.Btn1);
        btn2 = (Button) findViewById(R.id.Btn2);
        img1 = (ImageView) findViewById(R.id.img1);
        img2 = (ImageView) findViewById(R.id.img2);
        txtSeach = (EditText) findViewById(R.id.txtSeach);
        listView = (ListView) findViewById(R.id.listView);
        btnSearch = (ImageButton) findViewById(R.id.btnSearch);


        final SwipeRefreshLayout mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_layout);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // 새로고침 코드
                if (btn_Case) {
                    new SendMsgTask().execute("1"); // 내 사용 내역 셋팅
                }

                mSwipeRefreshLayout.setRefreshing(false); // 새로고침 완료

            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (btn_Case) { // 예약내역

                } else { // 미용실 변경
                            Log.e("asdasd",""+adapter_seach.get(position).getTitle());
                            strTempBeautyNum = adapter_seach.get(position).getNum();
                            strTempBeautyName = adapter_seach.get(position).getTitle();
                }

            }
        });
        new SendMsgTask().execute("1"); // 내 사용 내역 셋팅

    }


    public void onClick(View view)
    {// 버튼 눌렀을때
        switch (view.getId())
        {
            case R.id.Btn1:// 내 예약 내역

                if (btn_Case) break;
                btn1.setTextColor(Color.parseColor("#ff000000"));
                btn2.setTextColor(Color.parseColor("#ffaaaaaa"));
                img1.setVisibility(View.VISIBLE);
                img2.setVisibility(View.INVISIBLE);
                baseSeach.setVisibility(View.GONE);
                btn_choose.setVisibility(View.GONE);

                btn_Case = !btn_Case;

                if (adapter_record.size() > 0)
                {
                    Record_ListAdapter adapter = new Record_ListAdapter(mContext, 0, adapter_record);
                    listView.setAdapter(adapter);
                    break;
                }

                break;

            case R.id.Btn2:// 미용실 검색 페이지
                if (!btn_Case) break;

                btn1.setTextColor(Color.parseColor("#ffaaaaaa"));
                btn2.setTextColor(Color.parseColor("#ff000000"));
                img1.setVisibility(View.INVISIBLE);
                img2.setVisibility(View.VISIBLE);
                baseSeach.setVisibility(View.VISIBLE);
                btn_choose.setVisibility(View.VISIBLE);

                btn_Case = !btn_Case;

                adapter_seach.clear();

                Seach_ListAdapter adapter = new Seach_ListAdapter(mContext, 0, adapter_seach);
                listView.setAdapter(adapter);

                break;


            case R.id.btnSearch:// 미용실 검색 버튼

                strSeach = txtSeach.getText().toString();


                new SendMsgTask().execute("3");


                break;

            case R.id.btn_choose:// 미용실 선택 버튼

                if (strTempBeautyNum.equals("")){
                    Toast.makeText(atv_Setting.this, "미용실을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                DialogSimple();

        }
        try
        {
            Thread.sleep(10);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void DialogSimple()
    {
        String clause = strTempBeautyName+"을(를) 선택하시겠습니까?";
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

                new SendMsgTask().execute("4");

                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        // Icon for AlertDialog
        alert.show();

        TextView msgView = (TextView) alert.findViewById(android.R.id.message);
        msgView.setTextSize(16);

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
                map.put("id", CUserInfo.id);
                JSONObject json = SC.Json_Messenger("booking", SC.GET, map);

                try {
                    adapter_record.clear();

                    JSONArray jsonArray = json.getJSONArray("bookingyList");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter_record.add(new adapter_setting(jsonObject.get("num").toString(),
                                jsonObject.get("title").toString(),
                                jsonObject.get("style").toString(),
                                jsonObject.get("memo").toString(),
                                jsonObject.get("date").toString()));
                    }

                    String resultCode = json.get("resultCode").toString();

                    if(!resultCode.equals("200"))
                        return "0";

                } catch (JSONException e) {
                    e.printStackTrace();
                }catch (NullPointerException e) {
                    e.printStackTrace();
                }

                return "1";
            }
            else if (type.compareTo("2") == 0) {



            }
            else if (type.compareTo("3") == 0) {
                Map<String, String> map = new HashMap<>();
                map.put("beautyTitle",strSeach);

                JSONObject json = SC.Json_Messenger("beautyListUsingName", SC.GET, map);


                try {
                    JSONArray jsonArray = json.getJSONArray("beautyList");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        adapter_seach.add(new adapter_Seach_beauty(jsonObject.get("num").toString(),
                                jsonObject.get("title").toString(),
                                jsonObject.get("loc").toString()));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "3";
            }
            else if (type.compareTo("4") == 0) {

                Map<String, String> map = new HashMap<>();
                map.put("beautyNum",strTempBeautyNum);
                map.put("id", CUserInfo.id);
                JSONObject json = SC.Json_Messenger("users", SC.PUT, map);

                try {
                    String resultCode = json.get("resultCode").toString();

                    if(!resultCode.equals("200"))
                        return "0";

                    CBeautyInfo.num = json.get("num").toString();
                    CBeautyInfo.title = json.get("title").toString();
                    CBeautyInfo.working_time_from = json.get("working_time_from").toString();
                    CBeautyInfo.working_time_to = json.get("working_time_to").toString();
                    CBeautyInfo.loc = json.get("loc").toString();
                    CBeautyInfo.booking_method = json.get("booking_method").toString();
                    CBeautyInfo.booking_delay_time = json.get("booking_delay_time").toString();
                    CBeautyInfo.phone = json.get("phone").toString();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return "4";
            }


            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                Record_ListAdapter adapter = new Record_ListAdapter(mContext, 0, adapter_record);
                listView.setAdapter(adapter);
            }
            else if (result.compareTo("3") == 0) {

                if(adapter_seach.size()==0){
                    Toast.makeText(atv_Setting.this, "검색 결과가 없습니다", Toast.LENGTH_LONG).show();
                }
                Seach_ListAdapter adapter = new Seach_ListAdapter(mContext, 0, adapter_seach);
                listView.setAdapter(adapter);
            }
            else if (result.compareTo("4") == 0) {
                //미용실을 선택했다. 이제 다음화면으로 ㄱㄱ
                Toast.makeText(atv_Setting.this, "반갑습니다 " + CUserInfo.name + "님 ^^", Toast.LENGTH_SHORT).show();
                finish();// 이 화면 종료

            }
            else if (result.compareTo("0") == 0) {

            }
        }
    }


    private class Record_ListAdapter extends ArrayAdapter<adapter_setting>
    {

        private ArrayList<adapter_setting> mNoticeData;


        public Record_ListAdapter(Context context, int resource, ArrayList<adapter_setting> noticeData)
        {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }


        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            View rowView = null;

            if (mNoticeData.get(position).getStyle().equals("더보기"))
            {
                rowView = inflater.inflate(R.layout.row_view_more, null, true);

                Button btnViewMore = (Button) rowView.findViewById(R.id.btnViewMore);

                btnViewMore.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        // 더보기 눌렀을 때

                        Log.i("chat_add", "더보기 클릭: ");
                        new SendMsgTask().execute("2");  // 리스트 추가

                    }
                });
                return rowView;
            }

            rowView = inflater.inflate(R.layout.adapter_setting, null, true);

            TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
            TextView txtStyle = (TextView) rowView.findViewById(R.id.txtStyle);
            TextView txtMemo = (TextView) rowView.findViewById(R.id.txtMemo);
            TextView date = (TextView) rowView.findViewById(R.id.date);


            txtTitle.setText(mNoticeData.get(position).gettitle());
            txtStyle.setText(mNoticeData.get(position).getStyle());
            txtMemo.setText(mNoticeData.get(position).getMemo());
            date.setText(mNoticeData.get(position).getDate());

            return rowView;
        }
    }

    private class Seach_ListAdapter extends ArrayAdapter<adapter_Seach_beauty>
    {
        private ArrayList<adapter_Seach_beauty> mNoticeData;
        public Seach_ListAdapter(Context context, int resource, ArrayList<adapter_Seach_beauty> noticeData)
        {
            super(context, resource, noticeData);
            mNoticeData = noticeData;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {

            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
            // View rowView = inflater.inflate(R.layout.menu_adapter, null, true);

            View rowView = inflater.inflate(R.layout.adapter_seach_beauty, null, true);
            TextView txtTitle = (TextView) rowView.findViewById(R.id.txtTitle);
            TextView txtLoc = (TextView) rowView.findViewById(R.id.txtLoc);
            LinearLayout layout = (LinearLayout)rowView.findViewById(R.id.sse);

            txtTitle.setText(mNoticeData.get(position).getTitle());
            txtLoc.setText(mNoticeData.get(position).getLoc());

            return rowView;
        }
    }
}