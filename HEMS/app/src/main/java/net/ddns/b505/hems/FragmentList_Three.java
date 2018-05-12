package net.ddns.b505.hems;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.transition.TransitionManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Solinari on 2016/12/31.
 */

public class FragmentList_Three extends Fragment {

    private ArrayList<String> data = new ArrayList<String>();
    private static String events[] = {"當前事件","未來事件","歷史事件"};

    private static final String TAG = "myTag";
    private TextView txtNow, txtFuture, txtHistory;
    private Button btn_drequipment;
    private LinearLayout LayoutNow, LayoutFuture, LayoutHistory;
    private TextView tv_now_date, tv_now_start, tv_now_stop, tv_now_p1, tv_now_time, tv_now_cost,
            tv_fu_date, tv_fu_start, tv_fu_stop, tv_fu_p1, tv_fu_time, tv_fu_cost;
    private RequestQueue requestQueue;
    //private static final String getDREvents = "http://192.168.1.100/sl_demo_api/dr.php";
    private String getDREvents = "http://163.18.57.43/HEMSphp/plugschedule.php";
    private String getDR = "sl_demo_api/dr_parameter.php";
    private SharedPreferences setting;
    private SharedPreferences.Editor seteditor;
    private String token, ip;
    private JsonArrayRequest request;
    private TextView txtWinningprice;
    private Timer timer;
    private TimerTask task;
    private int num = 0;
    private ListView lvProduct;
    private ArrayList<HashMap<String, String>> datas = new ArrayList<HashMap<String,String>>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragmentlist_three, container, false);


        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        requestQueue = Volley.newRequestQueue(this.getActivity());
        setting = this.getActivity().getSharedPreferences("auto",0);
        seteditor = setting.edit();
        token = "mDSbpZrRXACEsBE8WR34";

        /*
        ip = setting.getString("ip","");
        int Comparesurl = getDR.indexOf("//");
        if(Comparesurl == -1) {
            getDR = ip + getDR;
        }
        int Comparesurl2 = getDREvents.indexOf("//");
        if(Comparesurl2 == -1) {
            getDREvents = ip + getDREvents;
        }
        */
        txtWinningprice = (TextView) getView().findViewById(R.id.tv_Winningprice);
//        txtWinningprice.setSingleLine();
//        txtWinningprice.setText("最新得標價格：8.7 元/kWh");
//        txtWinningprice.setSelected(true);  //使跑馬燈持續動起來！ (x
        final ViewGroup LayoutDRevent = (ViewGroup) getView().findViewById(R.id.Layout_reaction);
        LayoutNow = (LinearLayout) LayoutDRevent.findViewById(R.id.layout_nowevent);
        LayoutFuture = (LinearLayout) LayoutDRevent.findViewById(R.id.layout_futureevent);
        LayoutHistory = (LinearLayout) LayoutDRevent.findViewById(R.id.layout_historyevent);
        txtNow = (TextView) LayoutDRevent.findViewById(R.id.tv_btn_nowevent);
        txtFuture = (TextView) LayoutDRevent.findViewById(R.id.tv_btn_futureevent);
        txtHistory = (TextView) LayoutDRevent.findViewById(R.id.tv_btn_historyevent);
        tv_now_date = (TextView)getView().findViewById(R.id.tv_now_date);
        tv_now_start = (TextView)getView().findViewById(R.id.tv_now_start);
        tv_now_stop = (TextView)getView().findViewById(R.id.tv_now_stop);
        tv_now_p1 = (TextView)getView().findViewById(R.id.tv_now_p);
        tv_now_time = (TextView)getView().findViewById(R.id.tv_now_time);
        //tv_now_cost = (TextView)getView().findViewById(R.id.tv_now_cost);
        tv_fu_date = (TextView)getView().findViewById(R.id.tv_future_date);
        tv_fu_start = (TextView)getView().findViewById(R.id.tv_future_start);
        tv_fu_stop = (TextView)getView().findViewById(R.id.tv_future_stop);
        tv_fu_time = (TextView)getView().findViewById(R.id.tv_future_time);
        btn_drequipment = (Button)getView().findViewById(R.id.btn_drequipment);
        lvProduct = (ListView) getView().findViewById(R.id.lv_products);

        btn_drequipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),DRset.class);
                startActivity(intent);
            }
        });


        txtNow.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(LayoutDRevent);
                visible = !visible;
                LayoutNow.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtFuture.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(LayoutDRevent);
                visible = !visible;
                LayoutFuture.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        txtHistory.setOnClickListener(new Button.OnClickListener(){
            boolean visible;
            @Override
            public void onClick(View v) {
                TransitionManager.beginDelayedTransition(LayoutDRevent);
                visible = !visible;
                LayoutHistory.setVisibility(visible ? View.VISIBLE : View.GONE);
            }
        });
        getDREvents();
        Log.d("getDREvents","getDREventsgetDREventsgetDREventsgetDREventsgetDREventsgetDREventsgetDREventsgetDREvents");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        datas.clear();
