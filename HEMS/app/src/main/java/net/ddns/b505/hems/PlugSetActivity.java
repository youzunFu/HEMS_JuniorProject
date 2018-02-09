package net.ddns.b505.hems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import net.ddns.b505.hems.JsonStr;
import net.ddns.b505.hems.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class PlugSetActivity extends AppCompatActivity {
    private String url = "http://163.18.57.43/HEMSphp/plugschedule.php";
    private JsonArrayRequest ArrayRequest;
    private JsonObjectRequest ObjectRequest;
    private RequestQueue requestQueue;
    private int mYear, mMonth, mDay;
    private SharedPreferences setting;
    private String user, token, PlugNum,Equipment;
    private EditText edstart, edend;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog sttimePickerDialog, endtimePickerDialog;
    private TextView txtRemind;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //進入含有EditText的Activity時，不自動彈出虛擬鍵盤
        requestQueue = Volley.newRequestQueue(this);
       // super.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_plug_set);

        setting = this.getSharedPreferences("auto",0);
        user = setting.getString("USER","");
        token = "mDSbpZrRXACEsBE8WR34";
        //token = setting.getString("TOKEN","");
       /*
        String ip = setting.getString("ip","");
        int Comparesurl2 = Plug.indexOf("//");
        if(Comparesurl2 == -1) {
            Plug = ip + Plug;
        }
        */

        edstart = (EditText) findViewById(R.id.ed_starttime);
        edend = (EditText) findViewById(R.id.ed_endtime);
        txtRemind = (TextView) findViewById(R.id.tv_remind);
        Bundle bundle = getIntent().getExtras();
        PlugNum = bundle.getString("Num");
        Equipment = bundle.getString("Equipment");
        Toast.makeText(this, "插座編號"+PlugNum, Toast.LENGTH_LONG).show();
        GregorianCalendar calendar = new GregorianCalendar();

        // 實作DatePickerDialog的onDateSet方法，設定日期後將所設定的日期show在textDate上
        /*datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            //將設定的日期顯示出來
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                textDate.setText(year + "/" + monthOfYear + "/" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));*/
        // 實作TimePickerDialog的onTimeSet方法，設定時間後將所設定的時間show在textTime上
        sttimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String date = edstart.getText().toString();
                edstart.setText(date + " " + hourOfDay
                        + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                false);

        endtimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            //將時間轉為12小時製顯示出來
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String date = edend.getText().toString();
                edend.setText(date + " " + hourOfDay
                        + ":" + minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(calendar.MINUTE),
                false);
//        new PlugSetActivity.Select().execute("http://192.168.1.100/sl_demo_api/plugscheduleView.php");
        getPlug(PlugNum);


    }
    //ActionBar返回鍵功能
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //actionbar的左侧图标的点击事件处理
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            this.finish();
        }
        return true;
    }

    public void Saveset(View view) {
        String start = edstart.getText().toString();
        String end = edend.getText().toString();
        setPlugSchedule(PlugNum, "1", start, end);
//        new PlugSetActivity.Save().execute("http://192.168.1.100/sl_demo_api/plugschedule.php", "1", start, end);
        finish();
    }
    public void StartTimeset(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(PlugSetActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = setDateFormat(year,month,day);
                edstart.setText(format);
                sttimePickerDialog.show();
            }

        }, mYear,mMonth, mDay).show();
    }
    public void EndTimeset(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(PlugSetActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = setDateFormat(year,month,day);
                edend.setText(format);
                endtimePickerDialog.show();
            }

        }, mYear,mMonth, mDay).show();
    }

    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }
    public void close(View view){
        String start = edstart.getText().toString();
        String end = edend.getText().toString();
        setPlugSchedule(PlugNum, "0", start, end);
//        new PlugSetActivity.Save().execute("http://192.168.1.100/sl_demo_api/plugschedule.php", "0", "", "");
        finish();
    }

    public void getPlug(final String name){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getPlug");
        mJsonStr.setName(name);
        mJsonStr.setToken(token);
        mJsonStr.setEquipment(Equipment);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        ArrayRequest = new JsonArrayRequest(Request.Method.POST, url/*"http://163.18.57.43/HEMSphp/plugschedule.php"*/, json,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        try {
                            Log.d(name, response.getJSONObject(0).getString("now"));
                            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                            formatter.setLenient(false);
                            switch (name) {
                                case "001":
                                    if(response.getJSONObject(0).getString("schedule").equals("1")) {
                                        txtRemind.setText("排程狀態 ： 開啟");
                                        edstart.setText(response.getJSONObject(0).getString("start"));
                                        edend.setText(response.getJSONObject(0).getString("end"));
                                    }
                                    break;
                                case "002":
                                    if(response.getJSONObject(0).getString("schedule").equals("1")) {
                                        txtRemind.setText("排程狀態 ： 開啟");
                                        edstart.setText(response.getJSONObject(0).getString("start"));
                                        edend.setText(response.getJSONObject(0).getString("end"));
                                    }
                                    break;
                                case "003":
                                    if(response.getJSONObject(0).getString("schedule").equals("1")) {
                                        txtRemind.setText("排程狀態 ： 開啟");
                                        edstart.setText(response.getJSONObject(0).getString("start"));
                                        edend.setText(response.getJSONObject(0).getString("end"));
                                    }
                                    break;
                                case "004":
                                    if(response.getJSONObject(0).getString("schedule").equals("1")) {
                                        txtRemind.setText("排程狀態 ： 開啟");
                                        edstart.setText(response.getJSONObject(0).getString("start"));
                                        edend.setText(response.getJSONObject(0).getString("end"));
                                    }
                                    break;
                            }
                            Date now = new Date() ; // 獲取當前時間

                            Date plugresult = formatter.parse(response.getJSONObject(0).getString("now"));
                            //取得兩個時間的Unix時間
                            Long now_gettime = now.getTime();
                            Long plugresult_gettime = plugresult.getTime();
                            Long Now_plugresult = now_gettime - plugresult_gettime;  //毫秒差

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(ArrayRequest);
    }

    public void setPlugSchedule(String... params) {
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("setPlugSchedule");
        mJsonStr.setToken(token);
        mJsonStr.setEquipment(Equipment);
        mJsonStr.setName(params[0]);
        mJsonStr.setSchedule(params[1]);
        mJsonStr.setStart(params[2]);
        mJsonStr.setEnd(params[3]);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        ObjectRequest = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(PlugSetActivity.this,"Result： "+response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(PlugSetActivity.this, "Error" , Toast.LENGTH_SHORT).show();
                    Log.d("JSONException....", String.valueOf(e));
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(ObjectRequest);
    }


}
