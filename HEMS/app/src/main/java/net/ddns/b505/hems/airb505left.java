package net.ddns.b505.hems;

import android.app.DialogFragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.ddns.b505.hems.AboutFragment.ControlAirAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlLightAboutFragment;
import net.ddns.b505.hems.AboutFragment.ControlPlugAboutFragment;
import net.ddns.b505.hems.AboutFragment.DRAboutFragment;
import net.ddns.b505.hems.AboutFragment.HistoryAboutFragment;

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
import java.util.concurrent.TimeoutException;

public class airb505left extends AppCompatActivity {

    private LinearLayout BackgroundAirconditioner;
    private  ImageView btnOnoff,airicon;
    private Button btnMode, btnWindSpeed, btnTimeSet,btnTempSub,btnTempAdd;
    private ImageButton btnTempadd, btnTempsub;
    private TextView tvTempStr,tvAirMode,tvAirFan,tvAirTimeSet;
    private String FanStr, ModeStr, TempStr,TimeSetStr,m = "1";
    private int i = 1, on_off = 0,TimeSetNum = 0,TempNum = 28 ; //switch case variable
     public  String ctrltype,ctrlresultonoff,ctrlresultup,ctrlresultdown,ctrlresultauto;
    private Toolbar toolbarair;
    public String acall = "";
    public String  URL[] = {"http://163.18.57.42:92/b505leftv2.php","http://163.18.57.43/HEMSphp/WINb505left.php"};
    private SwipeRefreshLayout mSwipeRefreshLayout ;

    //163.18.57.42:92 固定ip Raspberry Pi (加try catch 解決 網路問題)
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
        btnTempAdd = (Button) findViewById(R.id.btnTempAdd);
        btnTempSub = (Button) findViewById(R.id.btnTempSub);

        tvAirMode  = (TextView) findViewById(R.id.tvAirMode);
        tvAirFan  = (TextView) findViewById(R.id.tvAirFan);
        tvAirTimeSet  = (TextView) findViewById(R.id.tvAirTimeSet);
        tvTempStr = (TextView) findViewById(R.id.tvTempStr); //TemperatureTextView
        toolbarair = (Toolbar) findViewById(R.id.ToolBarAir);
        toolbarair.setTitle("　　　冷　氣　控　制");
        toolbarair.setTitleTextColor(Color.WHITE);
        setSupportActionBar(toolbarair);


