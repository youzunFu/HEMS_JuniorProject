package net.ddns.b505.hems;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

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
import java.util.concurrent.ExecutionException;

public class Lightinfo extends Activity {
    private ImageView imageL1, imageL2, imageL3, imageL4;
    private Switch swL1, swL2, swL3, swL4;
    private TextView lampname1, lampname2, lampname3, lampname4, lampinfo1, lampinfo2, lampinfo3, lampinfo4;
    private Button btn_scheduleL1, btn_scheduleL2, btn_scheduleL3, btn_scheduleL4, btn_historyL1, btn_historyL2, btn_historyL3, btn_historyL4;

    public String lampname = null,pluginfo = null,plugstatus = null,plugall = null;
   //private String lampname = null,pluginfo = null,plugstatus = null,plugall = null;
    private CountDownTimer counterdowntimer ;
    private String PickDate_Time[] ,Date_Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lightinfo);
        try {
            InitialComponents();  // InitialComponents()＆＆＆InitialPlug()
            start();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
         SwitchClick();
    }



    //initial compents
    public void InitialComponents() throws ExecutionException, InterruptedException {
        imageL1 = (ImageView) findViewById(R.id.ItemImageL1);
        imageL2 = (ImageView) findViewById(R.id.ItemImageL2);
        imageL3 = (ImageView) findViewById(R.id.ItemImageL3);
        imageL4 = (ImageView) findViewById(R.id.ItemImageL4);
        lampname1 = (TextView) findViewById(R.id.ItemNameL1);
        lampname2 = (TextView) findViewById(R.id.ItemNameL2);
        lampname3 = (TextView) findViewById(R.id.ItemNameL3);
        lampname4 = (TextView) findViewById(R.id.ItemNameL4);
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

        InitialPlug1();
        InitialPlug2();
        InitialPlug3();
        InitialPlug4();
    }

    private void start(){
      // timer1.setText("2");
        counterdowntimer = new CountDownTimer(2*1000,1000){
            @Override
            public void onTick(long l) {
                try {
                   InitialPlug1();
                   InitialPlug2();
                   InitialPlug3();
                   InitialPlug4();
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


    public void InitialPlug1() throws ExecutionException, InterruptedException {
            lampname = "001";
            plugstatus = "2"; //meaningless :Don't send wrong status to php
            ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
            plugall = ctrlPlugnameAsynctask .execute(lampname,plugstatus).get().toString();
            String plugresultSplit[] =plugall.split(" ");
            plugstatus = plugresultSplit[1] ;
            switch (plugresultSplit[1]){
                case "0" :
                    imageL1.setImageResource(R.drawable.lampoff);
                    lampinfo1.setText("Power: " + plugresultSplit[4] +" W");
                    swL1.setChecked(false);
                    break;
                case "1" :
                    imageL1.setImageResource(R.drawable.lampon);
                    lampinfo1.setText("Power: " + plugresultSplit[4] +" W");
                    swL1.setChecked(true);
                    break;
                default:
                    break;
            }
    }
    public void InitialPlug2() throws ExecutionException, InterruptedException {
        lampname = "002";
        plugstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        plugall = ctrlPlugnameAsynctask .execute(lampname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL2.setImageResource(R.drawable.lampoff);
                lampinfo2.setText("Power: " + plugresultSplit[4] + " W");
                swL2.setChecked(false);
                break;
            case "1":
                imageL2.setImageResource(R.drawable.lampon);
                lampinfo2.setText("Power: " + plugresultSplit[4] + " W");
                swL2.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void InitialPlug3() throws ExecutionException, InterruptedException {
        lampname = "003";
        plugstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        plugall = ctrlPlugnameAsynctask .execute(lampname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL3.setImageResource(R.drawable.lampoff);
                lampinfo3.setText("Power: " + plugresultSplit[4] + " W");
                swL3.setChecked(false);
                break;
            case "1":
                imageL3.setImageResource(R.drawable.lampon);
                lampinfo3.setText("Power: " + plugresultSplit[4] + " W");
                swL3.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void InitialPlug4() throws ExecutionException, InterruptedException {
        lampname = "004";
        plugstatus = "2"; //meaningless :Don't send wrong status to php
        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
        plugall = ctrlPlugnameAsynctask .execute(lampname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                imageL4.setImageResource(R.drawable.lampoff);
                lampinfo4.setText("Power: " + plugresultSplit[4] + " W");
                swL4.setChecked(false);
                break;
            case "1":
                imageL4.setImageResource(R.drawable.lampon);
                lampinfo4.setText("Power: " + plugresultSplit[4] + " W");
                swL4.setChecked(true);
                break;
            default:
                break;
        }
    }

    //schedule
    public void sendPlugNumber1(View view){
        Intent intent = new Intent(Lightinfo.this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","001");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumber2(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","002");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumber3(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","003");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumber4(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","004");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //history chart
    public void sendPlugNumberChart1(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","001");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumberChart2(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","002");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumberChart3(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","003");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumberChart4(View view ){
        Intent intent = new Intent(Lightinfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("PlugNum","004");
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
                    imageL1.setImageResource(R.drawable.lampon);
                    lampname = "001";
                    plugstatus = "1" ;
                    plugall = null;
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        plugstatus = plugresultSplit[1] ;
                        //plugresultSplit[0]->name～plugresultSplit[1]->status～plugresultSplit[2]->～
                        lampinfo1.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {

                        e.printStackTrace();
                    }
                } else {
                    lampname = "001";
                    plugstatus = "0" ;
                    plugall = null;
                    imageL1.setImageResource(R.drawable.lampoff);
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                            lampinfo1.setText("Power: " + plugresultSplit[4] +" W");
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
                    imageL2.setImageResource(R.drawable.lampon);
                    lampname2.setText("插座002");
                    lampname = "002";
                    plugstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo2.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lampname = "002";
                    plugstatus = "0" ;
                    imageL2.setImageResource(R.drawable.lampoff);
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo2.setText("Power: " + plugresultSplit[4] +" W");
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
                    imageL3.setImageResource(R.drawable.lampon);
                    lampname3.setText("插座003");
                    lampname = "003";
                    plugstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo3.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lampname = "003";
                    plugstatus = "0" ;
                    imageL3.setImageResource(R.drawable.lampoff);
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo3.setText("Power: " + plugresultSplit[4]);
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
                    imageL4.setImageResource(R.drawable.lampon);
                    lampname4.setText("插座004");
                    lampname = "004";
                    plugstatus = "1" ;
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo4.setText("Power: " + plugresultSplit[4]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    lampname = "004";
                    plugstatus = "0" ;
                    imageL4.setImageResource(R.drawable.lampoff);
                    try {
                        ControlLightStatusAsynctask ctrlPlugnameAsynctask = new ControlLightStatusAsynctask(Lightinfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(lampname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        lampinfo4.setText("Power: " + plugresultSplit[4]);
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
                String plugname = params[0];
                String plugstatus= params[1];

                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("plugname", "UTF-8") + "=" + URLEncoder.encode(plugname, "UTF-8")
                       +"&"+ URLEncoder.encode("plugstatus", "UTF-8") + "=" + URLEncoder.encode(plugstatus, "UTF-8");
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
