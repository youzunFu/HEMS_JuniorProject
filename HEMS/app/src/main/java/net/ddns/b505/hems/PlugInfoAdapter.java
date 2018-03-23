 package net.ddns.b505.hems;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import java.util.ArrayList;
import java.util.HashMap;

 public class PlugInfoAdapter extends BaseAdapter {
     private Context context;
     private ArrayList<HashMap<String, String>> list;

     private int BASEnum = 0;
    private String BASEpower,BASEstatus,BASEname;
     private ArrayList<HashMap<String, String>> BASEdatas = new ArrayList<HashMap<String,String>>();

     private int currentItem = -1; //用於紀錄點擊的Item的position。為控制item展開的核心

     private RequestQueue requestQueue;
     private String URLgetPlugInfo = "http://163.18.57.43/HEMSphp/plugschedule.php";
     private JsonArrayRequest request;
     private JsonObjectRequest requestOb;


     public PlugInfoAdapter(Context context,
                            ArrayList<HashMap<String, String>> list) {
         super();
         this.context = context;
         this.list = list;
     }

     @Override
     public int getCount() {
         return list.size();
     }

     @Override
     public Object getItem(int position) {
         return list.get(position);
     }

     @Override
     public long getItemId(int position) {
         return position;
     }

     @Override
     public View getView(final int position, View convertView, ViewGroup parent) {
         ViewHolder holder = null;
         if (convertView == null) {
             convertView = LayoutInflater.from(context).inflate(
                     R.layout.item_pluginfo, parent, false);
             holder = new ViewHolder();

             holder.layoutPlugInfo = (LinearLayout) convertView.findViewById(R.id.layoutPlugInfo);

             holder.tvctrlInfo = (TextView) convertView.findViewById(R.id.tvctrlInfo);
             holder.tvctrlName = (TextView) convertView.findViewById(R.id.tvctrlName);
             holder.imgctrlPlug = (ImageView) convertView.findViewById(R.id.imgctrlPlug);
//-volley  / btn Click
             holder.btnctrlSchedule = (Button) convertView.findViewById(R.id.btnctrlSchedule);
             holder.swctrl = (Switch) convertView.findViewById(R.id.swctrl);


             convertView.setTag(holder);
         } else {
             holder = (ViewHolder) convertView.getTag();
         }

         HashMap<String, String> item = list.get(position);

         //注意：我們在此給響應點擊事件的區域 (目前以 showArea 的線性布局) 加入Tag。為了紀錄點擊的position。使用position設置Tag
         holder.layoutPlugInfo.setTag(position);

         holder.tvctrlName.setText(item.get("name"));
         String status = item.get("status");

         switch (status){
             case "0" :
                 holder.imgctrlPlug.setImageResource(R.drawable.icon_plugoff);
                 holder.tvctrlInfo.setText("即時用電量 : " + (item.get("power")) +" W");
                 holder.swctrl.setChecked(false);
                 break;
             case "1" :
                 holder.imgctrlPlug.setImageResource(R.drawable.icon_plugon);
                 holder.tvctrlInfo.setText("即時用電量 : " + (item.get("power")) +" W");
                 holder.swctrl.setChecked(true);
                 break;
             default:
                 break;
         }


         holder.btnctrlSchedule.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View view) {
                 //--intent to PlugSetActivity 設定排程 180226
                 Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();

                 //通知adapter數據改變需要再一次載入
                 notifyDataSetChanged();
             }
         });

         holder.swctrl.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
             @Override
             public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                 //--開關插座 volley DB

                 /*
                 if (sw1.isChecked()) {
                 
                       // holder.imgctrlPlug.setImageResource(R.drawable.icon_plugon);

                     image1.setImageResource(R.drawable.icon_plugon);
                     plugname = "001";
                     plugstatus = "1" ;
                     plugall = null;
                     try {
                         Pluginfo.ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new Pluginfo.ControlPlugStatusAsynctask(Pluginfo.this);
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

                     // holder.imgctrlPlug.setImageResource(R.drawable.icon_plugoff);

                     image1.setImageResource(R.drawable.icon_plugoff);
                     try {
                         Pluginfo.ControlPlugStatusAsynctask ctrlPlugnameAsynctask = new Pluginfo.ControlPlugStatusAsynctask(Pluginfo.this);
                         plugall = ctrlPlugnameAsynctask.execute(plugname,plugstatus).get().toString();
                         String plugresultSplit[] =plugall.split(" ");
                         pluginfo1.setText("即時用電量 : " + plugresultSplit[4] +" W");
                     } catch (InterruptedException e) {
                         e.printStackTrace();
                     } catch (ExecutionException e) {
                         e.printStackTrace();
                     }

                 }
                 */
             }
         });


         return convertView;
     }

/*
     public void getPlugInfo(){
         JsonStr mJsonStr = new JsonStr();
         mJsonStr.setAction("getPlugInfo");
         mJsonStr.setEq_type("plug");
         mJsonStr.setToken("mDSbpZrRXACEsBE8WR34");
         Gson gson = new Gson();
         String json = gson.toJson(mJsonStr);

         request = new JsonArrayRequest(Request.Method.POST, URLgetPlugInfo, json, new Response.Listener<JSONArray>() {
             @Override
             public void onResponse(JSONArray response) {
                 try {
                     Log.d("response",response.toString());

                     try{
                         while(response.getJSONObject(BASEnum).getString("name") != null){
                             BASEnum++;
                         }
                         Log.d("BASEnum",""+BASEnum);
                     } catch (Exception  e) {
                         e.printStackTrace();
                     }

                     //Log.d(response.getString(0), "request");

//-----------------------------把回傳資料顯示在 個別控制的插座上 ------- 180227
                     for(int i = 0; i < BASEnum; i++){

                         BASEpower = response.getJSONObject(i).getString("power") ;
                         BASEstatus = response.getJSONObject(i).getString("status");
                         BASEname = response.getJSONObject(i).getString("name");

                     }


                 } catch (Exception  e) {
                     e.printStackTrace();
                 }
             }
         }, new Response.ErrorListener() {
             @Override
             public void onErrorResponse(VolleyError error) {
             }
         });
         requestQueue.add(request);
     }
*/

     private static class ViewHolder {
         private LinearLayout layoutPlugInfo;

         private TextView tvctrlInfo,tvctrlName;
         private Button btnctrlSchedule;
         private Switch swctrl;
         private ImageView imgctrlPlug;


     }
 }