        //toolbarair.setclic
        //將圖片及圓角數值帶入getRoundedCornerBitmap()並放入mImg1內
        btnOnoff.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.onoff),180.0f));

        ///-------------------外關冷氣初始化----------
        airicon.setImageResource(R.drawable.airicon2);
        btnOnoff.setImageResource(R.drawable.icon_poweroff);

        btnMode.setBackgroundResource(R.drawable.round_airoff);
        btnWindSpeed.setBackgroundResource(R.drawable.round_airoff);
        btnTimeSet.setBackgroundResource(R.drawable.round_airoff);
        BackgroundAirconditioner.setBackgroundColor(0x808080);
        //tvAirMode.setTextColor(0xaaa);

        tvTempStr.setText("");


        try {
           initialac();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.refresh_airb505left);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                try {
                    initialac();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        // 依照id判斷點了哪個項目並做相應事件
        if (id == R.id.ItemAir) {
            FragmentTransaction ft1 = getFragmentManager().beginTransaction();
            android.app.Fragment prev1 = getFragmentManager().findFragmentByTag("ItemAir");
            if (prev1 != null) {
                ft1.remove(prev1);
            }
            ft1.addToBackStack(null);
            // Create and show the dialog.
            DialogFragment newFragment1 = new ControlAirAboutFragment();
            newFragment1.show(ft1, "ItemAir");

            return true;
        }else if(id == R.id.ItemAirExit){
            Intent intent = new Intent(airb505left.this, MainActivity.class);
            startActivity(intent);
            return true;
    }

        return false;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_air, menu);
        return true;
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

                on_off = 1;

                airicon.setImageResource(R.drawable.airicon2);
                btnOnoff.setImageResource(R.drawable.icon_poweron);


                btnMode.setBackgroundResource(R.drawable.round_air);
                btnWindSpeed.setBackgroundResource(R.drawable.round_air);
                btnTimeSet.setBackgroundResource(R.drawable.round_air);

                ModeStr = "auto";
                FanStr = "auto";
                TimeSetStr = "0";

                tvTempStr.setText("28"+" ℃");
                tvAirMode.setText("模式 : 自動");
                tvAirFan.setText("風量 : 自動") ;
                tvAirTimeSet.setText("定時  : " + "關");

                BackgroundAirconditioner.setBackgroundColor(getResources().getColor(R.color.brightblue));
                tvAirMode.setTextColor(getResources().getColor(R.color.black));
                tvAirFan.setTextColor(getResources().getColor(R.color.black));
                tvAirTimeSet.setTextColor(getResources().getColor(R.color.black));

                btnTempAdd.setTextColor(getResources().getColor(R.color.black));
                btnTempSub.setTextColor(getResources().getColor(R.color.black));

            } else if (on_off == 1) {
                ctrltype = "off";
                TempNum = 0 ;
                tvTempStr.setText("");
                tvTempStr.setText(null);
                on_off = 0;

                airicon.setImageResource(R.drawable.airicon2);
                btnOnoff.setImageResource(R.drawable.icon_poweroff);
                BackgroundAirconditioner.setBackgroundColor(getResources().getColor(R.color.darker_gray));

                tvAirMode.setText(null);
                tvAirFan.setText(null);
                tvAirTimeSet.setText(null);
                tvTempStr.setText(null);
                btnMode.setBackgroundResource(R.drawable.round_airoff);
                btnWindSpeed.setBackgroundResource(R.drawable.round_airoff);
                btnTimeSet.setBackgroundResource(R.drawable.round_airoff);

                btnTempAdd.setTextColor(getResources().getColor(R.color.darker_gray));
                btnTempSub.setTextColor(getResources().getColor(R.color.darker_gray));

            }

            for(int j = 0 ; j<2 ;j++){
                CtrlpageAsynctask ctrlpageAsynctaskonoff = new CtrlpageAsynctask(airb505left.this);
                ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype,URL[j]).get().toString();
            }
           //Toast.makeText(airb505left.this, ctrltype, Toast.LENGTH_SHORT).show();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void ModeBtnClick(View v) throws ExecutionException, InterruptedException ,TimeoutException{
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
            Toast.makeText(airb505left.this, "請先開啟冷氣", Toast.LENGTH_SHORT).show();
        }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void FansetClick(View v) throws ExecutionException, InterruptedException,TimeoutException {
try{    //&&(TimeSetStr.equals("0"))
if ((ModeStr.equals("cold")) |ModeStr.equals("wet")|ModeStr.equals("wind")){
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
            Toast.makeText(airb505left.this, "請先開啟冷氣", Toast.LENGTH_SHORT).show();
        }
}else {
    Toast.makeText(airb505left.this,"目前模式 風量無法改變",Toast.LENGTH_SHORT).show();
}
    } catch (Exception e) {
        e.printStackTrace();
    }

    }
    //時間設定待修正  wet /cold 分開
    public void TimeSetBtnClick(View v) throws ExecutionException, InterruptedException ,TimeoutException{
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
                    Toast.makeText(airb505left.this, "目前模式 不支援定時設定", Toast.LENGTH_SHORT).show();
                    break;

            }
            callDatabase();
        }
        else { //AirConditioner is turn off now.
            Toast.makeText(airb505left.this, "請先開啟冷氣", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(airb505left.this, "請先開啟冷氣", Toast.LENGTH_SHORT).show();
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


    public void callDatabase() throws ExecutionException, InterruptedException,TimeoutException {
        switch (ModeStr) {
            case "auto":
                //mode:a uto ,wind:Auto ;
                if (FanStr != "Auto") {
                    FanStr = "Auto";
                    i = 1;
                }
                ctrltype = "autoAuto";
                tvAirMode.setText(getModeStr(ModeStr));
                tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                tvAirFan.setText(getWindStr(FanStr));
                break;

            case "cold":   //still need to check
                if (TimeSetStr.equals("0")) {
                    ctrltype = ModeStr + TempStr + FanStr;
                    tvAirMode.setText(getModeStr(ModeStr));
                    tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                    tvAirFan.setText(getWindStr(FanStr));
                } else {
                    ctrltype = ModeStr + TempStr + FanStr + TimeSetStr + "Close";

                    tvAirMode.setText(getModeStr(ModeStr));
                    tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                    tvAirFan.setText(getWindStr(FanStr));
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
                    tvAirMode.setText(getModeStr(ModeStr));
                    tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                    tvAirFan.setText(getWindStr(FanStr));
                } else {
                    ctrltype = ModeStr + TimeSetStr + "Open";
                    tvAirMode.setText(getModeStr(ModeStr));
                    tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                    tvAirFan.setText(getWindStr(FanStr));

                }

                break;
            case "wind":
                if (FanStr.equals("Auto")) {
                    FanStr = "Small";
                    i = 2;
                }
                ctrltype = ModeStr + FanStr;
                tvAirMode.setText(getModeStr(ModeStr));
                tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                tvAirFan.setText(getWindStr(FanStr));
                break;
            case "warm":
                if (FanStr != "Auto") {
                    FanStr = "Auto";
                    i = 1;
                }
                ctrltype = ModeStr + TempStr + FanStr;
                tvAirMode.setText(getModeStr(ModeStr));
                tvAirTimeSet.setText(getTimerStr(TimeSetStr));
                tvAirFan.setText(getWindStr(FanStr));
                break;

            default:
                Log.d("ModeStr:", ModeStr);
                break;
        }



        try{
            for(int j = 0 ; j<2 ;j++){
                CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
                ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype,URL[j]).get().toString();
       // Toast.makeText(airb505left.this,"ctrltype :" + ctrltype,Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void autoclick(View v)throws ExecutionException, InterruptedException,TimeoutException{
        try{
        ctrltype = "auto";
        for(int j = 0 ; j<2 ;j++){
            CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
            ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype,URL[j]).get().toString() ;
        }
       // Toast.makeText(airb505left.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
        }catch(Exception e){
        e.printStackTrace();

        }
    }

    /*
    public String getModeStr(String mode){
        String ModeChinese = "";
        switch(mode) {
            case "auto": ModeChinese = "自動";break;
            case "cold": ModeChinese = "冷氣";break;
            case "wet":  ModeChinese = "除濕";break;
            case "wind": ModeChinese = "送風";break;
            case "warm": ModeChinese = "暖氣";break;
        }
        return ModeChinese;
    }
    public String getTimerStr(String timer){
        String TimerChinese = "";
        switch(timer) {
            case "no":TimerChinese = "無";break;
            case "1":TimerChinese = "1 小時";break;
            case "2":TimerChinese = "2 小時";break;
            case "3":TimerChinese = "3 小時";break;
            case "4":TimerChinese = "4 小時";break;
            case "5":TimerChinese = "5 小時";break;
            case "6":TimerChinese = "6 小時";break;
            case "7":TimerChinese = "7 小時";break;
            case "8":TimerChinese = "8 小時";break;

        }
        return TimerChinese;
    }
    public String getWindStr(String wind){
        String WindChinese = "";
        switch(wind) {
            case "Small": WindChinese  = "小";break;
            case "Middle": WindChinese  = "中";break;
            case "Big": WindChinese  = "大";break;
        }
        return WindChinese;
    }
*/

    public String getModeStr(String mode){
        String ModeChinese = "";
        switch(mode) {
            case "auto": ModeChinese = "模式 : 自動";break;
            case "Auto": ModeChinese = "模式 : 自動";break;
            case "cold": ModeChinese = "模式 : 冷氣";break;
            case "wet":  ModeChinese = "模式 : 除濕";break;
            case "wind": ModeChinese = "模式 : 送風";break;
            case "warm": ModeChinese = "模式 : 暖氣";break;
            case "no":ModeChinese = "模式 : 自動";break;
            default: ModeChinese = "模式 : 自動"; break;
        }
        return ModeChinese;
    }
    public String getTimerStr(String timer){
        String TimerChinese = "";
        switch(timer) {
            case "no":TimerChinese = "定時 : 無";break;
            case "0":TimerChinese = "定時 : 無";break;
            case "null":TimerChinese = "定時 : 無";break;
            case "1":TimerChinese = "定時 : 1 小時";break;
            case "2":TimerChinese = "定時 : 2 小時";break;
            case "3":TimerChinese = "定時 : 3 小時";break;
            case "4":TimerChinese = "定時 : 4 小時";break;
            case "5":TimerChinese = "定時 : 5 小時";break;
            case "6":TimerChinese = "定時 : 6 小時";break;
            case "7":TimerChinese = "定時 : 7 小時";break;
            case "8":TimerChinese = "定時 : 8 小時";break;
            case "9":TimerChinese = "定時 : 9 小時";break;
            case "10":TimerChinese = "定時 : 10 小時";break;
            case "11":TimerChinese = "定時 : 11 小時";break;
            case "12":TimerChinese = "定時 : 12 小時";break;
            default: TimerChinese = "定時 : 無"; break;
        }
        return TimerChinese;
    }
    public String getWindStr(String wind){
        String WindChinese = "";
        switch(wind) {
            case "no":WindChinese = "風量 : 自動";break;
            case "Auto": WindChinese  = "風量 : 自動";break;
            case "Small": WindChinese  = "風量 : 小";break;
            case "Middle": WindChinese  = "風量 : 中";break;
            case "Big": WindChinese  = "風量 : 大";break;
            default: WindChinese = "風量 : 自動"; break;
        }
        return WindChinese;
    }

    //AirControl Asynctask
    public static class CtrlpageAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        CtrlpageAsynctask (Context ctx) {context = ctx ;}
        @Override
        protected String doInBackground(String... params) {
            String ctrltype = params[0];
            String login_url = params[1] ; //http://192.168.0.102/b505left_v2.php /*"http://163.18.58.95:8888/b451-post.php"*/;

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
            }
            catch (MalformedURLException e) {
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


    public void initialac() throws InterruptedException,ExecutionException{

        getacAsyncTask getacasync = new getacAsyncTask(airb505left.this);
        acall = getacasync.execute("b505left").get().toString();
        String acresultSplit[] =acall.split(" ");
        if(acresultSplit[1].equals("On")){
            on_off = 1;
            airicon.setImageResource(R.drawable.airicon2);
            btnOnoff.setImageResource(R.drawable.icon_poweron);

            btnMode.setBackgroundResource(R.drawable.round_air);
            btnWindSpeed.setBackgroundResource(R.drawable.round_air);
            btnTimeSet.setBackgroundResource(R.drawable.round_air);

            if(acresultSplit[3].equals("no")){ModeStr = "auto";
            }else if(!(acresultSplit[3].equals(null))){ModeStr = acresultSplit[3];
            }else{ModeStr = "auto";}

            if(acresultSplit[5].equals("no")){FanStr = "Auto";
            }else if(!(acresultSplit[5].equals(null))){FanStr = acresultSplit[5];
            }else{FanStr = "auto";}

            if(acresultSplit[6].equals("no")){TimeSetStr = "0";
            }else if(!(acresultSplit[6].equals(null))){TimeSetStr = acresultSplit[6];
            }else{TimeSetStr = "auto";}

            if(acresultSplit[4].equals("no")){TempNum = Integer.valueOf(28);
            }else if(!(acresultSplit[4].equals(null))){TempNum = Integer.valueOf(acresultSplit[4]);
            }else{TempNum = Integer.valueOf(28);}

           // TempNum = Integer.valueOf(acresultSplit[4]);

            tvAirMode.setText(getModeStr(ModeStr));
            tvAirTimeSet.setText(getTimerStr(TimeSetStr));
            tvAirFan.setText(getWindStr(FanStr));
            tvTempStr.setText(TempNum + " ℃");

            BackgroundAirconditioner.setBackgroundColor(getResources().getColor(R.color.brightblue));
            tvAirMode.setTextColor(getResources().getColor(R.color.black));
            tvAirFan.setTextColor(getResources().getColor(R.color.black));
            tvAirTimeSet.setTextColor(getResources().getColor(R.color.black));

            btnTempAdd.setTextColor(getResources().getColor(R.color.black));
            btnTempSub.setTextColor(getResources().getColor(R.color.black));
        }else if(acresultSplit[1].equals("Off")) {

            tvTempStr.setText("");
            tvTempStr.setText(null);
            on_off = 0;

            airicon.setImageResource(R.drawable.airicon2);
            btnOnoff.setImageResource(R.drawable.icon_poweroff);
            BackgroundAirconditioner.setBackgroundColor(getResources().getColor(R.color.darker_gray));

            tvAirMode.setText(null);
            tvAirFan.setText(null);
            tvAirTimeSet.setText(null);
            tvTempStr.setText(null);
            btnMode.setBackgroundResource(R.drawable.round_airoff);
            btnWindSpeed.setBackgroundResource(R.drawable.round_airoff);
            btnTimeSet.setBackgroundResource(R.drawable.round_airoff);

            btnTempAdd.setTextColor(getResources().getColor(R.color.darker_gray));
            btnTempSub.setTextColor(getResources().getColor(R.color.darker_gray));

        }


    }
    public static class getacAsyncTask extends AsyncTask<String,Void,String> {;
        Context context2;
        getacAsyncTask (Context ctx) {context2 = ctx ;}
        @Override
        protected String doInBackground(String... params) {
            try {
                String acname = params[0];
                String login_url = "http://163.18.57.43/HEMSphp/acpost.php";
                URL url = new URL(login_url);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setDoInput(true);
                OutputStream outputStream = httpURLConnection.getOutputStream();
                BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(outputStream, "UTF-8"));
                String post_data = URLEncoder.encode("acname", "UTF-8") + "=" + URLEncoder.encode(acname, "UTF-8");
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
