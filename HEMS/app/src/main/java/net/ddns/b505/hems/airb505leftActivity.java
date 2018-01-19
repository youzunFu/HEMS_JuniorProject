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

public class airb505leftActivity extends AppCompatActivity {

    private  ImageButton btnOnoff,btnMode,btnWindSpeed,btnTimeSet,btnTempAdd,btnTempSub;
    private Button Fanset, Funcset, Power;
    private ImageButton btnTempadd, btnTempsub;
    private TextView tvTempStr,tvAirInfo;
    private String FanStr, ModeStr, TempStr,TimeSetStr,m;
    private int i = 1, on_off = 0,TimeSetNum = 0,TempNum; //switch case variable
     public  String ctrltype,ctrlresultonoff,ctrlresultup,ctrlresultdown,ctrlresultauto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_air_control);

        Fanset = (Button) findViewById(R.id.btn_fan);//FanSpeedButton
        Funcset = (Button) findViewById(R.id.btn_func_set);//FuncButton
        Power = (Button) findViewById(R.id.btn_power);  //Ｉ／Ｏ

        //imageButton ok
        btnOnoff = (ImageButton) findViewById(R.id.btnOnoff); //TempeartureUp
        btnMode = (ImageButton) findViewById(R.id.btnMode);
        btnWindSpeed = (ImageButton) findViewById(R.id.btnWindSpeed);
        btnTimeSet = (ImageButton) findViewById(R.id.btnTimeSet);
        btnTempAdd = (ImageButton) findViewById(R.id.btnTempAdd);
        btnTempSub = (ImageButton) findViewById(R.id.btnTempSub);

        tvAirInfo  = (TextView) findViewById(R.id.tvAirInfo);
        tvTempStr = (TextView) findViewById(R.id.tvTempStr); //TemperatureTextView

        ImageView mImg1 = (ImageView) findViewById(R.id.btnOnoff);
        ImageView mImg2 = (ImageView) findViewById(R.id.btnMode);
        ImageView mImg3 = (ImageView) findViewById(R.id.btnWindSpeed);
        ImageView mImg4 = (ImageView) findViewById(R.id.btnTimeSet);
        ImageView mImg5 = (ImageView) findViewById(R.id.btnTempAdd);
        ImageView mImg6 = (ImageView) findViewById(R.id.btnTempSub);

        //將圖片及圓角數值帶入getRoundedCornerBitmap()並放入mImg1內
        mImg1.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.onoff),180.0f));
        mImg2.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.cold),180.0f));
        mImg3.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.windspeed),180.0f));
        mImg4.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.timingset),180.0f));
        mImg5.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.triangle_up),180.0f));
        mImg6.setImageBitmap(getRoundedCornerBitmap(
                BitmapFactory.decodeResource(
                        getResources(), R.drawable.triangle_down),180.0f));

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
                tvTempStr.setText("28");
                on_off = 1;
            } else if (on_off == 1) {
                ctrltype = "off";
                TempNum = 0 ;
                tvTempStr.setText(null);
                on_off = 0;
            }
            CtrlpageAsynctask ctrlpageAsynctaskonoff = new CtrlpageAsynctask(airb505leftActivity.this);
            ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            Toast.makeText(airb505leftActivity.this, ctrltype, Toast.LENGTH_SHORT).show();

            tvAirInfo.setText("模式 : " + ModeStr + "風量 : " + FanStr + "定時 開/關 : " + TimeSetStr);
            //ctrlresultonoff = ctrlpageAsynctaskonoff.execute(ctrltype).get().toString();
            //Toast.makeText(airb505leftActivity.this,String.valueOf(ctrlresultonoff),Toast.LENGTH_SHORT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void ModeBtnClick(View v) throws ExecutionException, InterruptedException {
        switch (m) {
            case "1"://auto
                ModeStr = "auto";
                FanStr = "Auto";
                m = "2" ;
                break;
            case "2":  //cold
                ModeStr = "cold";
                TempNum = 28 ;
                TempStr = "28";
                TimeSetNum = 0;
                TimeSetStr = "null";
                tvTempStr.setText("28");
                FanStr = "Auto";
                i = 1;
              // TimeSetStr = "0";
                TimeSetNum = 0;
                m = "3" ;
                break;
            case "3":  //wet
                ModeStr = "wet";
                TimeSetNum = 0;
                TimeSetStr = "null";
                m = "4" ;
                break;
            case "4":  //wind
                ModeStr = "wind";
                FanStr = "Small";
                i = 2;
                m = "5" ;
                break;
            case "5":  //warm
                ModeStr = "warm";
                TempNum = 28 ;
                TempStr = "28";
                FanStr = "Auto";
                tvTempStr.setText("28");
                i = 0 ;
                m = "0" ;
                break;
        }
        callDatabase();
    }

    public void FansetClick(View v) throws ExecutionException, InterruptedException {
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

    //時間設定待修正  wet /cold 分開
    public void TimeSetBtnClick(View v) throws ExecutionException, InterruptedException {
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
                Toast.makeText(airb505leftActivity.this,"This situation don't support this",Toast.LENGTH_SHORT).show();
                break;

        }
        callDatabase();
    }

    public void TempAddClick(View v) {
        try {
            if (on_off == 1 && TimeSetNum != 32) {
                TimeSetNum++;
                TempStr = toString().valueOf(TimeSetNum);
                tvTempStr.setText(TempStr);
                callDatabase();
            }
            //ctrlresultup = ctrlpageAsynctaskup.execute(ctrltype).get().toString();
            //Toast.makeText(airb505leftActivity.this,String.valueOf(ctrlresultup),Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void TempSubClick(View v) {
        try {
            if (on_off == 1 && TimeSetNum != 18) {
                TimeSetNum--;
                TempStr = toString().valueOf(TimeSetNum);
                tvTempStr.setText(TempStr);
                callDatabase();
            }
            //ctrlresultdown = ctrlpageAsynctaskdown.execute(ctrltype).get().toString();
            //Toast.makeText(airb505leftActivity.this,String.valueOf(ctrlresultdown),Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(airb505leftActivity.this,"自動模式中 風量只有自動選項",Toast.LENGTH_SHORT).show();
                }
                ctrltype = "autoAuto";
                break;
            case "cold":   //still need to check
                if (TimeSetStr == "0") {
                    ctrltype = ModeStr + TempStr + FanStr;
                    tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 關 : " + "");
                } else {
                    ctrltype = ModeStr + TempStr + FanStr + TimeSetStr + "Close";
                    tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 關 : " + TimeSetStr + "小時");
                }
                break;
            case "wet":
                if (TimeSetStr != "0") {
                    ctrltype = ModeStr + TimeSetStr + "Open";
                    tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 開 : " + TimeSetStr + "小時");
                } else {
                    ctrltype = ModeStr + "null" + FanStr;
                    tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 開 : " + "");
                }

                break;
            case "wind":
                if (FanStr == "Auto") {
                    FanStr = "Small";
                    i = 2;
                }
                ctrltype = ModeStr + FanStr;
                tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 開 : " + "");
                break;
            case "warm":
                if (FanStr != "Auto") {
                    FanStr = "Auto";
                    i = 1;
                }
                Fanset.setText("自動");
                ctrltype = ModeStr + TempStr + FanStr;
                tvAirInfo.setText("模式 : " + ModeStr + "  風量 : " + FanStr + "  定時 開 : " + "");
                break;

            default:
                Log.d("ModeStr:", ModeStr);
                break;
        }

        //ctrltype = ModeStr + TempStr + FanStr ;
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(airb505leftActivity.this,"ctrltype :" + ctrltype,Toast.LENGTH_SHORT).show();

    }

    public void autoclick(View v)throws ExecutionException, InterruptedException{
        ctrltype = "auto";
        CtrlpageAsynctask ctrlpageAsynctaskauto = new CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(ctrltype).get().toString();
        Toast.makeText(airb505leftActivity.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
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
