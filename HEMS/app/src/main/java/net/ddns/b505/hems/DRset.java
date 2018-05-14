package net.ddns.b505.hems;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

/**
 * Created by RED on 2018/2/4.
 */

public class DRset extends AppCompatActivity {
    private String url = "http://163.18.57.43/HEMSphp/plugschedule.php";
    //private String Appliances = "HEMSphp/plugschdule.php";
    private Switch swChe1_light1, swChe1_light2, swChe1_plug1, swChe1_plug2,
           swRec1_light1, swRec1_light2, swRec1_plug1, swRec1_plug2;
    private SharedPreferences setting;
    private TextView tvCtrl1total,tvCtrl1light1, tvCtrl1light2, tvCtrl1plug1, tvCtrl1plug2;
    private LinearLayout Control1;
    private double Total1;
    private String token, check;
    private JsonArrayRequest requestArray;
    private JsonObjectRequest requestOb;
    private RequestQueue requestQueue;
    private Toolbar toolbardrset;
    private SwipeRefreshLayout mSwipeRefreshLayout ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dr_set);

        requestQueue = Volley.newRequestQueue(this);
        setting = this.getSharedPreferences("auto",0);
        //token = setting.getString("TOKEN","");
        /*
        String ip = setting.getString("ip","");
        int Comparesurl = Appliances.indexOf("//");
        if(Comparesurl == -1) {
            Appliances = ip + Appliances;
        }
        */
        findviewbyid();
        check = setting.getString("Controlnum","");

        toolbardrset = (Toolbar) findViewById(R.id.ToolBarDRset);
        toolbardrset.setTitle("　　　　　電器即時用電量");
        toolbardrset.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbardrset);

        final ViewGroup dr = (ViewGroup) findViewById(R.id.Layout_dr_set);
        Control1 = (LinearLayout) dr.findViewById(R.id.control1);

        Control1.setVisibility(View.VISIBLE);

        if(check.equals("1")){
            Control1.setVisibility(View.VISIBLE);
        }
        switch1();
        getAppliances();


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_drset);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAppliances();
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        // 依照id判斷點了哪個項目並做相應事件
        if(id == R.id.ItemDRsetExit){
                Intent intent = new Intent(DRset.this, MainActivity.class);
                startActivity(intent);
                return true;

        }
        return false;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drset, menu);
        return true;
    }

    public void findviewbyid() {
        tvCtrl1total = (TextView) findViewById(R.id.tv_ctrl1_total);
        tvCtrl1light1 = (TextView) findViewById(R.id.tv_Ctrl1_light1);
        tvCtrl1light2 = (TextView) findViewById(R.id.tv_Ctrl1_light2);
        tvCtrl1plug1 = (TextView) findViewById(R.id.tv_Ctrl1_plug1);
        tvCtrl1plug2 = (TextView) findViewById(R.id.tv_Ctrl1_plug2);
        swChe1_light1 = (Switch) findViewById(R.id.sw_Che1_light1);
        swChe1_light2 = (Switch) findViewById(R.id.sw_Che1_light2);
        swChe1_plug1 = (Switch) findViewById(R.id.sw_Che1_plug1);
        swChe1_plug2 = (Switch) findViewById(R.id.sw_Che1_plug2);
        swRec1_light1 = (Switch) findViewById(R.id.sw_Rec1_light1);
        swRec1_light2 = (Switch) findViewById(R.id.sw_Rec1_light2);
        swRec1_plug1 = (Switch) findViewById(R.id.sw_Rec1_plug1);
        swRec1_plug2 = (Switch) findViewById(R.id.sw_Rec1_plug2);
    }
    public void switch1() {
        swChe1_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light1.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","003","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","003","0");
                }
            }
        });
        swChe1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light2.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","004","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","004","0");
                }
            }
        });
        swChe1_plug1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1plug1.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","001","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","001","0");
                }
            }
        });
        swChe1_plug2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1plug2.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","002","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","002","0");
                }
            }
        });
        swRec1_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","003","1");
                } else {
                    setAppliances("setApplianceRecover","003","0");
                }
            }
        });
        swRec1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","004","1");
                } else {
                    setAppliances("setApplianceRecover","004","0");
                }
            }
        });
        swRec1_plug1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","001","1");
                } else {
                    setAppliances("setApplianceRecover","001","0");
                }
            }
        });
        swRec1_plug2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","002","1");
                } else {
                    setAppliances("setApplianceRecover","002","0");
                }
            }
        });
    }

    public void getAppliances(){
        Log.d("Response","getAppliances");
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getAppliances");
        mJsonStr.setToken("mDSbpZrRXACEsBE8WR34");
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        Log.d("Response","getAppliances1");
        requestArray = new JsonArrayRequest(Request.Method.POST, url, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.d("Response","2");
                try {

                    Log.d("Response",response.toString());

                   // tvCtrl1ac1.setText(response.getJSONObject(8).getString("power"));
                    tvCtrl1light1.setText(response.getJSONObject(2).getString("power"));
                    tvCtrl1light2.setText(response.getJSONObject(3).getString("power"));
                    tvCtrl1plug1.setText(response.getJSONObject(0).getString("power"));
                    tvCtrl1plug2.setText(response.getJSONObject(1).getString("power"));

                   // if(response.getJSONObject(8).getString("checked").equals("1"))
                   // {swChe1_ac1.setChecked(true);}
                    if(response.getJSONObject(2).getString("checked").equals("1"))
                    {swChe1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(3).getString("checked").equals("1"))
                    {swChe1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(0).getString("checked").equals("1"))
                    {swChe1_plug1.setChecked(true);}
                    if(response.getJSONObject(1).getString("checked").equals("1"))
                    {swChe1_plug2.setChecked(true);}


                   // if(response.getJSONObject(8).getString("recover").equals("1"))
                  //  {swRec1_ac1.setChecked(true);}

                    if(response.getJSONObject(2).getString("recover").equals("1"))
                    {swRec1_light1.setChecked(true);}
                    if(response.getJSONObject(3).getString("recover").equals("1"))
                    {swRec1_light2.setChecked(true);}
                    if(response.getJSONObject(0).getString("recover").equals("1"))
                    {swRec1_plug1.setChecked(true);}
                    if(response.getJSONObject(1).getString("recover").equals("1"))
                    {swRec1_plug2.setChecked(true);}



                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d("JSONException",e.toString());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("VolleyError",error.toString());
            }
        });
        requestQueue.add(requestArray);
    }

    public void setAppliances(String... params){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction(params[0]);
        mJsonStr.setToken("mDSbpZrRXACEsBE8WR34");
        switch (params[0]){
            case "setApplianceChecked":
                mJsonStr.setName(params[1]);
                mJsonStr.setChecked(params[2]);
                break;
            case "setApplianceRecover":
                mJsonStr.setName(params[1]);
                mJsonStr.setRecover(params[2]);
                break;
        }
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestOb = new JsonObjectRequest(Request.Method.POST, url, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response_setAppliances",response.toString());
                /*try {
                    Toast.makeText(getActivity(),"Result： "+response.getString("result"),Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Error" , Toast.LENGTH_SHORT).show();
                }*/
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });

        requestQueue.add(requestOb);
    }



}