//        Log.d(TAG, "onDestroyView");
//        Toast.makeText(getActivity(), "onDestroyView", Toast.LENGTH_LONG).show();
    }

    public void getDREvents(){
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction("getDREvents");
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);

        request = new JsonArrayRequest(Request.Method.POST, getDREvents, json, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {
                    Log.d("DREvents response",response.toString());
                    try{

                        while(response.getJSONObject(num).getString("start_at") != null){
                            num++;

                        }

                        Log.d("num",""+num);

                    } catch (Exception  e) {
                        e.printStackTrace();
                    }

                    //Log.d(response.getString(0), "request");
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    formatter.setLenient(false);
                    Date now = new Date() ; // 獲取當前時間

                    for(int i = 0; i < num; i++){
                        Log.d("getDREvents","6");
                        Log.d("for i =",""+i);
                        String[] start = response.getJSONObject(i).getString("start_at").split(" ");
                        String[] end = response.getJSONObject(i).getString("end_at").split(" ");
                        Date dr_start = formatter.parse(response.getJSONObject(i).getString("start_at"));
                        Date dr_end = formatter.parse(response.getJSONObject(i).getString("end_at"));
                        //取得兩個時間的Unix時間
                        Long now1 = now.getTime();
                        Long dr_start1 = dr_start.getTime();
                        Long dr_end1 = dr_end.getTime();  //相減獲得兩個時間差距的毫秒
                        Long End_Now = dr_end1 - now1;  //毫秒差

                        if (End_Now > 0) {  //結束時間大於現在時間
                            Long Start_Now = dr_start1 - now1;  //毫秒差
                            if (Start_Now > 0) {  //開始時間大於現在時間，把事件歸類未來事件
                                tv_fu_date.setText(start[0]);
                                tv_fu_start.setText(start[1]);
                                tv_fu_stop.setText(end[1]);
                               // tv_fu_p1.setText(response.getJSONObject(i).getString("pre_p"));
                                tv_fu_time.setText(response.getJSONObject(i).getString("duration"));
                               // tv_fu_cost.setText(response.getJSONObject(i).getString("expected_reward"));
                            } else {  //開始時間小於現在時間，把事件歸類到當前事件
                                tv_now_date.setText(start[0]);
                                tv_now_start.setText(start[1]);
                                tv_now_stop.setText(end[1]);
                                tv_now_p1.setText(response.getJSONObject(i).getString("pre_p"));
                                tv_now_time.setText(response.getJSONObject(i).getString("duration"));
                                //tv_now_cost.setText(response.getJSONObject(i).getString("expected_reward"));
                            }
                        } else {  //結束時間小於現在時間，把事件歸類到歷史事件


                            String result = "";
                            if(response.getJSONObject(i).getString("result").equals("1")) result = "成功";
                            else if(response.getJSONObject(i).getString("result").equals("0")) result = "失敗";
                            HashMap<String, String> item = new HashMap<String, String>();
                            item.put("history_date", start[0]);
                            item.put("history_start", start[1]);
                            item.put("history_stop", end[1]);
                            item.put("history_p1", response.getJSONObject(i).getString("pre_p"));
                            item.put("history_time", response.getJSONObject(i).getString("duration"));
                            item.put("history_p2", response.getJSONObject(i).getString("actual_p"));
                            item.put("history_result", result);
                            item.put("history_cost", response.getJSONObject(i).getString("expected_reward"));
                            item.put("history_cost2", response.getJSONObject(i).getString("actual_reward"));
                           // item.put("history_p3", response.getJSONObject(i).getString("baseline"));
                            datas.add(item);
                        }

                    }
                    OneExpandAdapter adapter = new OneExpandAdapter(getActivity(), datas);

                    lvProduct.setAdapter(adapter);


                } catch (Exception  e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        requestQueue.add(request);
    }


}