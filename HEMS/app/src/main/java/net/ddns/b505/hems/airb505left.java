package net.ddns.b505.hems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class airb505left extends AppCompatActivity {

    private LinearLayout BackgroundAirconditioner;
    private  ImageView btnOnoff,airicon;
    private Button btnMode, btnWindSpeed, btnTimeSet;
    private ImageButton btnTempadd, btnTempsub,btnTempSub,btnTempAdd;
    private TextView tvTempStr,tvAirMode,tvAirFan,tvAirTimeSet;
    private String FanStr, ModeStr, TempStr,TimeSetStr,m = "1";
    private int i = 1, on_off = 0,TimeSetNum = 0,TempNum = 28 ; //switch case variable
     public  String ctrltype,ctrlresultonoff,ctrlresultup,ctrlresultdown,ctrlresultauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.airb505left);

        airicon = (ImageView) findViewById(R.id.airicon);
        BackgroundAirconditioner = (LinearLayout) findViewById(R.id.BackgroundAirconditioner);


        //imageButton ok
        btnOnoff = (ImageView) findViewById(R.id.btnOnoff); //TempeartureUp
        btnMode = (Button) findViewById(R.id.btnMode);
        btnWindSpeed = (Button) findViewById(R.id.btnWindSpeed);
        btnTimeSet = (Button) findViewById(R.id.btnTimeSet);
        btnTempAdd = (ImageButton) findViewById(R.id.btnTempAdd);
        btnTempSub = (ImageButton) findViewById(R.id.btnTempSub);

        tvAirMode  = (TextView) findViewById(R.id.tvAirMode);
        tvAirFan  = (TextView) findViewById(R.id.tvAirFan);
        tvAirTimeSet  = (TextView) findViewById(R.id.tvAirTimeSet);
        tvTempStr = (TextView) findViewById(R.id.tvTempStr); //TemperatureTextView

