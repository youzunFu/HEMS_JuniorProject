package net.ddns.b505.hems;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class MPchartActivity extends AppCompatActivity implements OnChartGestureListener{


    private static final String TAG = "myTag";
    private int mYear, mMonth, mDay;
    private EditText txtTime, txtdayTime;
    private SharedPreferences setting;
    private String user, token, PlugNum;
    private float[] p = new float[96], w = new float[96], sun = new float[96], temp1 = new float[96], temp2 = new float[96];
    private RequestQueue requestQueue;
    //private static final String ESS = "http://192.168.1.100/sl_demo_api/ess.php";
//    private String Plug = "sl_demo_api/plug.php";
    private String Plug = "http://163.18.57.43/HEMSphp/plugschedule.php";
    private JsonObjectRequest request;
    private LineChart powerChart;
    //    private BarChart wChart;

//implements OnChartGestureListener ,OnChartValueSelectedListener
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_history);

        setting = this.getSharedPreferences("auto",0);
       token = setting.getString("TOKEN","");
        token = "mDSbpZrRXACEsBE8WR34";
        /*
        String ip = setting.getString("ip","");
        int Comparesurl = Plug.indexOf("//");
        if(Comparesurl == -1) {
            Plug = ip + Plug;
        }
        */
       // Bundle bundle = getIntent().getExtras();
       // PlugNum = bundle.getString("PlugNum");
        Bundle bundle = getIntent().getExtras();
        PlugNum = bundle.getString("PlugNum");
        Toast.makeText(this, "插座編號"+PlugNum, Toast.LENGTH_LONG).show();

        powerChart = (LineChart) findViewById(R.id.chart_sch_power);
        txtTime = (EditText) findViewById(R.id.tv_w_time);
        TextView kwh = (TextView) findViewById(R.id.kwh);
        RotateAnimation ranim = (RotateAnimation) AnimationUtils.loadAnimation(this, R.anim.rotate_anim);
        ranim.setFillAfter(true); //For the button to remain at the same place after the rotation
        kwh.setAnimation(ranim);
        setLineChart(powerChart);
        requestQueue = Volley.newRequestQueue(this);
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // TODO Auto-generated method stub

        if (keyCode == KeyEvent.KEYCODE_BACK) { // 攔截返回鍵
            finish();
        }
        return true;
    }

    private void setLineChart(LineChart chart) {

        chart.setOnChartGestureListener(this);
//        chart.setScaleEnabled(true);
        chart.setDrawGridBackground(false); // 設置網格背景
        chart.setTouchEnabled(true);
        chart.setDoubleTapToZoomEnabled(true);

        chart.setScaleEnabled(true); // 設置縮放
        chart.setDoubleTapToZoomEnabled(true); // 設置雙擊進行縮放

        chart.setAutoScaleMinMaxEnabled(false);
        // 設置X軸
        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM); // 設置X軸的位置
        xAxis.setEnabled(true);
        xAxis.setSpaceBetweenLabels(6);
        xAxis.setDrawGridLines(false);

        xAxis.setSpaceBetweenLabels(1);
        Legend legend = chart.getLegend();
        legend.setTextSize(10f);

        legend.setFormSize(18f);
        legend.setForm(Legend.LegendForm.SQUARE);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setForm(Legend.LegendForm.LINE);
        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setValueFormatter(new MyYAxisValueFormatter());
        leftAxis.setTextSize(10f);
        leftAxis.setPosition(YAxis.YAxisLabelPosition.INSIDE_CHART);
        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
    }

    public void Dateset(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(MPchartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = setDateFormat(year,month,day);
                txtTime.setText(format);
                getPV("getPlugInformationInDay", year, month, day);
            }

        }, mYear,mMonth, mDay).show();
    }
    private String setDateFormat(int year,int monthOfYear,int dayOfMonth){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1) + "-"
                + String.valueOf(dayOfMonth);
    }
    //月份選擇
    public void Monthset(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(MPchartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = setMonthFormat(year,month);
                txtTime.setText(format);
                getPV("getPlugInformationInMonth", year, month, 0);
            }

        }, mYear,mMonth, mDay).show();
    }
    private String setMonthFormat(int year,int monthOfYear){
        return String.valueOf(year) + "-"
                + String.valueOf(monthOfYear + 1);
    }

    //年份選擇
    public void Yearset(View view) {
        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        new DatePickerDialog(MPchartActivity.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                String format = String.valueOf(year);
                txtTime.setText(format);
                getPV("getPlugInformationInYear", year, 0, 0);
            }

        }, mYear,mMonth, mDay).show();
    }

    private ArrayList<String> Time() {
        ArrayList<String> m = new ArrayList<String>();
        m.add("5");
        return m;
    }

    public void getPV(String button, int year, int month, int day) {
        JsonStr mJsonStr = new JsonStr();
        mJsonStr.setAction(button);
        mJsonStr.setName(PlugNum);
        switch (button){
            case "getPlugInformationInYear":
                mJsonStr.setYear(String.valueOf(year));
                break;
            case "getPlugInformationInMonth":
                mJsonStr.setYear(String.valueOf(year));
                mJsonStr.setMonth(String.valueOf(month+1));
                break;
            case "getPlugInformationInDay":
                mJsonStr.setYear(String.valueOf(year));
                mJsonStr.setMonth(String.valueOf(month+1));
                mJsonStr.setDay(String.valueOf(day));
                break;
        }
        mJsonStr.setToken(token);
        Gson gson = new Gson();
        String json = gson.toJson(mJsonStr);
        request = new JsonObjectRequest(Request.Method.POST, Plug, json, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("Response", response.toString());
                try {
                    int num = 0;
                    try{
                        while(response.getJSONArray("label").getString(num) != null){
                            num++;
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    String[] array = new String[num];
                    for (int i = 0;i < num;i++){
                        array[i] = response.getJSONArray("label").getString(i);
                    }
                    for (int i = 0;i < num;i++){
                        String power = response.getJSONArray("energy").getString(i);
                        w[i] = Float.parseFloat(power);
                    }
                    loadLineChartData(powerChart, "Power", num, w, array);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "回傳錯誤" , Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(request);
    }
    //put data to chart
    private void loadLineChartData(LineChart chart, String str, int datanumber, float[] data, String[] text) {
        //MyMarkerView mv = new MyMarkerView(this,R.layout.custom_marker_view_layout);
       // chart.setMarkerView(mv);
        ArrayList<LineDataSet> allLinesList = new ArrayList<LineDataSet>();

        ArrayList<Entry> entryList = new ArrayList<Entry>();
        for (int i = 0; i < datanumber; i++) {
            entryList.add(new Entry(data[i], i));
        }
        LineDataSet dataSet = new LineDataSet(entryList, str);
        dataSet.setLineWidth(2.5f);
        dataSet.setCircleSize(2.5f);
        dataSet.setColor(Color.GREEN);
        dataSet.setCircleColor(Color.GREEN);
        dataSet.setHighLightColor(Color.GRAY);
        dataSet.setDrawValues(false);
        dataSet.setValueTextColor(Color.RED);
        dataSet.setValueTextSize(15f);
        allLinesList.add(dataSet);
        LineData mChartData = new LineData(text, allLinesList);
        chart.setData((LineData) mChartData);

        // 設置動畫
        //chart.animateX(8000);
        chart.animateY(3000);

//        chart.setVisibleXRangeMaximum(3);
//        chart.setVisibleXRangeMinimum(17);
        chart.setVisibleXRangeMaximum(48);  //設置初始x軸資料筆
        chart.setVisibleYRangeMaximum(10000, YAxis.AxisDependency.LEFT);

        chart.moveViewToY(60,YAxis.AxisDependency.LEFT);
    }


    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {

    }

    @Override
    public void onChartLongPressed(MotionEvent me) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {

    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {

    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {

    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {

    }
}
