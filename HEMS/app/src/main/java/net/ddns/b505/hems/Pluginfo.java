package net.ddns.b505.hems;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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

public class Pluginfo extends Activity {
    private ImageView image1,image2,image3,image4;
    private Switch sw1,sw2,sw3,sw4;
    private TextView plugname1,plugname2,plugname3,plugname4,pluginfo1,pluginfo2,pluginfo3,pluginfo4;
    private Button btn_schedule1,btn_schedule2,btn_schedule3,btn_schedule4;

    public String plugname = null,pluginfo = null,plugstatus = null,plugall = null;
    private CountDownTimer counterdowntimer ;
    private String PickDate_Time[] ,Date_Time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluginfo);
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

    //initial compents
    public void InitialComponents() throws ExecutionException, InterruptedException {
        image1 = (ImageView) findViewById(R.id.ItemImage1);
        image2 = (ImageView) findViewById(R.id.ItemImage2);
        image3 = (ImageView) findViewById(R.id.ItemImage3);
        image4 = (ImageView) findViewById(R.id.ItemImage4);
        plugname1 = (TextView) findViewById(R.id.ItemName1);
        plugname2 = (TextView) findViewById(R.id.ItemName2);
        plugname3 = (TextView) findViewById(R.id.ItemName3);
        plugname4 = (TextView) findViewById(R.id.ItemName4);
        pluginfo1 = (TextView) findViewById(R.id.ItemInfo1);
        pluginfo2 = (TextView) findViewById(R.id.ItemInfo2);
        pluginfo3 = (TextView) findViewById(R.id.ItemInfo3);
        pluginfo4 = (TextView) findViewById(R.id.ItemInfo4);
        sw1 = (Switch) findViewById(R.id.ItemSwitch1);
        sw2 = (Switch) findViewById(R.id.ItemSwitch2);
        sw3 = (Switch) findViewById(R.id.ItemSwitch3);
        sw4 = (Switch) findViewById(R.id.ItemSwitch4);
        btn_schedule1 = (Button) findViewById(R.id.ItemButton1);
        btn_schedule2 = (Button) findViewById(R.id.ItemButton2);
        btn_schedule3 = (Button) findViewById(R.id.ItemButton3);
        btn_schedule4 = (Button) findViewById(R.id.ItemButton4);

        InitialPlug1();
        InitialPlug2();
        InitialPlug3();
        InitialPlug4();
    }
    public void InitialPlug1() throws ExecutionException, InterruptedException {
            plugname = "001";
            plugstatus = "2";
            ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
            plugall = ctrlPlugnameAsynctask .execute(plugname,plugstatus).get().toString();
            String plugresultSplit[] =plugall.split(" ");
            plugstatus = plugresultSplit[1] ;
            switch (plugresultSplit[1]){
                case "0" :
                    image1.setImageResource(R.drawable.plugoff25);
                    pluginfo1.setText("Power: " + plugresultSplit[4] +" W");
                    sw1.setChecked(false);
                    break;
                case "1" :
                    image1.setImageResource(R.drawable.plugon25);
                    pluginfo1.setText("Power: " + plugresultSplit[4] +" W");
                    sw1.setChecked(true);
                    break;
                default:
                    break;
            }
    }
    public void InitialPlug2() throws ExecutionException, InterruptedException {
        plugname = "002";
        plugstatus = "2";
        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
        plugall = ctrlPlugnameAsynctask .execute(plugname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                image2.setImageResource(R.drawable.plugoff25);
                pluginfo2.setText("Power: " + plugresultSplit[4] + " W");
                sw2.setChecked(false);
                break;
            case "1":
                image2.setImageResource(R.drawable.plugon25);
                pluginfo2.setText("Power: " + plugresultSplit[4] + " W");
                sw2.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void InitialPlug3() throws ExecutionException, InterruptedException {
        plugname = "003";
        plugstatus = "2";
        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
        plugall = ctrlPlugnameAsynctask .execute(plugname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                image3.setImageResource(R.drawable.plugoff25);
                pluginfo3.setText("Power: " + plugresultSplit[4] + " W");
                sw3.setChecked(false);
                break;
            case "1":
                image3.setImageResource(R.drawable.plugon25);
                pluginfo3.setText("Power: " + plugresultSplit[4] + " W");
                sw3.setChecked(true);
                break;
            default:
                break;
        }
    }
    public void InitialPlug4() throws ExecutionException, InterruptedException {
        plugname = "004";
        plugstatus = "2";
        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
        plugall = ctrlPlugnameAsynctask .execute(plugname,plugstatus).get().toString();
        String plugresultSplit[] =plugall.split(" ");
        plugstatus = plugresultSplit[1];
        switch (plugresultSplit[1]) {
            case "0":
                image4.setImageResource(R.drawable.plugoff25);
                pluginfo4.setText("Power: " + plugresultSplit[4] + " W");
                sw4.setChecked(false);
                break;
            case "1":
                image4.setImageResource(R.drawable.plugon25);
                pluginfo4.setText("Power: " + plugresultSplit[4] + " W");
                sw4.setChecked(true);
                break;
            default:
                break;
        }
    }

    public void sendPlugNumber1(View view){
        Intent intent = new Intent(Pluginfo.this,PlugSetActivity.class);
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
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw1.isChecked()) {
                    image1.setImageResource(R.drawable.plugon25);
                    plugname = "001";
                    plugstatus = "1" ;
                    plugall = null;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        plugstatus = plugresultSplit[1] ;
                        //plugresultSplit[0]->name～plugresultSplit[1]->status～plugresultSplit[2]->～
                        pluginfo1.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {

                        e.printStackTrace();
                    }
                } else {
                    plugname = "001";
                    plugstatus = "0" ;
                    plugall = null;
                    image1.setImageResource(R.drawable.plugoff25);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                            pluginfo1.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        sw2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw2.isChecked()) {
                    image2.setImageResource(R.drawable.plugon25);
                    plugname2.setText("插座002");
                    plugname = "002";
                    plugstatus = "1" ;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo2.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    plugname = "002";
                    plugstatus = "0" ;
                    image2.setImageResource(R.drawable.plugoff25);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo2.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

        sw3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw3.isChecked()) {
                    image3.setImageResource(R.drawable.plugon25);
                    plugname3.setText("插座003");
                    plugname = "003";
                    plugstatus = "1" ;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo3.setText("Power: " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    plugname = "003";
                    plugstatus = "0" ;
                    image3.setImageResource(R.drawable.plugoff25);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo3.setText("Power: " + plugresultSplit[4]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });


        sw4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw4.isChecked()) {
                    image4.setImageResource(R.drawable.plugon25);
                    plugname4.setText("插座004");
                    plugname = "004";
                    plugstatus = "1" ;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo4.setText("Power: " + plugresultSplit[4]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    plugname = "004";
                    plugstatus = "0" ;
                    image4.setImageResource(R.drawable.plugoff25);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo4.setText("Power: " + plugresultSplit[4]);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }

                }
            }
        });

    }

    public static class ControlPlugStatusAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        ControlPlugStatusAsynctask (Context ctx) {context = ctx ;}
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
