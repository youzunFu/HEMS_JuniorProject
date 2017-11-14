package net.ddns.b505.hems;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
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

public class Air505leftControlActivity extends AppCompatActivity {

    private ImageView fan_low, fan_normal, fan_large, func_auto, func_ac, func_humi
            , func_fan, func_heat;
    private Button Fanset, Funcset, Power;
    private ImageButton btnTempadd, btnTempsub;
    private TextView tvTemp;
    private int i = 1, j = 1, on_off = 0;

    public  String ctrltype,ctrlresultonoff,ctrlresultup,ctrlresultdown,ctrlresultauto;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air505left_control);
        //fan3 types  little middle large  (ＮＯ Function)
        fan_low = (ImageView)findViewById(R.id.img_fan_low_505left);
        fan_normal = (ImageView)findViewById(R.id.img_fan_nor_505left);
        fan_large = (ImageView)findViewById(R.id.img_fan_large);
        // func 5 types auto～air conditioner～dehumidificational～fanout～heat　ｏｎｌｙ　ａｉｒ　ｃｏｎｄｉｃｔｉｏｎｅｒ
        func_auto = (ImageView)findViewById(R.id.img_func_auto_505left);
        func_ac = (ImageView)findViewById(R.id.img_func_ac_505left);
        func_humi = (ImageView)findViewById(R.id.img_func_humi_505left);
        func_fan = (ImageView)findViewById(R.id.img_func_fan_505left);
        func_heat = (ImageView)findViewById(R.id.img_func_heat_505left);

        Fanset = (Button) findViewById(R.id.btn_fan_set_505left);//FanSpeedButton
        Funcset = (Button) findViewById(R.id.btn_func_set_505left);//FuncButton
        Power = (Button) findViewById(R.id.btn_power_505left);  //　Ｉ／Ｏ

        btnTempadd = (ImageButton) findViewById(R.id.imgbtn_temp_add_505left); //TempeartureUp
        btnTempsub = (ImageButton) findViewById(R.id.imgbtn_temp_sub_505left); //TempeartureDown
        tvTemp = (TextView) findViewById(R.id.tv_temp_505left); //TemperatureTextView

        Fanset.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (i%3) {
                    case 1:
                        fan_large.setVisibility(View.INVISIBLE);
                        fan_low.setVisibility(View.VISIBLE);
                        i++;
                        break;
                    case 2:
                        fan_low.setVisibility(View.INVISIBLE);
                        fan_normal.setVisibility(View.VISIBLE);
                        i++;
                        break;
                    case 0:
                        fan_normal.setVisibility(View.INVISIBLE);
                        fan_large.setVisibility(View.VISIBLE);
                        i = 1;
                        break;
                }
            }
        });
        Funcset.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch (j%5) {
                    case 1:
                        func_heat.setVisibility(View.INVISIBLE);
                        func_auto.setVisibility(View.VISIBLE);
                        j++;
                        break;
                    case 2:
                        func_auto.setVisibility(View.INVISIBLE);
                        func_ac.setVisibility(View.VISIBLE);
                        j++;
                        break;
                    case 3:
                        func_ac.setVisibility(View.INVISIBLE);
                        func_humi.setVisibility(View.VISIBLE);
                        j++;
                        break;
                    case 4:
                        func_humi.setVisibility(View.INVISIBLE);
                        func_fan.setVisibility(View.VISIBLE);
                        j++;
                        break;
                    case 0:
                        func_fan.setVisibility(View.INVISIBLE);
                        func_heat.setVisibility(View.VISIBLE);
                        j = 1;
                        break;
                }
            }
        });
        Power.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try {
                    if (on_off == 0) {
                        ctrltype = "on";
                        tvTemp.setText("28");
                        on_off = 1;
                    }
                    else if(on_off == 1) {
                        ctrltype = "off";
                        tvTemp.setText(null);
                        on_off = 0;
                    }
                    CtrlpageAsynctask ctrlpageAsynctaskonoff = new CtrlpageAsynctask(Air505leftControlActivity.this);
                    ctrlpageAsynctaskonoff.execute(ctrltype);
                    //ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
                    //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultonoff),Toast.LENGTH_SHORT);
                } catch (Exception e){
                    e.printStackTrace();}
            }
        });
        btnTempadd.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    int temp = Integer.parseInt(tvTemp.getText().toString());
                    if(on_off == 1 && temp != 28) {
                        temp++;
                        ctrltype = String.valueOf(temp);
                        tvTemp.setText(ctrltype);
                        Toast.makeText(Air505leftControlActivity.this,ctrltype,Toast.LENGTH_SHORT).show();

                        CtrlpageAsynctask ctrlpageAsynctaskup = new CtrlpageAsynctask(Air505leftControlActivity.this);
                        ctrlpageAsynctaskup.execute(ctrltype);
                    }
                    //ctrlresultup = ctrlpageAsynctaskup.execute(ctrltype).get().toString();
                    //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultup),Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        btnTempsub.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                try{
                    int temp = Integer.parseInt(tvTemp.getText().toString());
                    if(on_off == 1 && temp != 16){
                        temp--;
                        ctrltype = String.valueOf(temp);
                        tvTemp.setText(ctrltype);
                        CtrlpageAsynctask ctrlpageAsynctaskdown = new CtrlpageAsynctask(Air505leftControlActivity.this);
                        ctrlpageAsynctaskdown.execute(ctrltype);
                    }
                    //ctrlresultdown = ctrlpageAsynctaskdown.execute(ctrltype).get().toString();
                    //Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultdown),Toast.LENGTH_SHORT).show();
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    public void autoclick(View v)throws ExecutionException, InterruptedException{
        ctrltype = "auto";
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(Air505leftControlActivity.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
    }

    //AirControl Asynctask
    public static class CtrlpageAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        CtrlpageAsynctask (Context ctx) {context = ctx ;}
        @Override
        protected String doInBackground(String... params) {
            String ctrltype = params[0];
            String login_url = "http://192.168.0.102/b505left.php" ; //http://163.18.57.42:81/b505left.php          //http://192.168.0.110/b505left.php


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

    public void onBackPressed() {finish();}

}

