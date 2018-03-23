package net.ddns.b505.hems;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

public class PluginfoV2 extends AppCompatActivity {

    private ArrayList<String> data = new ArrayList<String>();
    private static String events[] = {"當前事件","未來事件","歷史事件"};

    private static final String TAG = "myTag";
    private RequestQueue requestQueue;
    //private static final String URLgetPlugInfo = "http://192.168.1.100/sl_demo_api/dr.php";
    private String URLgetPlugInfo = "http://163.18.57.43/HEMSphp/plugschedule.php";
    //private String getDR = "sl_demo_api/dr_parameter.php";
    private SharedPreferences setting;
    private SharedPreferences.Editor seteditor;
    private String token;
    private JsonArrayRequest request;
    private TextView txtWinningprice;
    private Timer timer;
    private TimerTask task;
    private int num = 0;
    private ListView lvProduct1;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluginfo_v2);

        requestQueue = Volley.newRequestQueue(this);

        setting = this.getSharedPreferences("auto",0);
        seteditor = setting.edit();

        token = "mDSbpZrRXACEsBE8WR34";

        lvProduct1 = (ListView) findViewById(R.id.lv_products);
        getDeviceInfo();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        datas.clear();
//        Log.d(TAG, "onDestroyView");
//        Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_LONG).show();
    }


    public void getDeviceInfo(){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getDeviceInfo");
        mJsonStr.setEqtype("plug");
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        Log.d("getPlugInfo111","getPlugInfo111");

        request = new JsonArrayRequest(Request.Method.POST, URLgetPlugInfo, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("response",response.toString());

                    try{
                        while(response.getJSONObject(num).getString("name") != null){
                            num++;
                        }
                        Log.d("num",""+num);
                    } catch (Exception  e) {
                        e.printStackTrace();
                    }

                    //Log.d(response.getString(0), "request");

//-----------------------------把回傳資料顯示在 個別控制的插座上 ------- 180227
                    for(int i = 0; i < num; i++){
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("power", response.getJSONObject(i).getString("power"));
                            item.put("status", response.getJSONObject(i).getString("status"));
                            item.put("name", response.getJSONObject(i).getString("name"));
                            datas.add(item);
                    }
                    PlugInfoAdapter adapter = new PlugInfoAdapter(PluginfoV2.this, datas);

                    lvProduct1.setAdapter(adapter);


                } catch (Exception  e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("error",""+error);
            }
        });
        requestQueue.add(request);
    }


}
