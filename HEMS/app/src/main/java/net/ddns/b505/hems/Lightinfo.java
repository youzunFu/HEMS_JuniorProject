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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightinfo);

        toolbarlight = (Toolbar) findViewById(R.id.ToolBarLight);
        toolbarlight.setTitle("　　　智　慧　燈　具");
        toolbarlight.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarlight);
        try {
            InitialComponents();  // InitialComponents()＆＆＆InitialLight()
            InitialLight1();
            InitialLight2();
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
        imageL1 = (ImageView) findViewById(R.id.ItemImageL1);
        imageL2 = (ImageView) findViewById(R.id.ItemImageL2);
        imageL3 = (ImageView) findViewById(R.id.ItemImageL3);
        imageL4 = (ImageView) findViewById(R.id.ItemImageL4);
        lightname1 = (TextView) findViewById(R.id.ItemNameL1);
        lightname2 = (TextView) findViewById(R.id.ItemNameL2);
        lightname3 = (TextView) findViewById(R.id.ItemNameL3);
        lightname4 = (TextView) findViewById(R.id.ItemNameL4);
        lampinfo1 = (TextView) findViewById(R.id.ItemInfoL1);
        lampinfo2 = (TextView) findViewById(R.id.ItemInfoL2);
        lampinfo3 = (TextView) findViewById(R.id.ItemInfoL3);
        lampinfo4 = (TextView) findViewById(R.id.ItemInfoL4);
        swL1 = (Switch) findViewById(R.id.ItemSwitchL1);
        swL2 = (Switch) findViewById(R.id.ItemSwitchL2);
        swL3 = (Switch) findViewById(R.id.ItemSwitchL3);
        swL4 = (Switch) findViewById(R.id.ItemSwitchL4);
        btn_scheduleL1 = (Button) findViewById(R.id.ItemButtonL1);
        btn_scheduleL2 = (Button) findViewById(R.id.ItemButtonL2);
        btn_scheduleL3 = (Button) findViewById(R.id.ItemButtonL3);
        btn_scheduleL4 = (Button) findViewById(R.id.ItemButtonL4);
        btn_historyL1 = (Button) findViewById(R.id.ItemButtonL5);
        btn_historyL2 = (Button) findViewById(R.id.ItemButtonL6);
        btn_historyL3 = (Button) findViewById(R.id.ItemButtonL7);
        btn_historyL4 = (Button) findViewById(R.id.ItemButtonL8);

/*
        InitialLight1();
        InitialLight2();
        InitialLight3();
        InitialLight4();
*/
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
    public void InitialLight1() throws ExecutionException, InterruptedException {
            lightname = "001";
            lightstatus  = "2"; //meaningless :Don't send wrong status to php
            ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
            lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
            String plugresultSplit[] =lightall.split(" ");
             lightstatus = plugresultSplit[1] ;
            switch (plugresultSplit[1]){
                case "0" :
                    imageL1.setImageResource(R.drawable.lightoff);
                    lampinfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    swL1.setChecked(false);
                    break;
                case "1" :
                    imageL1.setImageResource(R.drawable.lighton);
                    lampinfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    swL1.setChecked(true);
                    break;
                default:
                    break;
            }
    }
    public void InitialLight2() throws ExecutionException, InterruptedException {
        lightname = "002";
        lightstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
        String plugresultSplit[] =lightall.split(" ");
        lightstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL2.setImageResource(R.drawable.lightoff);
                lampinfo2.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL2.setChecked(false);
                break;
            case "1":
                imageL2.setImageResource(R.drawable.lighton);
                lampinfo2.setText("即時用電量 : " + plugresultSplit[4] + " W");
                swL2.setChecked(true);
                break;
            default:
                break;
        }
    }
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
    public void sendLightNumber1(View view){
        Intent intent = new Intent(Lightinfo.this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumber2(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumber3(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","003");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumber4(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","004");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //history chart
    public void sendLightNumberChart1(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendLightNumberChart2(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Light");
        intent.putExtras(bundle);
        startActivity(intent);
    }
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

    //btnSchedule1 set YYYY-MM-DD HH:mm:ss
    /*
    public void pick_DateTime1(View v){
        Date_Time = "";
        final Calendar c = Calendar.getInstance();
        int years = c.get(Calendar.YEAR);
        int months = c.get(Calendar.MONTH);
        int days = c.get(Calendar.DAY_OF_MONTH);
        int hours = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        new TimePickerDialog(Pluginfo.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int minute) {
                // if-else statement set calender data ex:2017-1-1 5:5  ---->2017-01-01 05:05:00 YYYY-MM-DD HH:mm:ss  Y-year M-month D-DAY H-hour m-minute s-second
                if(hour < 10){Date_Time = Date_Time + "0" +String.valueOf(hour)+ " ";}
                else{Date_Time = Date_Time + String.valueOf(hour)+ " ";}
                if(minute < 10){Date_Time = Date_Time + "0" +String.valueOf(minute)+ " ";}
                else{Date_Time = Date_Time + String.valueOf(minute)+ "  " ;}
                Toast.makeText(Pluginfo.this,"First Toast"+Date_Time,Toast.LENGTH_SHORT).show();
                PickDate_Time = Date_Time.split(" ");
                Date_Time = PickDate_Time[0] + "-" + PickDate_Time[1] + "-" + PickDate_Time[2] +
                        " " +PickDate_Time[3] +":"+ PickDate_Time[4] + ":" + "00";
                Toast.makeText(Pluginfo.this,"Secon Toast"+Date_Time,Toast.LENGTH_SHORT).show();
            }
        }, hours, minute,true).show();

        new DatePickerDialog(Pluginfo.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month +1 ;
                // if-else statement set calender data ex:2017-1-1 5:5  ---->2017-01-01 05:05:00 YYYY-MM-DD HH:mm:ss  Y-year M-month D-DAY H-hour m-minute s-second
                if(year < 10){Date_Time = Date_Time + "000" +String.valueOf(year)+ " ";}
                else{Date_Time = Date_Time + String.valueOf(year)+ " ";}
                if(month < 10){Date_Time = Date_Time + "0" +String.valueOf(month)+ " ";}
                else{Date_Time = Date_Time + String.valueOf(month)+ " ";}
                if(day < 10){Date_Time = Date_Time + "0" +String.valueOf(day)+ " ";}
                else{Date_Time = Date_Time + String.valueOf(day)+ " ";}

            }
        }, years, months, days).show();

    }
*/

    //PlugSwitchClick 1 2 3 4
    public void SwitchClick(){
        swL1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swL1.isChecked()) {
                    imageL1.setImageResource(R.drawable.lighton);
                    lightname = "001";
                    lightstatus = "1" ;
                    lightall = null;
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lightstatus = plugresultSplit[1] ;
                        //plugresultSplit[0]->name～plugresultSplit[1]->status～plugresultSplit[2]->～
                        lampinfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {

                        e.printStackTrace();
                    }
                } else {
                    lightname = "001";
                    lightstatus = "0" ;
                    lightall = null;
                    imageL1.setImageResource(R.drawable.lightoff);
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                            lampinfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        swL2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swL2.isChecked()) {
                    imageL2.setImageResource(R.drawable.lighton);
                    lightname2.setText("插座002");
                    lightname = "002";
                    lightstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo2.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lightname = "002";
                    lightstatus = "0" ;
                    imageL2.setImageResource(R.drawable.lightoff);
                    try {
                        ControlLightStatusAsynctask ctrlLightStatusAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        lightall = ctrlLightStatusAsynctask.execute(lightname,lightstatus).get().toString();
                        String plugresultSplit[] =lightall.split(" ");
                        lampinfo2.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        swL3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (swL3.isChecked()) {
                    imageL3.setImageResource(R.drawable.lighton);
                    lightname3.setText("插座003");
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
                    lightname4.setText("插座004");
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

            String login_url = "http://163.18.57.43/HEMSphp/lightopen.php";

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
                String post_data = URLEncoder.encode("lightname", "UTF-8") + "=" + URLEncoder.encode(lightname, "UTF-8")
                       +"&"+ URLEncoder.encode("lightstatus", "UTF-8") + "=" + URLEncoder.encode(lightstatus, "UTF-8");
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
