package net.ddns.b505.hems;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
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
import java.util.concurrent.ExecutionException;

public class AirControlActivity extends AppCompatActivity {

    private ImageView fan_low, fan_normal, fan_large, func_auto, func_ac, func_humi, func_fan, func_heat;
    private Button Fanset, Funcset, Power, ModeBtn, TimeSetBtn;
    private ImageButton btnTempadd, btnTempsub;
    private TextView tvTemp, tvTimeSet;
    private int i = 1, on_off = 0, m = 1; //switch case variable
    private String FanStr, ModeStr, TempStr, TimeSetStr;
    public String ctrltype, ctrlresultonoff, ctrlresultup, ctrlresultdown, ctrlresultauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_control);
        //fan3 types  little middle large  (ＮＯ Function)
        fan_low = (ImageView) findViewById(R.id.img_fan_low);
        fan_normal = (ImageView) findViewById(R.id.img_fan_nor);
        fan_large = (ImageView) findViewById(R.id.img_fan_large);
        // func 5 types auto～air conditioner～dehumidificational～fanout～heat　ｏｎｌｙ　ａｉｒ　ｃｏｎｄｉｃｔｉｏｎｅｒ
        func_auto = (ImageView) findViewById(R.id.img_func_auto);
        func_ac = (ImageView) findViewById(R.id.img_func_ac);
        func_humi = (ImageView) findViewById(R.id.img_func_humi);
        func_fan = (ImageView) findViewById(R.id.img_func_fan);
        func_heat = (ImageView) findViewById(R.id.img_func_heat);

        TimeSetBtn = (Button) findViewById(R.id.btn_timeset);
        ModeBtn = (Button) findViewById(R.id.btn_mode);
        Fanset = (Button) findViewById(R.id.btn_fan);//FanSpeedButton
        Funcset = (Button) findViewById(R.id.btn_func_set);//FuncButton
        Power = (Button) findViewById(R.id.btn_power);  //Ｉ／Ｏ

        btnTempadd = (ImageButton) findViewById(R.id.btn_add); //TempeartureUp
        btnTempsub = (ImageButton) findViewById(R.id.btn_sub); //TempeartureDown
        tvTimeSet = (TextView) findViewById(R.id.tv_TimeSet);
        tvTemp = (TextView) findViewById(R.id.tv_temp); //

    }

    public void PowerClick(View v) {
        try {
            if (on_off == 0) {
                ctrltype = "on";
                tvTemp.setText("28");
                on_off = 1;
            } else if (on_off == 1) {
                ctrltype = "off";
                tvTemp.setText(null);
                on_off = 0;
            }
            CtrlpageAsynctask ctrlpageAsynctaskonoff = new CtrlpageAsynctask(AirControlActivity.this);
            ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            Toast.makeText(AirControlActivity.this, ctrltype, Toast.LENGTH_SHORT).show();

            //ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultonoff),Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ModeBtnClick(View v) throws ExecutionException, InterruptedException {
        switch (m % 5) {
            case 1://auto
                func_heat.setVisibility(View.INVISIBLE);
                func_auto.setVisibility(View.VISIBLE);
                ModeBtn.setText("自動");
                ModeStr = "auto";
                m++;
                break;
            case 2:  //cold
                func_auto.setVisibility(View.INVISIBLE);
                func_ac.setVisibility(View.VISIBLE);
                ModeStr = "cold";
                ModeBtn.setText("冷氣");
                TempStr = "28";
                tvTemp.setText("28");
                FanStr = "Auto";
                i = 1;
                TimeSetStr = "0";
                tvTimeSet.setText("0");
                m++;
                break;
            case 3:  //wet
                func_ac.setVisibility(View.INVISIBLE);
                func_humi.setVisibility(View.VISIBLE);
                ModeStr = "wet";
                ModeBtn.setText("除濕");
                TimeSetStr = "null";
                tvTimeSet.setText("0");
                FanStr = "Auto";
                Fanset.setText("自動");
                m++;
                break;
            case 4:  //wind
                func_humi.setVisibility(View.INVISIBLE);
                func_fan.setVisibility(View.VISIBLE);
                ModeBtn.setText("送風");
                ModeStr = "wind";
                FanStr = "Small";
                i = 2;
                Fanset.setText("自動");
                m++;
                break;
            case 0:  //warm
                func_fan.setVisibility(View.INVISIBLE);
                func_heat.setVisibility(View.VISIBLE);
                ModeBtn.setText("暖氣");
                ModeStr = "warm";
                TempStr = "28";
                tvTemp.setText("28");
                FanStr = "Auto";
                i = 1;
                m = 1;
                break;
        }
        callDatabase();
    }

    public void FansetClick(View v) throws ExecutionException, InterruptedException {
        switch (i % 4) {
            case 1:
                fan_large.setVisibility(View.INVISIBLE);
                fan_low.setVisibility(View.VISIBLE);
                Fanset.setText("低");
                FanStr = "Small";
                i++;
                break;
            case 2:
                fan_low.setVisibility(View.INVISIBLE);
                fan_normal.setVisibility(View.VISIBLE);
                Fanset.setText("中");
                FanStr = "Middle";
                i++;
                break;
            case 3:
                fan_normal.setVisibility(View.INVISIBLE);
                fan_large.setVisibility(View.VISIBLE);
                Fanset.setText("高");
                FanStr = "Big";
                i++;
                break;
            case 0:
                fan_large.setVisibility(View.INVISIBLE);
                Fanset.setText("自動");
                FanStr = "Auto";
                i = 1;
                break;
        }
        callDatabase();
    }

    //時間設定待修正  wet /cold 分開
    public void TimeSetBtnClick(View v) throws ExecutionException, InterruptedException {
        int temp = Integer.parseInt(tvTimeSet.getText().toString());
        if (on_off == 0 && temp != 12) {
            temp++;
            TimeSetStr = toString().valueOf(temp);
        } else if (on_off == 0 && temp == 12) {
            temp = 0;
            TimeSetStr = toString().valueOf(temp);
        }
        switch (ModeStr) {
            case "wet":  //如果0_open ctrltype=wetnullAuto   or wetnullSmall
                tvTimeSet.setText(TimeSetStr + "\n" + "Hr開");
                break;

            case "cold":
                tvTimeSet.setText(TimeSetStr + "\n" + "Hr關");
                break;

            default://be continue...
                break;

        }
        callDatabase();
    }

    public void TempAddClick(View v) {
        try {
            int temp = Integer.parseInt(tvTemp.getText().toString());
            if (on_off == 1 && temp != 32) {
                temp++;
                TempStr = toString().valueOf(temp);
                tvTemp.setText(TempStr);
                callDatabase();
            }
            //ctrlresultup = ctrlpageAsynctaskup.execute(ctrltype).get().toString();
            //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultup),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TempSubClick(View v) {
        try {
            int temp = Integer.parseInt(tvTemp.getText().toString());
            if (on_off == 1 && temp != 18) {
                temp--;
                TempStr = toString().valueOf(temp);
                tvTemp.setText(TempStr);
                callDatabase();
            }
            //ctrlresultdown = ctrlpageAsynctaskdown.execute(ctrltype).get().toString();
            //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultdown),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void callDatabase() throws ExecutionException, InterruptedException {

        switch (ModeStr) {
            case "auto":
                //mode:a uto ,wind:Auto ;
                if (FanStr != "Auto") {
                    FanStr = "Auto";
                }
                Fanset.setText("自動");
                ctrltype = "autoAuto";
                break;
            case "cold":   //still need to check
                if (TimeSetStr == "0") {
                    ctrltype = ModeStr + TempStr + FanStr;
                } else {
                    ctrltype = ModeStr + TempStr + FanStr + TimeSetStr + "Close";
                }
                TimeSetBtn.setText(TimeSetStr + "Hr關");
                break;
            case "wet":
                if (TimeSetStr != "null") {
                    TimeSetBtn.setText(TimeSetStr + "Hr開");
                    ctrltype = ModeStr + TimeSetStr + "Open";
                } else {
                    ctrltype = ModeStr + TimeSetStr + FanStr;
                }

                break;
            case "wind":
            if (FanStr == "Auto") {
                FanStr = "Small";
                i = 2;
            }
            Fanset.setText("低");
            ctrltype = ModeStr + FanStr;
                break;
            case "warm":
            if (FanStr != "Auto") {
                FanStr = "Auto";
                i = 1;
            }
            Fanset.setText("自動");
            ctrltype = ModeStr + TempStr + FanStr;
                break;

            default:
            Log.d("ModeStr:", ModeStr);
                break;
        }

        //ctrltype = ModeStr + TempStr + FanStr ;
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(AirControlActivity.this,"ctrltype :" + ctrltype,Toast.LENGTH_SHORT).show();
    }

    public void autoclick(View v)throws ExecutionException, InterruptedException{
        ctrltype = "auto";
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
    }




    //AirControl Asynctask
    public static class CtrlpageAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        CtrlpageAsynctask (Context ctx) {context = ctx ;}
        @Override
        protected String doInBackground(String... params) {
            String ctrltype = params[0];
            String login_url = "http://192.168.0.102/b505leftv2.php" ; //http://163.18.57.42:81/b505left_v2.php /*"http://163.18.58.95:8888/b451-post.php"*/;

            try {
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("ctrl", "UTF-8") + "=" + URLEncoder.encode(ctrltype, "UTF-8");
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
