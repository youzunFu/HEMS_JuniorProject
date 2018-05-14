package net.ddns.b505.hems;


import android.app.DialogFragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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


public class DrSetFragment extends DialogFragment {
    //private static final String Appliances = "http://192.168.1.100/sl_demo_api/appliances.php";
    private String Appliances = "sl_demo_api/appliances.php";
    private Switch swChe1_light1, swChe1_light2, swChe1_light3, swChe1_light4,
             swRec1_light1, swRec1_light2, swRec1_light3, swRec1_light4;
    private SharedPreferences setting;
    private TextView tvCtrl1total, tvCtrl1ac1, tvCtrl1light1, tvCtrl1light2, tvCtrl1light3, tvCtrl1light4;
    private LinearLayout Control1;
    private double Total1, Total2, Total3;
    private String token, check;
    private JsonArrayRequest requestArray;
    private JsonObjectRequest requestOb;
    private RequestQueue requestQueue;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        return inflater.inflate(R.layout.dr_set, container, false);
    }
    @Override
    public void onStart() {
        getDialog().getWindow().getAttributes().width=getResources().getDisplayMetrics().widthPixels;
        super.onStart();
    }
    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        token = setting.getString("TOKEN","");
        String ip = setting.getString("ip","");
        int Comparesurl = Appliances.indexOf("//");
        if(Comparesurl == -1) {
            Appliances = ip + Appliances;
        }
        findviewbyid();
        check = setting.getString("Controlnum","");

        final ViewGroup dr = (ViewGroup) getView().findViewById(R.id.Layout_dr_set);
        Control1 = (LinearLayout) dr.findViewById(R.id.control1);

        if(check.equals("1")){
            Control1.setVisibility(View.VISIBLE);
        }
        switch1();
        getAppliances();
    }
    public void findviewbyid() {
        tvCtrl1total = (TextView) getView().findViewById(R.id.tv_ctrl1_total);
        tvCtrl1light1 = (TextView) getView().findViewById(R.id.tv_Ctrl1_light1);
        tvCtrl1light2 = (TextView) getView().findViewById(R.id.tv_Ctrl1_light2);
        tvCtrl1light3 = (TextView) getView().findViewById(R.id.tv_Ctrl1_plug1);
        tvCtrl1light4 = (TextView) getView().findViewById(R.id.tv_Ctrl1_plug2);
        swChe1_light1 = (Switch) getView().findViewById(R.id.sw_Che1_light1);
        swChe1_light2 = (Switch) getView().findViewById(R.id.sw_Che1_light2);
        swChe1_light3 = (Switch) getView().findViewById(R.id.sw_Che1_plug1);
        swChe1_light4 = (Switch) getView().findViewById(R.id.sw_Che1_plug2);
        swRec1_light1 = (Switch) getView().findViewById(R.id.sw_Rec1_light1);
        swRec1_light2 = (Switch) getView().findViewById(R.id.sw_Rec1_light2);
        swRec1_light3 = (Switch) getView().findViewById(R.id.sw_Rec1_plug1);
        swRec1_light4 = (Switch) getView().findViewById(R.id.sw_Rec1_plug2);
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
                    setAppliances("setApplianceChecked","10","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","10","0");
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
                    setAppliances("setApplianceChecked","11","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","11","0");
                }
            }
        });
        swChe1_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light3.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","12","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","12","0");
                }
            }
        });
        swChe1_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                double i = Double.parseDouble(tvCtrl1light4.getText().toString());
                if (isChecked) {
                    Total1 += i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","13","1");
                } else {
                    Total1 -= i;
                    tvCtrl1total.setText(String.format("%.3f", Total1));
                    setAppliances("setApplianceChecked","13","0");
                }
            }
        });
        swRec1_light1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","10","1");
                } else {
                    setAppliances("setApplianceRecover","10","0");
                }
            }
        });
        swRec1_light2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","11","1");
                } else {
                    setAppliances("setApplianceRecover","11","0");
                }
            }
        });
        swRec1_light3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","12","1");
                } else {
                    setAppliances("setApplianceRecover","12","0");
                }
            }
        });
        swRec1_light4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView,
                                         boolean isChecked) {
                if (isChecked) {
                    setAppliances("setApplianceRecover","13","1");
                } else {
                    setAppliances("setApplianceRecover","13","0");
                }
            }
        });
    }

    public void getAppliances(){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getAppliances");
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestArray = new JsonArrayRequest(Request.Method.POST, Appliances, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    tvCtrl1ac1.setText(response.getJSONObject(0).getString("power"));
                    tvCtrl1light1.setText(response.getJSONObject(8).getString("power"));
                    tvCtrl1light2.setText(response.getJSONObject(9).getString("power"));
                    tvCtrl1light3.setText(response.getJSONObject(10).getString("power"));
                    tvCtrl1light4.setText(response.getJSONObject(11).getString("power"));

                    if(response.getJSONObject(8).getString("checked").equals("1"))
                    {swChe1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(9).getString("checked").equals("1"))
                    {swChe1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(10).getString("checked").equals("1"))
                    {swChe1_light3.setChecked(true);
                    }
                    if(response.getJSONObject(11).getString("checked").equals("1"))
                    {swChe1_light4.setChecked(true);
                    }

                    if(response.getJSONObject(8).getString("recover").equals("1"))
                    {swRec1_light1.setChecked(true);
                    }
                    if(response.getJSONObject(9).getString("recover").equals("1"))
                    {swRec1_light2.setChecked(true);
                    }
                    if(response.getJSONObject(10).getString("recover").equals("1"))
                    {swRec1_light3.setChecked(true);
                    }
                    if(response.getJSONObject(11).getString("recover").equals("1"))
                    {swRec1_light4.setChecked(true);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(requestArray);
    }
    public void setAppliances(String... params){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction(params[0]);
        mJsonStr.setToken(token);
        switch (params[0]){
            case "setApplianceChecked":
                mJsonStr.setId(params[1]);
                mJsonStr.setChecked(params[2]);
                break;
            case "setApplianceRecover":
                mJsonStr.setId(params[1]);
                mJsonStr.setRecover(params[2]);
                break;
        }
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        requestOb = new JsonObjectRequest(Request.Method.POST, Appliances, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
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