package net.ddns.b505.hems;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
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
//import java.util.concurrent.ExecutionException;

public class PlugSelectActivity extends AppCompatActivity {
    String[] func = {"插座1", "插座2", "插座3","插座4"};
    int[] icons = {R.drawable.plugoff,
            R.drawable.plugoff,
            R.drawable.plugoff,
            R.drawable.plugoff};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plug_select);
        ListView list = (ListView) findViewById(R.id.list);
        IconAdapter gAdapter = new IconAdapter();
        list.setAdapter(gAdapter);

        //ListView功能項目觸發
        list.setOnItemClickListener(
                new AdapterView.OnItemClickListener(){
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent;
                        switch ((int) id){
                            //點擊電網
                            case R.drawable.air_large:
                                break;
                            case R.drawable.plug:
                                break;
                            case R.drawable.schedule:
                                break;
                        }
                    }// onItemSelected
                } //new OnItemClickListener
        ); //grid.setOnItemClickListener

    }


    class IconAdapter extends BaseAdapter {

        //回傳ListView中項目的個數
        @Override
        public int getCount() {
            return func.length;
        }

        //回傳position所對應的資源
        @Override
        public Object getItem(int position) {
            return func[position];
        }

        //回傳position所對應的id值
        @Override
        public long getItemId(int position) {
            return icons[position];
        }

        //依照position產生相對應的功能項目
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            if (row == null){
                row = PlugSelectActivity.this.getLayoutInflater().inflate(R.layout.item_plug, null);
                ImageView image = (ImageView) row.findViewById(R.id.img_plug);
                Switch sw1 = (Switch) row.findViewById(R.id.switch1);
                TextView text = (TextView) row.findViewById(R.id.tv_plugname);
                Button btn_schedule = (Button) row.findViewById(R.id.btn_schedule);
                image.setImageResource(icons[position]);
                text.setText(func[position]);
                btn_scheduleClick(btn_schedule,position,func.length);


            }
            return row;
        }

        public void btn_scheduleClick(View view,int position,long l){
            Toast.makeText(PlugSelectActivity.this,position,Toast.LENGTH_SHORT).show();

        }


    }

/*plug BTN 模擬
    public void autoclick(View v)throws ExecutionException, InterruptedException{
        plugname = "auto"; 根據按下的按鈕傳入值
        plugstatus =      根據按下的狀態更改值
        AirControlActivity.CtrlpageAsynctask ctrlpageAsynctaskauto = new AirControlActivity.CtrlpageAsynctask(this);
        ctrlresultauto = ctrlpageAsynctaskauto.execute(plugname,plugstatus).get().toString();

        Toast.makeText(AirControlActivity.this,String.valueOf(ctrlresultauto),Toast.LENGTH_SHORT).show();
    }

   */
    //AirControl Asynctask
    public static class CtrlpageAsynctask extends AsyncTask<String,Void,String> {;
        Context context;
        CtrlpageAsynctask (Context ctx) {context = ctx ;}
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
                String post_data = URLEncoder.encode("plugname", "UTF-8") + "=" + URLEncoder.encode(plugname, "UTF-8")+"&"+
                URLEncoder.encode("plugstatus", "UTF-8") + "=" + URLEncoder.encode(plugstatus, "UTF-8");
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