/*        tvTempStr.setTextColor(Color.parseColor("#aaa"));
        btnMode.setTextColor(Color.parseColor("#aaa"));
        btnWindSpeed.setTextColor(Color.parseColor("#aaa"));
        btnTimeSet.setTextColor(Color.parseColor("#aaa"));
        btnTempAdd.setTextColor(Color.parseColor("#aaa"));
        btnTempSub.setTextColor(Color.parseColor("#aaa"));
        */

        //將圖片及圓角數值帶入getRoundedCornerBitmap()並放入mImg1內
        btnOnoff.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.onoff),180.0f));

        ///-------------------外關冷氣初始化----------
        airicon.setImageResource(R.drawable.icon_airoff);
        btnOnoff.setImageResource(R.drawable.icon_poweroff);
        getWindow().setBackgroundDrawableResource(R.drawable.backgroundgray);
        btnMode.setBackgroundResource(R.drawable.round_airoff);
        btnWindSpeed.setBackgroundResource(R.drawable.round_airoff);
        btnTimeSet.setBackgroundResource(R.drawable.round_airoff);

        tvTempStr.setText("");



    }
    // set image Roundcorner
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float roundPx)
    {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(),
                bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    public void PowerClick(View v) {
        try {
            if (on_off == 0) {
                ctrltype = "on";
                TempNum = 28 ;
                tvTempStr.setText("28"+" ℃");
                on_off = 1;

                airicon.setImageResource(R.drawable.icon_airon);
                btnOnoff.setImageResource(R.drawable.icon_poweron);
                BackgroundAirconditioner.setBackgroundResource(R.drawable.background);
                btnMode.setBackgroundResource(R.drawable.round_air);
                btnWindSpeed.setBackgroundResource(R.drawable.round_air);
                btnTimeSet.setBackgroundResource(R.drawable.round_air);

                tvAirMode.setText("模式 : 自動");
                tvAirFan.setText("風量 : 自動") ;
                tvAirTimeSet.setText("定時  : " + "關");
            } else if (on_off == 1) {
                ctrltype = "off";
                TempNum = 0 ;
                tvTempStr.setText("");
                tvTempStr.setText(null);
                on_off = 0;

                airicon.setImageResource(R.drawable.icon_airoff);
                btnOnoff.setImageResource(R.drawable.icon_poweroff);
                BackgroundAirconditioner.setBackgroundResource(R.drawable.backgroundgray);
                btnMode.setBackgroundResource(R.drawable.round_airoff);
                btnWindSpeed.setBackgroundResource(R.drawable.round_airoff);
                btnTimeSet.setBackgroundResource(R.drawable.round_airoff);

                tvAirMode.setText("模式 : ");
                tvAirFan.setText("風量 : ") ;
                tvAirTimeSet.setText("定時  : " + "關");
            }
            CtrlpageAsynctask ctrlpageAsynctaskonoff = new CtrlpageAsynctask(airb505left.this);
            ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            Toast.makeText(airb505left.this, ctrltype, Toast.LENGTH_SHORT).show();


            tvAirMode.setText("模式 : " + ModeStr );
            tvAirFan.setText("風量 : " + FanStr);
            tvAirTimeSet.setText("定時  : " + TimeSetStr);

            //ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            //Toast.makeText(airb505left.this,String.valueOf(ctrlresultonoff),Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ModeBtnClick(View v) throws ExecutionException, InterruptedException {
        try {

        if (on_off == 1) { //要修
            switch (m) {
                case "1"://auto
                    ModeStr = "auto";
                    FanStr = "Auto";
                    m = "2";
                    break;
                case "2":  //cold
                    ModeStr = "cold";
                    TempNum = 28;
                    TempStr = "28";
                    TimeSetNum = 0;
                    TimeSetStr = "0";
                    tvTempStr.setText("28"+" ℃");
                    FanStr = "Auto";
                    i = 1;
                    m = "3";
                    break;
                case "3":  //wet
                    ModeStr = "wet";
                    TimeSetNum = 0;
                    TimeSetStr = "0";
                    m = "4";
                    break;
                case "4":  //wind
                    ModeStr = "wind";
                    FanStr = "Small";
                    i = 2;
                    m = "5";
                    break;
                case "5":  //warm
                    ModeStr = "warm";
                    TempNum = 28;
                    TempStr = "28";
                    FanStr = "Auto";
                    tvTempStr.setText("28"+" ℃");
                    i = 1;
                    m = "1";
                    break;
            }
            callDatabase();
        }else { //AirConditioner is turn off now.
            Toast.makeText(airb505left.this, "Please turn on the air conditioner  first.", Toast.LENGTH_SHORT).show();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void FansetClick(View v) throws ExecutionException, InterruptedException {
try{
if (ModeStr.equals("cold")|ModeStr.equals("wet")|ModeStr.equals("wind")){
        if (on_off == 1) { //要修
            switch (i % 4) {
                case 1:
                    FanStr = "Small";
                    i++;
                    break;
                case 2:
                    FanStr = "Middle";
                    i++;
                    break;
                case 3:
                    FanStr = "Big";
                    i++;
                    break;
                case 0:
                    FanStr = "Auto";
                    i = 1;
                    break;
            }
            callDatabase();
        }
        else { //AirConditioner is turn off now.
            Toast.makeText(airb505left.this, "Please turn on the air conditioner  first.", Toast.LENGTH_SHORT).show();
        }
}else {
    Toast.makeText(airb505left.this,"目前模式 風量無法改變",Toast.LENGTH_SHORT).show();
}
    } catch (Exception e) {
        e.printStackTrace();
    }

    }

    //時間設定待修正  wet /cold 分開
    public void TimeSetBtnClick(View v) throws ExecutionException, InterruptedException {
        try{
        if (ModeStr.equals("cold")) {

        if (on_off == 1) { //要修
            switch (ModeStr) {
                case "wet":  //如果0_open ctrltype=wetnullAuto   or wetnullSmall
                    if (on_off == 0 && TimeSetNum != 12) {
                        TimeSetNum++;
                        TimeSetStr = toString().valueOf(TimeSetNum);
                    } else if (on_off == 0 && TimeSetNum == 12) {
                        TimeSetNum = 0;
                        TimeSetStr = toString().valueOf(TimeSetNum);
                    }
                    break;
                case "cold":
                    if (on_off == 1 && TimeSetNum != 12) {
                        TimeSetNum++;
                        TimeSetStr = toString().valueOf(TimeSetNum);
                    } else if (on_off == 1 && TimeSetNum == 12) {
                        TimeSetNum = 0;
                        TimeSetStr = toString().valueOf(TimeSetNum);
                    }
                    break;
                default:
                    Toast.makeText(airb505left.this, "This situation don't support this", Toast.LENGTH_SHORT).show();
                    break;

            }
            callDatabase();
        }
        else { //AirConditioner is turn off now.
            Toast.makeText(airb505left.this, "Please turn on the air conditioner  first.", Toast.LENGTH_SHORT).show();
        }

    }else {
            Toast.makeText(airb505left.this,"目前模式 不支援定時設定",Toast.LENGTH_SHORT).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void TempAddClick(View v) {
        try{
        if (ModeStr.equals("cold")|ModeStr.equals("warm")) {

            if (on_off == 1) { //要修
                try {
                    if (on_off == 1 && TempNum != 32) {
                        TempNum++;
                        TempStr = toString().valueOf(TempNum);
                        tvTempStr.setText(TempStr + " ℃");
                        callDatabase();
                    }
                    //ctrlresultup = ctrlpageAsynctaskup.execute(ctrltype).get().toString();
                    //Toast.makeText(airb505left.this,String.valueOf(ctrlresultup),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else { //AirConditioner is turn off now.
                Toast.makeText(airb505left.this, "Please turn on the air conditioner  first.", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(airb505left.this,"目前模式 溫度無法改變",Toast.LENGTH_SHORT).show();
        }
    } catch (Exception e) {
        e.printStackTrace();
    }
    }

    public void TempSubClick(View v) {
        try{
        if (ModeStr.equals("cold")|ModeStr.equals("warm")) {

            if (on_off == 1) { //要修
                try {
                    if (on_off == 1 && TempNum != 18) {
                        TempNum--;
                        TempStr = toString().valueOf(TempNum);
                        tvTempStr.setText(TempStr + " ℃");
                        callDatabase();
                    }
                    //ctrlresultdown = ctrlpageAsynctaskdown.execute(ctrltype).get().toString();
                    //Toast.makeText(airb505left.this,String.valueOf(ctrlresultdown),Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else { //AirConditioner is turn off now.
                Toast.makeText(airb505left.this, "Please turn on the air conditioner  first.", Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(airb505left.this,"目前模式 溫度無法改變",Toast.LENGTH_SHORT).show();
        }
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
                    i = 1;
                }
                ctrltype = "autoAuto";
                tvAirMode.setText("模式 : " + "自動" );
                tvAirFan.setText("風量 : " + FanStr);
                tvAirTimeSet.setText("定時  : " + "無");
                break;

            case "cold":   //still need to check
                if (TimeSetStr.equals("0")) {
                    ctrltype = ModeStr + TempStr + FanStr;
                    tvAirMode.setText("模式 : " + "冷氣" );
                    tvAirFan.setText("風量 : " + FanStr);
                    tvAirTimeSet.setText("定時  : " + "無");
                } else {
                    ctrltype = ModeStr + TempStr + FanStr + TimeSetStr + "Close";

                    tvAirMode.setText("模式 : " + "冷氣" );
                    tvAirFan.setText("風量 : " + FanStr);
                    tvAirTimeSet.setText("定時  : " + TimeSetStr + "小時");
                }
                break;

            case "wet":
                if (TimeSetStr.equals("0")) {
                    //only Auto&Small FanStr
                    if(i == 1) {
                        FanStr = "Auto";
                    }else if(i != 1){
                        FanStr = "Small";
                        i = 0 ;
                    }
                    ctrltype = ModeStr + "null" + FanStr;
                    tvAirMode.setText("模式 : " + "除濕" );
                    tvAirFan.setText("風量 : " + FanStr);
                    tvAirTimeSet.setText("定時  : " + "無");
                } else {
                    ctrltype = ModeStr + TimeSetStr + "Open";

                    tvAirMode.setText("模式 : " + "除濕" );
                    tvAirFan.setText("風量 : " + FanStr);
                    tvAirTimeSet.setText("定時  : " + TimeSetStr + "小時");
                }

                break;
            case "wind":
                if (FanStr.equals("Auto")) {
                    FanStr = "Small";
                    i = 2;
                }
                ctrltype = ModeStr + FanStr;
                tvAirMode.setText("模式 : " + "送風" );
                tvAirFan.setText("風量 : " + FanStr);
                tvAirTimeSet.setText("定時  : " + "無");
                break;
            case "warm":
                if (FanStr != "Auto") {
                    FanStr = "Auto";
                    i = 1;
                }
                ctrltype = ModeStr + TempStr + FanStr;
                tvAirMode.setText("模式 : " + "暖氣" );
                tvAirFan.setText("風量 : " + FanStr);
                tvAirTimeSet.setText("定時  : " + "無");
                break;

            default:
                Log.d("ModeStr:", ModeStr);
                break;
        }

        //ctrltype = ModeStr + TempStr + FanStr ;
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(airb505left.this,"ctrltype :" + ctrltype,Toast.LENGTH_SHORT).show();

    }

    public void autoclick(View v)throws ExecutionException, InterruptedException{
        ctrltype = "auto";
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(airb505left.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
    }




    //AirControl Asynctask
    public static class CtrlpageAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        CtrlpageAsynctask (Context ctx) {context = ctx ;}
        @Override
        protected String doInBackground(String... params) {
            String ctrltype = params[0];
            String login_url = "http://163.18.57.42:92/b505leftv2.php" ; //http://192.168.0.102/b505left_v2.php /*"http://163.18.58.95:8888/b451-post.php"*/;

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
