package net.ddns.b505.hems;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import net.ddns.b505.hems.AboutFragment.ControlAirAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlLightAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlPlugAboutFragment;

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
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import static net.ddns.b505.hems.R.id.start;

public class Lightinfo extends AppCompatActivity {
    private ImageView imageL1, imageL2, imageL3, imageL4;
    private Switch swL1, swL2, swL3, swL4;
    private TextView lightname1, lightname2, lightname3, lightname4, lampinfo1, lampinfo2, lampinfo3, lampinfo4;
    private Button btn_scheduleL1, btn_scheduleL2, btn_scheduleL3, btn_scheduleL4, btn_historyL1, btn_historyL2, btn_historyL3, btn_historyL4;

    public String lightname = null,pluginfo = null, lightstatus = null,lightall = null;
   //private String lightname = null,pluginfo = null,plugstatus = null,lightall = null;
    private CountDownTimer counterdowntimer ;
    private String PickDate_Time[] ,Date_Time;
    private Toolbar toolbarlight;
    private SwipeRefreshLayout mSwipeRefreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightinfo);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_lightinfo);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    InitialLight3();
                    InitialLight4();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        toolbarlight = (Toolbar) findViewById(R.id.ToolBarLight);
        toolbarlight.setTitle("　　　智　慧　燈　具");
        toolbarlight.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarlight);
        try {
            InitialComponents();  // InitialComponents()＆＆＆InitialLight()
            InitialLight3();
            InitialLight4();
        }
        catch (ExecutionException e) {e.printStackTrace(); }
        catch (InterruptedException e) {e.printStackTrace(); }
         SwitchClick();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.ItemLight) {
                FragmentTransaction ft3 = getFragmentManager().beginTransaction();
                android.app.Fragment prev3 = getFragmentManager().findFragmentByTag("ItemLight");
                if (prev3 != null) {
                    ft3.remove(prev3);
                }
                ft3.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment3 = new ControlLightAboutFragment();
                newFragment3.show(ft3, "ItemLight");

            return true;
        }else if(id == R.id.ItemLightExit){
            Intent intent = new Intent(Lightinfo.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_light, menu);
        return true;
    }

    //initial compents
    public void InitialComponents() throws ExecutionException, InterruptedException {
        imageL3 = (ImageView) findViewById(R.id.ItemImageL3);
        imageL4 = (ImageView) findViewById(R.id.ItemImageL4);
        lightname3 = (TextView) findViewById(R.id.ItemNameL3);
        lightname4 = (TextView) findViewById(R.id.ItemNameL4);
        lampinfo3 = (TextView) findViewById(R.id.ItemInfoL3);
        lampinfo4 = (TextView) findViewById(R.id.ItemInfoL4);
        swL3 = (Switch) findViewById(R.id.ItemSwitchL3);
        swL4 = (Switch) findViewById(R.id.ItemSwitchL4);
        btn_scheduleL3 = (Button) findViewById(R.id.ItemButtonL3);
        btn_scheduleL4 = (Button) findViewById(R.id.ItemButtonL4);
        btn_historyL3 = (Button) findViewById(R.id.ItemButtonL7);
        btn_historyL4 = (Button) findViewById(R.id.ItemButtonL8);

        InitialLight3();
        InitialLight4();
    }

/* 計時器
    private void start(){
      // timer1.setText("2");
        counterdowntimer = new CountDownTimer(2*1000,1000){
            @Override
            public void onTick(long l) {
                try {
                   InitialLight1();
                   InitialLight2();
                   InitialLight3();
                   InitialLight4();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //timer1.setText(""+l/1000);
            }

            @Override
            public void onFinish() {
               // timer1.setText("done");
                counterdowntimer.start();
            }
        };
        counterdowntimer.start();
    }
*/

    public void InitialLight3() throws ExecutionException, InterruptedException {
        lightname = "003";
        lightstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
        String plugresultSplit[] =lightall.split(" ");
        lightstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL3.setImageResource(R.drawable.lightoff);
                lampinfo3.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL3.setChecked(false);
                break;
            case "1":
                imageL3.setImageResource(R.drawable.lighton);
                lampinfo3.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL3.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void InitialLight4() throws ExecutionException, InterruptedException {
        lightname = "004";
        lightstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
        String plugresultSplit[] =lightall.split(" ");
        lightstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL4.setImageResource(R.drawable.lightoff);
                lampinfo4.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL4.setChecked(false);
                break;
            case "1":
                imageL4.setImageResource(R.drawable.lighton);
                lampinfo4.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL4.setChecked(true);
                break;
            default:
                break;
        }
    }

    //schedule
    public void sendLightNumber3(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","003");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumber4(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","004");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //history chart
    public void sendLightNumberChart3(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","003");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumberChart4(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","004");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //PlugSwitchClick 1 2 3 4
    public void SwitchClick(){
        swL3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swL3.isChecked()) {
                    imageL3.setImageResource(R.drawable.lighton);
                    //lightname3.setText("插座003");
                    lightname = "003";
                    lightstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo3.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lightname = "003";
                    lightstatus = "0" ;
                    imageL3.setImageResource(R.drawable.lightoff);
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo3.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        swL4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swL4.isChecked()) {
                    imageL4.setImageResource(R.drawable.lighton);
                   // lightname4.setText("插座004");
                    lightname = "004";
                    lightstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo4.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lightname = "004";
                    lightstatus = "0" ;
                    imageL4.setImageResource(R.drawable.lightoff);
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo4.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }



    public static class ControlLightStatusAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        ControlLightStatusAsynctask(Context ctx) {context = ctx ;}
        @Override
        protected String doInBackground(String... params) {

            String login_url = "http://163.18.57.43/HEMSphp/plugopen.php";

            try {
                String lightname = params[0];
                String lightstatus = params[1];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("plugname", "UTF-8") + "=" + URLEncoder.encode(lightname, "UTF-8")
                       +"&"+ URLEncoder.encode("plugstatus", "UTF-8") + "=" + URLEncoder.encode(lightstatus, "UTF-8");
                bufferedWriter.write(post_data);
                bufferedWriter.flush();
                bufferedWriter.close();
                outputStream.close();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "iso-8859-1"));
                String result="";
                String line = "";
                while ((line = bufferedReader.readLine()) != null) {
                    result = line;
                }
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
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
        @Override
        protected void onPostExecute(String result) {
//            Toast.makeText(context,String.valueOf(result),Toast.LENGTH_SHORT).show();
        }
        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }



    }





}
