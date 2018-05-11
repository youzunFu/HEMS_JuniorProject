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

public class Pluginfo extends AppCompatActivity {
    private ImageView image1,image2,image3,image4;
    private Switch sw1,sw2,sw3,sw4;
    private TextView plugname1,plugname2,pluginfo1,pluginfo2,p;
    private Button btn_schedule1,btn_schedule2,btn_history1,btn_history2;

    public String plugname = null,pluginfo = null,plugstatus = null,plugall = null;
    private CountDownTimer counterdowntimer ;
   // private String PickDate_Time[] ,Date_Time;
    private Toolbar toolbarplug;
    private SwipeRefreshLayout mSwipeRefreshLayout ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pluginfo);


        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_pluginfo);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {
                    InitialPlug1();
                    InitialPlug2();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });

        toolbarplug = (Toolbar) findViewById(R.id.ToolBarPlug);
        toolbarplug.setTitle("　　　智　慧　插　座");
        toolbarplug.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarplug);


        try {
            InitialComponents();  // InitialComponents()＆＆＆InitialPlug()
            InitialPlug1();
            InitialPlug2();
            //start();
            //InitialPlug3();
            //InitialPlug4();
        }
        catch (ExecutionException e) {e.printStackTrace(); }
        catch (InterruptedException e) {e.printStackTrace(); }
        SwitchClick();
    }
/* 計時器
   private void start(){
      // timer1.setText("2");
        counterdowntimer = new CountDownTimer(5*1000,1000){
            @Override
            public void onTick(long l) {
                try {
                   InitialPlug1();
                   InitialPlug2();
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.ItemPlug) {
                FragmentTransaction ft2 = getFragmentManager().beginTransaction();
                android.app.Fragment prev2 = getFragmentManager().findFragmentByTag("ItemPlug");
                if (prev2 != null) {
                    ft2.remove(prev2);
                }
                ft2.addToBackStack(null);
                // Create and show the dialog.
                DialogFragment newFragment2 = new ControlPlugAboutFragment();
                newFragment2.show(ft2, "ItemPlug");

            return true;
        }else if(id == R.id.ItemPlugExit){
            Intent intent = new Intent(Pluginfo.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_plug, menu);
        return true;
    }

    //initial compents
    public void InitialComponents() throws ExecutionException, InterruptedException {
        image1 = (ImageView) findViewById(R.id.ItemImage1);
        image2 = (ImageView) findViewById(R.id.ItemImage2);
        plugname1 = (TextView) findViewById(R.id.ItemName1);
        plugname2 = (TextView) findViewById(R.id.ItemName2);
        pluginfo1 = (TextView) findViewById(R.id.ItemInfo1);
        pluginfo2 = (TextView) findViewById(R.id.ItemInfo2);
        sw1 = (Switch) findViewById(R.id.ItemSwitch1);
        sw2 = (Switch) findViewById(R.id.ItemSwitch2);
        btn_schedule1 = (Button) findViewById(R.id.ItemButton1);
        btn_schedule2 = (Button) findViewById(R.id.ItemButton2);
        btn_history1 = (Button) findViewById(R.id.ItemButton5);
        btn_history2 = (Button) findViewById(R.id.ItemButton6);


        InitialPlug1();
        InitialPlug2();


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
                    image1.setImageResource(R.drawable.icon_plugoff);
                    pluginfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    sw1.setChecked(false);
                    break;
                case "1" :
                    image1.setImageResource(R.drawable.icon_plugon);
                    pluginfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
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
                image2.setImageResource(R.drawable.icon_plugoff);
                pluginfo2.setText("即時用電量 : " + plugresultSplit[4] + " W");
                sw2.setChecked(false);
                break;
            case "1":
                image2.setImageResource(R.drawable.icon_plugon);
                pluginfo2.setText("即時用電量 : " + plugresultSplit[4] + " W");
                sw2.setChecked(true);
                break;
            default:
                break;
        }
    }


    //schedule
    public void sendPlugNumber1(View view){
        Intent intent = new Intent(Pluginfo.this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumber2(View view){
        Intent intent = new Intent(this,PlugSetActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //history chart
    public void sendPlugNumberChart1(View view ){
        Intent intent = new Intent(this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","001");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }
    public void sendPlugNumberChart2(View view ){
        Intent intent = new Intent(Pluginfo.this,MPchartActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("Num","002");
        bundle.putString("Equipment","Plug");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    //PlugSwitchClick 1 2 3 4
    public void SwitchClick(){
        sw1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (sw1.isChecked()) {
                    image1.setImageResource(R.drawable.icon_plugon);
                    plugname = "001";
                    plugstatus = "1" ;
                    plugall = null;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        plugstatus = plugresultSplit[1] ;
                        //plugresultSplit[0]->name～plugresultSplit[1]->status～plugresultSplit[2]->～
                        pluginfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {

                        e.printStackTrace();
                    }
                } else {
                    plugname = "001";
                    plugstatus = "0" ;
                    plugall = null;
                    image1.setImageResource(R.drawable.icon_plugoff);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                            pluginfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
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
                    image2.setImageResource(R.drawable.icon_plugon);
                   // plugname2.setText("插座002");
                    plugname = "002";
                    plugstatus = "1" ;
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo2.setText("即時用電量 : " + plugresultSplit[4] +" W");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                } else {
                    plugname = "002";
                    plugstatus = "0" ;
                    image2.setImageResource(R.drawable.icon_plugoff);
                    try {
                        ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new ControlPlugStatusAsynctask(Pluginfo.this);
                        plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                        String plugresultSplit[] =plugall.split(" ");
                        pluginfo2.setText("即時用電量 : " + plugresultSplit[4] +" W");
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
