package beauty.tw.beauty_mother.a2_Roading_Join;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import beauty.tw.beauty_mother.AtvBase;
import beauty.tw.beauty_mother.R;
import beauty.tw.beauty_mother.a1_Data_Package.CBeautyInfo;
import beauty.tw.beauty_mother.a1_Data_Package.CUserInfo;
import beauty.tw.beauty_mother.a1_sub_class.Alert;
import beauty.tw.beauty_mother.a1_sub_class.BackPressCloseHandler;
import beauty.tw.beauty_mother.a1_sub_class.CSharedPreferences;
import beauty.tw.beauty_mother.a1_sub_class.PermissionRequester;
import beauty.tw.beauty_mother.a1_sub_class.SC;
import beauty.tw.beauty_mother.a3_mainpage.atv_main;


/**
 * Created by kimtaewoo on 2017-01-13.
 */
public class atv_Join  extends AtvBase {
    private BackPressCloseHandler backPressCloseHandler;
    Button checkBox_1;
    Button checkBox_2;
    EditText input_name;
    String name     = "";
    Context context  = this;
    CSharedPreferences sharedPreferences;
    String sex;
    String phoneNum;
    String phone_id;

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
    }


    @Override
    protected void onResume()
    {
        super.onResume();
    }


    @Override
    protected void onPause()
    {
        super.onPause();
    }


    @Override
    protected void setView()
    {
        setView(R.layout.atv_join);
    }
    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        backPressCloseHandler.onBackPressed();
        //moveTaskToBack(true);
    }


    @Override
    protected void init()
    {
        backPressCloseHandler = new BackPressCloseHandler(this);

        mTxtTopTitle.setText("기본정보 입력");

        checkBox_1 = (Button) findViewById(R.id.btn01);
        checkBox_2 = (Button) findViewById(R.id.btn02);
        input_name = (EditText) findViewById(R.id.input_name);
        sharedPreferences = new CSharedPreferences(this);

        checkBox_1.setOnClickListener(new View.OnClickListener()
        {

            @Override
            public void onClick(View v)
            {
                checkBox_1.setSelected(!checkBox_1.isSelected());
                checkBox_2.setSelected(false);

            }
        });

        checkBox_2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                checkBox_2.setSelected(!checkBox_2.isSelected());
                checkBox_1.setSelected(false);
            }
        });
    }


    public void onClick(View view)
    {// 버튼 눌렀을때
        switch (view.getId())
        {
            case R.id.btn_submit:// 가입

                name = input_name.getText().toString();

                boolean check = false;

                if (checkBox_1.isSelected())
                {
                    sex = "남성";
                    check = true;
                }
                if (checkBox_2.isSelected())
                {
                    sex = "여성";
                    check = true;
                }
                if(!check){
                    Toast.makeText(this, "성별을 선택해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (name.length() < 2)
                {
                    Toast.makeText(this, "이름은 2자 이상 입니다.", Toast.LENGTH_SHORT).show();
                    break;
                }
                InputMethodManager imm= (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);


                boolean permission = CheckPermission();
                Log.i("GetPermission", "permission:  " + permission);

                if (!permission) return;

                DialogSimple();

                break;
        }

    }

    private void DialogSimple()
    {
        String clause = "1. 개인정보의 취급목적\n"
                + "\n"
                + "회원가입 시 서비스 이용을 위해 필요한 최소한의 개인정보만을 수집합니다. 귀하가 서비스를 이용하기 위해서는 회원가입 시 (핸드폰 번호, 별명)을 필수적으로 입력하셔야 합니다.\n"
                + "\n" + "개인정보 항목별 구체적인 수집목적 및 이용목적은 다음과 같습니다.\n"
                + "핸드폰 번호, 이름, 성별 :회원제 서비스 이용에 따른 본인 식별 절차에 이용.\n"
                + "본 서비스는 아동에게 유해한 정보를 게시하거나 제공하고 있지 않습니다.\n"
                + "\n" + "2. 개인정보의 취급 및 보유기간 \n"
                + "\n" + "수집된 개인정보의 보유기간은 회원가입 하신 후 해지(탈퇴신청등)시까지 입니다. 또한 해지시 회원님의 개인정보를 재생이 불가능한 방법으로 즉시 파기하며 (개인정보가 제3자에게 제공된 경우에는 제3자에게도 파기하도록 지시합니다.) 다만 다음 각호의 경우에는 각 호에 명시한 기간동안 개인정보를 보유합니다.\n"
                + "① 상법 등 법령의 규정에 의하여 보존할 필요성이 있는 경우에는 법령에서 규정한 보존기간 동안 거래내역과 최소한의 기본정보를 보유함 \n" + "② 보유기간을 회원님에게 미리 고지하고 그 보유기간이 경과하지 아니한 경우와 개별적으로 회원님의 동의를 받을 경우에는 약속한 보유기간 동안 보유함 \n"
                + "3. 동의 거부 권리 및 동의 거부 시 불이익 내용 \n" + "\n"
                + "귀하는 개인정보의 수집목적 및 이용목적에 대한 동의를 거부할 수 있으며, 동의 거부시 회원가입이 되지 않으며, 제공하는 모든 서비스를 이용할 수 없습니다.";

        AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setMessage(clause).setCancelable(false).setPositiveButton("취소", new DialogInterface.OnClickListener()
        {
            public void onClick(DialogInterface dialog, int id_)
            {
                // do something
                dialog.cancel();
            }
        }).setNegativeButton("동의", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id_) {
                name = name.replace("\n", "");

                new SendMsgTask().execute("1");

                dialog.cancel();
            }
        });
        AlertDialog alert = alt_bld.create();
        // Title for AlertDialog
        alert.setTitle("이용 약관");
        // Icon for AlertDialog
        alert.show();

        TextView msgView = (TextView) alert.findViewById(android.R.id.message);
        msgView.setTextSize(12);

    }


    public class SendMsgTask extends AsyncTask<String, Void, String>
    {
        String msg = "";


        @Override
        protected void onPreExecute()
        {
            super.onPreExecute();
        }


        @Override
        protected String doInBackground(String... params)
        {
            String type = params[0];
            if (type.compareTo("1") == 0) {

                TelephonyManager telManager = (TelephonyManager) context.getSystemService(context.TELEPHONY_SERVICE);
                phoneNum = telManager.getLine1Number();

                if(phoneNum.equals(null)){
                    return "10";
                }

                phone_id = FirebaseInstanceId.getInstance().getToken();

                Log.e("###phone_id",phone_id);
                Map<String, String> map = new HashMap<>();

                map.put("id", phoneNum);
                map.put("name", name);
                map.put("sex", sex);
                map.put("phone_id", phone_id);

                JSONObject json = SC.Json_Messenger("users", SC.POST, map);

                try {
                    String resultCode = json.get("resultCode").toString();
                    Log.i("SC_GET MSG 1", "" + json.get("resultCode"));

                    if(resultCode.equals("404")){
                        return "0";
                    }
                    if(json.get("penalty_count").toString().equals("out")) //불량사용자
                        return "2";

                    if(resultCode.equals("200")) {
                        //로그인 성공.
                        CUserInfo.id = phoneNum;
                        CUserInfo.name = name;
                        CUserInfo.sex = sex;

                        sharedPreferences.savePreferences("id", phoneNum);
                        sharedPreferences.savePreferences("name", name);
                        sharedPreferences.savePreferences("sex", sex);
                        sharedPreferences.savePreferences("phone_id", phone_id);

                        return "1";
                    }
                    else{
                        return "100";
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                return "1";
            }
            return null;
        }


        @Override
        protected void onPostExecute(String result)
        {
            super.onPostExecute(result);

            if (result.compareTo("1") == 0)
            {
                Intent intent5 = new Intent(atv_Join.this, atv_Seach_beauty.class);
                startActivity(intent5);
                finish();// 이 화면 종료
                return;
            }
            else if(result.compareTo("2") == 0){
                //계정 정지 상태 다이얼로그 뛰우고 앱 끄기
                //이건 아직 쓰지 않음
                Alert alert = new Alert();
                alert.showAlert(context, "당신은 불량 사용자로 지정되었습니다. \n앞으로 이 서비스를 이용하실 수 없습니다.");
            }
            else if(result.compareTo("10") == 0){
                Alert alert = new Alert();
                alert.showAlert(context, "핸드폰 번호가 없는 디바이스로는 이 앱을 사용하실수 없습니다.");
            }
            else
            {
                Toast.makeText(atv_Join.this, "회원가입 실패! 다시 시도해 주세요.", Toast.LENGTH_SHORT).show();
            }
        }

    }


    private boolean CheckPermission()
    {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);

        if (permissionCheck == PackageManager.PERMISSION_DENIED)
        {
            Log.i("GetPermission", "권한 없음");
            // 권한 없음
            GetPermission();

            return false;

        }
        else
        {
            Log.i("GetPermission", "권한 있음");
            // 권한 있음
            return true;
        }
    }


    private void GetPermission()
    {
        int result = new PermissionRequester.Builder(atv_Join.this).setTitle("권한 요청").setMessage("회원 고유 식별자로 사용할 핸드폰 번호를 받아오는데 권한이 필요합니다.").setPositiveButtonName("네").setNegativeButtonName("아니요.").create().request(Manifest.permission.READ_PHONE_STATE, 100, new PermissionRequester.OnClickDenyButtonListener()
        {
            @Override
            public void onClick(Activity activity)
            {
                Log.d("RESULT", "취소함.");
            }
        });

        if (result == PermissionRequester.ALREADY_GRANTED) {
            Log.d("RESULT", "권한이 이미 존재함.");
            if (ActivityCompat.checkSelfPermission(atv_Join.this, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED)
            {
                Log.d("RESULT", "권한이 존재함.");
            }
        }
        else if (result == PermissionRequester.NOT_SUPPORT_VERSION) Log.d("RESULT", "마쉬멜로우 이상 버젼 아님.");
        else if (result == PermissionRequester.REQUEST_PERMISSION) Log.d("RESULT", "요청함. 응답을 기다림.");

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults)
    {

        switch (requestCode)
        {
            case 100:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // 권한 허가
                    // 해당 권한을 사용해서 작업을 진행할 수 있습니다
                    DialogSimple();

                }
                else
                {
                    // 권한 거부
                    // 사용자가 해당권한을 거부했을때 해주어야 할 동작을 수행합니다

                    Alert alert = new Alert();
                    alert.showAlert(getContext(), "회원 고유 식별자로 사용할 핸드폰 번호를 받아오는데 권한이 필요합니다.\n\n[설정] > [권한]에서 해당 권한을 활성화해주세요.");
                }

                break;
        }
    }

}
