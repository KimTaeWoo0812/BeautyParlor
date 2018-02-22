package beauty.tw.beauty_mother.a2_Roading_Join;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.BackPressCloseHandler;
import beauty.tw.beauty_mother.a1_sub_class.CSharedPreferences;
import beauty.tw.beauty_mother.a1_sub_class.SC;
import beauty.tw.beauty_mother.a2_Roading_Join.search.KoreanTextMatch;
import beauty.tw.beauty_mother.a2_Roading_Join.search.KoreanTextMatcher;
import beauty.tw.beauty_mother.a3_mainpage.atv_main;


/**
 * Created by kimtaewoo on 2017-01-15.
 */
public class atv_Seach_beauty extends AtvBase {
    private ArrayList<adapter_Seach_beauty> adapter_data         = new ArrayList<adapter_Seach_beauty>();
    private ArrayList<adapter_Seach_beauty> mNoticeData          = new ArrayList<adapter_Seach_beauty>();
    private List<adapter_Seach_beauty> beautyList         = new LinkedList<adapter_Seach_beauty>();

    private BackPressCloseHandler backPressCloseHandler;

    EditText search;
    Context mContext             = this;
    ListView listView;
    KoreanTextMatch match;
    String strTempBeautyNum;
    String strTempBeautyName;
    Button btn1;
    CSharedPreferences sharedPreferences;

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
        setView(R.layout.atv_seach_beauty);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
        //moveTaskToBack(true);
    }
    @Override
    protected void init() {
        backPressCloseHandler = new BackPressCloseHandler(this);

        mTxtTopTitle.setText("미용실 선택");


        sharedPreferences = new CSharedPreferences(this);

        search = (EditText) findViewById(R.id.editText3);

        listView = (ListView) findViewById(R.id.listView2);
        btn1 = (Button) findViewById(R.id.btn1);
        listView.setSelector(R.color.appColor) ;
        search.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                adapter_data.clear();
                // 입력되는 텍스트에 변화가 있을 때

                // listView.setSelection(300);
                // int index = listView.getFirstVisiblePosition();
                // listView.smoothScrollToPosition(index);
                Log.e("##", "포지션: " + listView.getFirstVisiblePosition());
                Log.e("##", "" + listView.getCheckedItemPosition());

                // Log.i("###",""+s.toString());
                if (s.toString().equals("")) {
                    Log.i("###", "널");
                    android.widget.ListAdapter adapter = null;
                    adapter = new ListAdapter(mContext, 0, adapter_data);
                    listView.setAdapter(adapter);

                    return;
                }

                String text;
                String pattern;
                // Log.i("###", "adapter_data.size(): "+adapter_data.size());

                for (int i = 0; i < beautyList.size(); i++)
                {
                    text = beautyList.get(i).getTitle(); // 리스트를 하나씩 비교
                    pattern = s.toString();
                    // Log.i("###",""+text+" "+pattern);

                    match = KoreanTextMatcher.match(text, pattern);
                    if (match.success())
                    {
                        // Log.i("###","match!");

                        // 여기서 리스트뷰 갱신
                        adapter_data.add(new adapter_Seach_beauty(beautyList.get(i).getNum(), beautyList.get(i).getTitle(), beautyList.get(i).getLoc()));

                        // System.out.format("%s: %s[%d]에서 시작, 길이 %d\n",
                        // match.value(), text, match.index(), match.length());
                    }
                }

                ListAdapter adapter = null;
                adapter = new ListAdapter(mContext, 0, adapter_data);
                listView.setAdapter(adapter);

                Log.i("###", "리스트뷰 갱신!!");
            }


            @Override
            public void afterTextChanged(Editable arg0)
            {
                // 입력이 끝났을 때
                Log.i("###", "afterTextChanged");
//                int index = listView.getFirstVisiblePosition() + 1;
//
//                listView.setSelection(index);


            }


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after)
            {
                // 입력하기 전에
            }
        });


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Log.e("asdasd",""+mNoticeData.get(position).getTitle());
                strTempBeautyNum = mNoticeData.get(position).getNum();
                strTempBeautyName = mNoticeData.get(position).getTitle();
            }
        });


        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (strTempBeautyNum.equals("")){
                    Toast.makeText(atv_Seach_beauty.this, "미용실을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                    DialogSimple();
            }
        });

        new SendMsgTask().execute("1");
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
                JSONObject json = SC.Json_Messenger("beauty", SC.GET, map);

                try {
                    JSONArray jsonArray = json.getJSONArray("beautyList");

                    for (int i = 0; i < jsonArray.length(); i++)
                    {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        beautyList.add(new adapter_Seach_beauty(jsonObject.get("num").toString(),
                                jsonObject.get("title").toString(),
                                jsonObject.get("loc").toString()));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return "1";
            }
            if (type.compareTo("2") == 0) {

                Map<String, String> map = new HashMap<>();
                map.put("beautyNum",strTempBeautyNum);
                map.put("id", CUserInfo.id);
                JSONObject json = SC.Json_Messenger("users", SC.PUT, map);

                try {
                    String resultCode = json.get("resultCode").toString();

                    if(!resultCode.equals("200"))
                        return "0";

                    CUserInfo.isMaster = json.get("isMaster").toString();
                    CBeautyInfo.num = json.get("num").toString();
                    CBeautyInfo.title = json.get("title").toString();
                    CBeautyInfo.working_time_from = json.get("working_time_from").toString();
                    CBeautyInfo.working_time_to = json.get("working_time_to").toString();
                    CBeautyInfo.loc = json.get("loc").toString();
                    CBeautyInfo.booking_method = json.get("booking_method").toString();
                    CBeautyInfo.booking_delay_time = json.get("booking_delay_time").toString();
                    CBeautyInfo.booking_delay_time_for_cut = json.get("booking_delay_time_for_cut").toString();
                    CBeautyInfo.phone = json.get("phone").toString();
                    CBeautyInfo.master_id = json.get("master_id").toString();
                    CBeautyInfo.dayOff = json.get("dayOff").toString();

                    String strIsNotice = json.get("isNotice").toString();
                    if (strIsNotice.equals("1")) {
                        CBeautyInfo.isNotice = true;
                        CBeautyInfo.notice = json.get("notice").toString();
                    } else {
                        CBeautyInfo.isNotice = false;
                        CBeautyInfo.notice = json.get("notice").toString();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return "2";
            }
            return "0";
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0) {
                //그냥 받아서 map에 넣어놓기만 한다 아무런 동작이 필요없다
            }
            else if (result.compareTo("2") == 0) {
                //미용실을 선택했다. 이제 다음화면으로 ㄱㄱ
                sharedPreferences.savePreferences("isLogin", "1");
                Toast.makeText(atv_Seach_beauty.this, "반갑습니다 " + CUserInfo.name + "님 ^^", Toast.LENGTH_SHORT).show();
                Intent intent2 = new Intent(atv_Seach_beauty.this, atv_main.class);
                startActivity(intent2);
                finish();// 이 화면 종료

            }
            else if (result.compareTo("0") == 0) {
                //서버한테 오류메시지를 받았다
                Toast.makeText(atv_Seach_beauty.this, "오류가 발생했습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT).show();

            }
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

                new SendMsgTask().execute("2");

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

    private class ListAdapter extends ArrayAdapter<adapter_Seach_beauty>
    {
        public ListAdapter(Context context, int resource, ArrayList<adapter_Seach_beauty> noticeData)
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

