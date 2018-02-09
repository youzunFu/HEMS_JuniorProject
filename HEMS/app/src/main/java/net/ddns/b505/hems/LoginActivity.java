package net.ddns.b505.hems;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {
    private EditText Username, Password;
    private Button btn_callsignup;
    String user = null,pw = null;
    public static File file;
    private CheckBox memory,auto;
    public String phpinfo,logout,phpinfosignup;
    //使用SharedPreferences進行讀取
    private SharedPreferences pref,setting;
    //使用SharedPreferences.Editor進行儲存
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //制定SharedPreference的文件名為LoginInfo

        pref = getSharedPreferences("PREF_NFC", Context.MODE_PRIVATE);
       // setting = getSharedPreferences("auto",0);
        editor = pref.edit();
        Username = (EditText)findViewById(R.id.et_username);
        Password = (EditText)findViewById(R.id.et_password);
        memory = (CheckBox)findViewById(R.id.check_login);
        auto = (CheckBox)findViewById(R.id.check_auto);

        //建立一個file
        file = new File("/data/data/net.ddns.b505.hems/shared_prefs","auto.xml");

        Check_Login();

//-----------dialgo fragment-------------
        btn_callsignup = (Button) findViewById(R.id.btn_callsignup);
        btn_callsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this);

                final View mView = getLayoutInflater().inflate(R.layout.fragment_sing_up,null);
                final TextView tvSignUpInfo;
                final EditText etsignup_nickname, etsignup_username,
                                etsignup_password, etsignup_password_confirm;
                Button btn_signup;

                etsignup_nickname = (EditText) mView.findViewById(R.id.etsignup_nickname);
                etsignup_username = (EditText) mView.findViewById(R.id.etsignup_username);
                etsignup_password= (EditText) mView.findViewById(R.id.etsignup_password);
                etsignup_password_confirm= (EditText) mView.findViewById(R.id.etsignup_password_confirm);
                tvSignUpInfo = (TextView) mView.findViewById(R.id.tvSignUpInfo);
                btn_signup = (Button) mView.findViewById(R.id.btn_signup);

                btn_signup.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String url = "http://163.18.57.43/HEMSphp/plugschedule.php";
                        JsonArrayRequest ArrayRequest;
                        JsonObjectRequest ObjectRequest;
                        RequestQueue requestQueue;

                        String token,StrNickname,StrUsername,StrPassword,StrPasswordConfirm;
                        StrNickname =etsignup_nickname.getText().toString();
                        StrUsername = etsignup_username.getText().toString();
                        StrPassword = etsignup_password.getText().toString();
                        StrPasswordConfirm = etsignup_password_confirm.getText().toString();
                        requestQueue = Volley.newRequestQueue(mView.getContext());
                        //token = "mDSbpZrRXACEsBE8WR34";

                        if(StrPassword.equals(StrPasswordConfirm)){
                            SignUpAsync signUpAsync = new SignUpAsync(LoginActivity.this);
                            try {
                                phpinfosignup = signUpAsync.execute(StrNickname,StrUsername,StrPassword).get().toString();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            } catch (ExecutionException e) {
                                e.printStackTrace();
                            }
                            Toast.makeText(mView.getContext(),phpinfosignup,Toast.LENGTH_SHORT).show();

                            if(phpinfosignup.equals("INSERT")){
                                Toast.makeText(mView.getContext(),"成功註冊",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(mView.getContext(),"使用者名稱重複",Toast.LENGTH_SHORT).show();
                            }
                        }

                        else{
                            tvSignUpInfo.setText("密碼與確認密碼不相符");
                            //密碼 與 確認密碼不相符 2018/01/30 X
                        }

                    }

                });


                mBuilder.setView(mView);
                AlertDialog dialog = mBuilder.create();
                dialog.show();




            }

        });
