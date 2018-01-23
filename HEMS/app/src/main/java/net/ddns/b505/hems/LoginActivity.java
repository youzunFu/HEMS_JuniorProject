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
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

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

public class LoginActivity extends AppCompatActivity {
    private EditText Username, Password;
    String user = null;
    public static File file;
    private CheckBox memory,auto;
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
        setting = getSharedPreferences("auto",0);
        editor = pref.edit();
        Username = (EditText)findViewById(R.id.et_username);
        Password = (EditText)findViewById(R.id.et_password);
        memory = (CheckBox)findViewById(R.id.check_login);
        auto = (CheckBox)findViewById(R.id.check_auto);
        boolean autoisRemember = pref.getBoolean("auto_check",false);  //判斷LoginInfo內記錄的自動登入是否有被勾選
        //建立一個file
        file = new File("/data/data/net.ddns.b505.hems/shared_prefs","auto.xml");
        if(file.exists() && autoisRemember){ //如果自動登入有被勾選
            ReadValue();

            if(!user.equals("")){
                SendIntent();
            }

        }
        //查看app中是否已經儲存過帳號密碼，有的話直接顯示出來
        boolean memoryisRemember = pref.getBoolean("login_check",false);
        if(memoryisRemember) {
            if(autoisRemember) auto.setChecked(true);
            String user = pref.getString("UserName", "");
            String password = pref.getString("Password", "");
            Username.setText(user);
            Password.setText(password);
            memory.setChecked(true);
        }

    }

    public void OnLogin(View view) {  //登入
        String user = Username.getText().toString();
        String password = Password.getText().toString();
        //如果記住密碼被勾選，將帳號密碼儲存
        //没有被勾選，將儲存的帳號密碼清除
        if(auto.isChecked()) editor.putBoolean("auto_check",true);
        else editor.putBoolean("auto_check",false);

        if(memory.isChecked()){  //將帳號與Check
            editor.putBoolean("login_check",true);
            editor.putString("UserName",user);
            editor.putString("Password",password);
        }else{
            editor.clear();
        }
        //使用commit將添加的數據提交
        editor.commit();
        setting = getSharedPreferences("auto",0);
        setting.edit().putString("USER",user).commit();
        String type = "login";
        BackgroundWorker backgroundWorker = new BackgroundWorker(this);
        backgroundWorker.execute(user, password);
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
                Toast.makeText(LoginActivity.this, "Logon failure : unknown user name or bad password .", Toast.LENGTH_LONG).show();
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
            URL url = new URL("http://163.18.57.43/HEMSphp/login.php");
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
            String line = bufferedReader.readLine();
            if(line != null) {

                SendIntent();
                    /*Intent intent = new Intent(this, DataActivity.class);  //跳轉至DataActivity頁面
                    intent.putExtra("USER", user_name);  //將username的值傳至DaaActivity
                    startActivity(intent);
                    finish();*/
            }
            else result = "failure";
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
        setting = getSharedPreferences("auto",0);
        user = setting.getString("USER","");
    }

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