//-----------dialgo fragment-------------


    }

    private void Check_Login() {
        boolean autoisRemember = pref.getBoolean("auto_check",false);  //判斷LoginInfo內記錄的自動登入是否有被勾選
        boolean memoryisRemember = pref.getBoolean("login_check",false);  //查看app中是否已經儲存過帳號密碼
        boolean logoutcheck = pref.getBoolean("logout",false);

        if(logoutcheck){ //經由MainActivity登出 把帳號密碼/記住密碼/自動登入 原資訊貼上 並不檢查自動登入
            if (memoryisRemember) {//記住帳號有被勾選 顯示出記憶的帳號密碼
                String user = pref.getString("UserName", "");
                String password = pref.getString("Password", "");
                Username.setText(user);
                Password.setText(password);
                memory.setChecked(true);
                if (autoisRemember) {auto.setChecked(true);}

                editor.putBoolean("logout",false);
                editor.putBoolean("auto_check",autoisRemember);
                editor.putBoolean("login_check",memoryisRemember);
                editor.commit();
            }
        }else {
            if (memoryisRemember) {//記住帳號有被勾選 顯示出記憶的帳號密碼
                String user = pref.getString("UserName", "");
                String password = pref.getString("Password", "");
                Username.setText(user);
                Password.setText(password);
                memory.setChecked(true);
                //if(autoisRemember) auto.setChecked(true);
                if (autoisRemember) { //自動登入+記住帳號有被勾選
                    BackgroundWorker backgroundWorker = new BackgroundWorker(this);
                    try {
                        phpinfo = backgroundWorker.execute(user,password).get().toString();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    String phpcheck[] =phpinfo.split(" ");

                    if(!phpcheck[0].equals("FAIL")){   //不等於 failure
                        SendIntent();
                    }

                } else {//記住帳號有被勾選

                }
                //記住帳號+自動登入未被勾選
            }
        }
    }

    public void OnLogin(View view) throws ExecutionException, InterruptedException {  //登入
        String user = Username.getText().toString();
        String password = Password.getText().toString();
        //如果記住密碼被勾選，將帳號密碼儲存
        //没有被勾選，將儲存的帳號密碼清除


        if(memory.isChecked()){  //將帳號與Check
            editor.putBoolean("login_check",true);
            editor.putString("UserName",user);
            editor.putString("Password",password);
            if(auto.isChecked()) {
                editor.putBoolean("auto_check", true);

            }else {
                editor.putBoolean("auto_check", false);

            }
        }else{
            editor.clear();
        }

        //使用commit將添加的數據提交
        editor.commit();

       // pref = getSharedPreferences("PREF_NFC", Context.MODE_PRIVATE);
        //pref.edit().putString("UserName",user).commit();
        String type = "login";


        checkID(user, password);


    }

    private void checkID(String user, String password) throws InterruptedException, ExecutionException {
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        phpinfo = backgroundWorker.execute(user,password).get().toString();
        String phpcheck[] =phpinfo.split(" ");

        if(!phpcheck[0].equals("FAIL")){   //不等於 failure
            new AlertDialog.Builder(this)
                    .setTitle("歡迎回來, " +phpcheck[2] +"!" )//設定視窗標題
                    .setMessage("")//設定顯示的文字
                    .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SendIntent();
                        }
                    })//設定結束的子視窗
                    .show();//呈現對話視窗
        }else {
            new AlertDialog.Builder(this)
                    .setTitle("您輸入的帳號或密碼有誤")//設定視窗標題
                    .setMessage("")//設定顯示的文字
                    .setPositiveButton("OK",new DialogInterface.OnClickListener(){
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })//設定結束的子視窗
                    .show();//呈現對話視窗
        }

        //phpcheck[2] 使用者名稱

        // 如果正確才跳轉 SendIntent();
    }

    public class BackgroundWorker extends AsyncTask<String,Void,String> {  //背景執行程式 (連上MySQL判斷帳號正確與否)
        Context context;
        BackgroundWorker (Context ctx) {
            context = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String result = IO(params);
            if (result != null) return result;
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == "failure") {
                Toast.makeText(LoginActivity.this, "您輸入的帳號或密碼有誤", Toast.LENGTH_LONG).show();
                editor.clear();
                editor.commit();
            }
        }   // 登入失敗，浮點顯示Logon failure : unknown user name or bad password .

    }

    @Nullable
    private String IO(String[] params) {
        try {
            String user_name = params[0];
            String password = params[1];
            URL url = new URL("http://163.18.57.43/HEMSphp/loginn.php"); //loginn.php 含回傳使用者 // login.php 不含回傳使用者
            //URL url = new URL("http://192.168.2.110/login.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("user_name", "UTF-8") + "=" + URLEncoder.encode(user_name, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            bufferedWriter.write(post_data);    //傳username、password給php
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line = "";
            if((line = bufferedReader.readLine()) != null) {
                result = line;
            } else {result = "failure";}




            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void ReadValue(){
        user = pref.getString("UserName","");
        pw = pref.getString("Password","");
    }

    public class SignUpAsync extends AsyncTask<String,Void,String> {  //背景執行程式 (連上MySQL判斷帳號正確與否)
        Context contextt;
        SignUpAsync (Context ctx) {
            contextt = ctx;
        }
        @Override
        protected String doInBackground(String... params) {
            String type = params[0];
            String result = IOO(params);
            if (result != null) return result;
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result == "failure") {
                Toast.makeText(LoginActivity.this, "您輸入的帳號或密碼有誤", Toast.LENGTH_LONG).show();
                editor.clear();
                editor.commit();
            }
        }   // 登入失敗，浮點顯示Logon failure : unknown user name or bad password .

    }

    @Nullable
    private String IOO(String[] params) {
        try {
            String nickname = params[0];
            String username = params[1];
            String password = params[2];
            URL url = new URL("http://163.18.57.43/HEMSphp/signup.php"); //loginn.php 含回傳使用者 // login.php 不含回傳使用者
            //URL url = new URL("http://192.168.2.110/login.php");
            HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setDoInput(true);
            OutputStream outputStream = httpURLConnection.getOutputStream();
            BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
            String post_data = URLEncoder.encode("nickname", "UTF-8") + "=" + URLEncoder.encode(nickname, "UTF-8") + "&" +
                    URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8") + "&" +
                    URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");
            bufferedWriter.write(post_data);    //傳username、password給php
            bufferedWriter.flush();
            bufferedWriter.close();
            outputStream.close();
            InputStream inputStream = httpURLConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"iso-8859-1"));
            String result="";
            String line = "";
            if((line = bufferedReader.readLine()) != null) {
                result = line;
            } else {result = "failure";}




            bufferedReader.close();
            inputStream.close();
            httpURLConnection.disconnect();
            return result;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    //-------------------------------------------------
    public void SendIntent(){
        Intent it = new Intent();
        it.setClass(LoginActivity.this,MainActivity.class);
        startActivity(it);
        LoginActivity.this.finish();
    }

    //結束
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("確認視窗")
                    .setMessage("確定要結束應用程式嗎?")
                    .setIcon(R.drawable.icon)
                    .setPositiveButton("確定",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    finish();
                                }
                            })
                    .setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    // TODO Auto-generated method stub

                                }
                            }).show();
        }
        return true;
    }


}

